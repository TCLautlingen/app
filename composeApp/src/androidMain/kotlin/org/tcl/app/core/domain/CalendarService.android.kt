package org.tcl.app.core.domain

import android.content.Intent
import android.provider.CalendarContract
import org.tcl.app.AppContext

actual class CalendarService {
    actual fun openCalendarWithEvent(
        title: String,
        description: String,
        location: String?,
        startTimeMillis: Long,
        endTimeMillis: Long
    ) {
        val intent = Intent(Intent.ACTION_INSERT).apply {
            data = CalendarContract.Events.CONTENT_URI
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            putExtra(CalendarContract.Events.TITLE, title)
            putExtra(CalendarContract.Events.DESCRIPTION, description)
            putExtra(CalendarContract.Events.EVENT_LOCATION, location)
            putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startTimeMillis)
            putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTimeMillis)
        }
        val context = AppContext.get()
        context.startActivity(intent)
    }
}