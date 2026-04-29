package org.tcl.app.util

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

fun LocalTime.plusMinutes(minutes: Int): LocalTime {
    val totalMinutes = hour * 60 + minute + minutes
    return LocalTime(totalMinutes / 60, totalMinutes % 60)
}

fun LocalDate.formatDdMmYyyy(): String {
    val day = day.toString().padStart(2, '0')
    val month = month.number.toString().padStart(2, '0')
    val yearStr = year.toString().padStart(4, '0')

    return "$day.$month.$yearStr"
}

fun LocalDate.formatWeekdayDdMonth(): String {
    val day = day.toString().padStart(2, '0')
    return "${dayOfWeek.germanName()}, $day. ${month.germanName()}"
}

fun LocalDateTime.formatRelative(): String {
    val today = Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault())
        .date

    val daysDiff = today.toEpochDays() - this.date.toEpochDays()

    val day =  when (daysDiff) {
        0L -> "Heute"
        1L -> "Gestern"
        in 2..6 -> dayOfWeek.germanName() // within last 7 days
        else -> this.date.formatWeekdayDdMonth()
    }

    val time = "${time.hour.toString().padStart(2, '0')}:${time.minute.toString().padStart(2, '0')}"
    return "$day, $time"
}

val MONTH_NAMES = listOf("Januar", "Februar", "März", "April", "Mai", "Juni", "Juli", "August", "September", "Oktober", "November", "Dezember")

fun Month.germanName(): String {
    return MONTH_NAMES[ordinal]
}

val WEEKDAY_NAMES = listOf(
    "Montag",
    "Dienstag",
    "Mittwoch",
    "Donnerstag",
    "Freitag",
    "Samstag",
    "Sonntag"
)

fun DayOfWeek.germanName(): String {
    return WEEKDAY_NAMES[ordinal]
}