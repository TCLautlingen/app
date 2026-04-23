package org.tcl.app.core.domain

actual class CalendarService actual constructor() {
    actual fun openCalendarWithEvent(
        title: String,
        description: String,
        location: String?,
        startTimeMillis: Long,
        endTimeMillis: Long
    ) {
    }
}