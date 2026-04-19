package org.tcl.app.core.domain

import platform.EventKit.EKEntityType
import platform.EventKit.EKEvent
import platform.EventKit.EKEventStore
import platform.EventKitUI.EKEventEditViewAction
import platform.EventKitUI.EKEventEditViewController
import platform.EventKitUI.EKEventEditViewDelegateProtocol
import platform.Foundation.NSDate
import platform.Foundation.dateWithTimeIntervalSince1970
import platform.UIKit.UIApplication
import platform.darwin.NSObject
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue

actual class CalendarService actual constructor() : NSObject(), EKEventEditViewDelegateProtocol {
    private val eventStore = EKEventStore()

    actual fun openCalendarWithEvent(
        title: String,
        description: String,
        location: String?,
        startTimeMillis: Long,
        endTimeMillis: Long
    ) {
        eventStore.requestAccessToEntityType(EKEntityType.EKEntityTypeEvent) { granted, _ ->
            if (granted) {
                dispatch_async(dispatch_get_main_queue()) {
                    val event = EKEvent.eventWithEventStore(this.eventStore)
                    event.title = title
                    event.notes = description
                    event.location = location
                    event.startDate = NSDate.dateWithTimeIntervalSince1970(startTimeMillis / 1000.0)
                    event.endDate = NSDate.dateWithTimeIntervalSince1970(endTimeMillis / 1000.0)
                    event.calendar = this.eventStore.defaultCalendarForNewEvents

                    val controller = EKEventEditViewController()
                    controller.eventStore = this.eventStore
                    controller.event = event
                    controller.editViewDelegate = this

                    // Get the root view controller to present from
                    val rootViewController = UIApplication.sharedApplication.keyWindow?.rootViewController
                    rootViewController?.presentViewController(controller, animated = true, completion = null)
                }
            }
        }
    }

    override fun eventEditViewController(
        controller: EKEventEditViewController,
        didCompleteWithAction: EKEventEditViewAction
    ) {
        controller.dismissViewControllerAnimated(true, completion = null)
    }
}
