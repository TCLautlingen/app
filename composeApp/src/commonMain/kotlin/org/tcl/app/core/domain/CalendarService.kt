package org.tcl.app.core.domain

expect class CalendarService() {
    fun openCalendarWithEvent(
        title: String,
        description: String,
        location: String?,
        startTimeMillis: Long,
        endTimeMillis: Long
    )
}