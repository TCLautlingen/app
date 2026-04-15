package org.tcl.app.booking.presentation.editor

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.kizitonwose.calendar.core.minusMonths
import com.kizitonwose.calendar.core.now
import com.kizitonwose.calendar.core.plusMonths
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.YearMonth
import org.koin.compose.viewmodel.koinViewModel
import org.tcl.app.core.presentation.ObserveAsEvents
import zed.rainxch.rikkaui.components.ui.button.Button
import zed.rainxch.rikkaui.components.ui.button.ButtonVariant
import zed.rainxch.rikkaui.components.ui.button.IconButton
import zed.rainxch.rikkaui.components.ui.icon.RikkaIcons
import zed.rainxch.rikkaui.components.ui.scaffold.Scaffold
import zed.rainxch.rikkaui.components.ui.text.Text
import zed.rainxch.rikkaui.components.ui.topappbar.TopAppBar
import zed.rainxch.rikkaui.foundation.RikkaTheme


@Composable
fun BookingEditorRoot(
    date: String? = null,
    court: Int? = null,
    startTime: String? = null,
    onNavigateBack: () -> Unit,
    viewModel: BookingEditorViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(date, court, startTime) {
        viewModel.initialize(date, court, startTime)
    }

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is BookingEditorEvent.CourtBooked -> onNavigateBack()
        }
    }

    BookingEditorScreen(
        state = state,
        onAction = viewModel::onAction,
        onNavigateBack = onNavigateBack,
    )
}

@Composable
fun BookingEditorScreen(
    state: BookingEditorState,
    onAction: (BookingEditorAction) -> Unit,
    onNavigateBack: () -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = "Neue Buchung",
                navigationIcon = {
                    IconButton(
                        icon = RikkaIcons.ArrowLeft,
                        contentDescription = "Back",
                        onClick = onNavigateBack,
                    )
                }
            )
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(RikkaTheme.spacing.lg),
            verticalArrangement = Arrangement.spacedBy(RikkaTheme.spacing.lg)
        ) {
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
                        isSelected = day.date == state.date,
                        onClick = { onAction(BookingEditorAction.OnDateChange(day.date)) }
                    )
                }
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(RikkaTheme.spacing.md),
            ) {
                for (i in intArrayOf(30, 60, 90, 120)) {
                    Button(
                        onClick = { onAction(BookingEditorAction.OnDurationChange(i)) },
                        text = "$i min",
                        variant = if (i == state.duration) ButtonVariant.Default else ButtonVariant.Outline,
                    )
                }
            }

            val availableTimes = state.availableSlots.map { it.startTime }.distinct()
            val availableCourts = state.availableSlots
                .filter { it.startTime == state.startTime }
                .map { it.court }

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(RikkaTheme.spacing.sm),
                verticalArrangement = Arrangement.spacedBy(0.dp),
            ) {
                for (availableTime in availableTimes) {
                    Button(
                        onClick = { onAction(BookingEditorAction.OnStartTimeChange(availableTime)) },
                        text = availableTime,
                        variant = if (availableTime == state.startTime) ButtonVariant.Default else ButtonVariant.Outline,
                    )
                }
            }

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(RikkaTheme.spacing.sm),
                verticalArrangement = Arrangement.spacedBy(0.dp),
            ) {
                for (availableCourt in availableCourts) {
                    Button(
                        onClick = { onAction(BookingEditorAction.OnCourtChange(availableCourt)) },
                        text = "Platz $availableCourt",
                        variant = if (availableCourt == state.court) ButtonVariant.Default else ButtonVariant.Outline,
                    )
                }
            }

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = { onAction(BookingEditorAction.OnBookClick) },
                    text = "Buchen",
                    enabled = !state.isSaving
                )
            }
        }
    }
}

@Composable
fun DayCell(
    day: CalendarDay,
    isSelected: Boolean,
    onClick: (CalendarDay) -> Unit
) {
    val isToday = day.date == LocalDate.now()

    Button(
        onClick = { onClick(day) },
        variant = when {
            isSelected -> ButtonVariant.Default
            isToday -> ButtonVariant.Secondary
            else -> ButtonVariant.Ghost
        }
    ) {
        Text(
            text = day.date.day.toString(),
            color = when {
                isSelected -> RikkaTheme.colors.onPrimary
                else -> RikkaTheme.colors.onBackground
            }
        )
    }
}