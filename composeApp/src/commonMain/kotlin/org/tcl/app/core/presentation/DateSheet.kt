package org.tcl.app.core.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.kizitonwose.calendar.core.minusMonths
import com.kizitonwose.calendar.core.now
import com.kizitonwose.calendar.core.plusMonths
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.YearMonth
import org.tcl.app.booking.presentation.editor.DayCell
import zed.rainxch.rikkaui.components.ui.button.IconButton
import zed.rainxch.rikkaui.components.ui.icon.RikkaIcons
import zed.rainxch.rikkaui.components.ui.sheet.Sheet
import zed.rainxch.rikkaui.components.ui.sheet.SheetAnimation
import zed.rainxch.rikkaui.components.ui.sheet.SheetContent
import zed.rainxch.rikkaui.components.ui.sheet.SheetHeader
import zed.rainxch.rikkaui.components.ui.sheet.SheetSide
import zed.rainxch.rikkaui.components.ui.text.Text

@Composable
fun DateSheet(
    open: Boolean,
    onDismiss: () -> Unit,
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
) {
    Sheet(
        open = open,
        onDismiss = onDismiss,
        side = SheetSide.Bottom,
        animation = SheetAnimation.Slide,
    ) {
        SheetHeader(
            title = "Datum wählen"
        )
        SheetContent {
            val currentMonth = remember { YearMonth.now() }
            val startMonth = remember { currentMonth.minusMonths(100) }
            val endMonth = remember { currentMonth.plusMonths(100) }
            val firstDayOfWeek = remember { firstDayOfWeekFromLocale() }

            val calendarState = rememberCalendarState(
                startMonth = startMonth,
                endMonth = endMonth,
                firstVisibleMonth = currentMonth,
                firstDayOfWeek = firstDayOfWeek
            )

            HorizontalCalendar(
                state = calendarState,
                monthHeader = { month ->
                    val currentMonth = month.yearMonth
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val scope = rememberCoroutineScope()
                        IconButton(
                            icon = RikkaIcons.ArrowLeft,
                            onClick = {
                                scope.launch {
                                    calendarState.scrollToMonth(
                                        calendarState.firstVisibleMonth.yearMonth.minusMonths(1)
                                    )
                                }
                            },
                            contentDescription = "Vorheriger Monat",
                        )

                        Text(
                            text = currentMonth.month.name
                                .lowercase()
                                .replaceFirstChar { it.uppercase() } +
                                    " ${currentMonth.year}"
                        )

                        IconButton(
                            icon = RikkaIcons.ArrowRight,
                            onClick = {
                                scope.launch {
                                    calendarState.scrollToMonth(
                                        calendarState.firstVisibleMonth.yearMonth.plusMonths(1)
                                    )
                                }
                            },
                            contentDescription = "Nächster Monat",
                        )
                    }
                },
                dayContent = { day ->
                    DayCell(
                        day = day,
                        isSelected = day.date == selectedDate,
                        onClick = {
                            onDateSelected(day.date)
                        }
                    )
                }
            )
        }
    }
}