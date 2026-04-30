package org.tcl.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import com.github.terrakok.navigation3.browser.HierarchicalBrowserNavigation
import com.github.terrakok.navigation3.browser.buildBrowserHistoryFragment

@Composable
actual fun BrowserNavigationEffect(currentDestination: State<Any?>) {
    HierarchicalBrowserNavigation(
        currentDestination,
        currentDestinationName = { current ->
            when (current) {
                is AppGraph.OnboardingWelcome -> buildBrowserHistoryFragment("welcome")
                is AppGraph.OnboardingAccount -> buildBrowserHistoryFragment("account")
                is AppGraph.OnboardingProfile -> buildBrowserHistoryFragment("setup-profile")
                is AppGraph.AuthLogin -> buildBrowserHistoryFragment("login")
                is AppGraph.BookingList -> buildBrowserHistoryFragment("bookings")
                is AppGraph.CreateBooking -> buildBrowserHistoryFragment("book")
                is AppGraph.BookingSuccess -> buildBrowserHistoryFragment("booking-success")
                is AppGraph.BookingCourt -> buildBrowserHistoryFragment("courts")
                is AppGraph.UserProfile -> buildBrowserHistoryFragment("profile")
                is AppGraph.UserList -> buildBrowserHistoryFragment("users")
                is AppGraph.UserEditor -> buildBrowserHistoryFragment("edit-user", mapOf("id" to current.userId.toString()))
                is AppGraph.NotificationInbox -> buildBrowserHistoryFragment("notifications")
                is AppGraph.NotificationBuilder -> buildBrowserHistoryFragment("send-notification")
                else -> null
            }
        }
    )
}
