package org.tcl.app.navigation

import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import org.koin.compose.getKoin
import org.koin.compose.viewmodel.koinViewModel
import org.tcl.app.AppViewModel
import org.tcl.app.auth.presentation.login.AuthLoginRoot
import org.tcl.app.booking.presentation.court.BookingCourtRoot
import org.tcl.app.booking.presentation.editor.BookingEditorRoot
import org.tcl.app.booking.presentation.list.BookingListRoot
import org.tcl.app.booking.presentation.success.BookingSuccessRoot
import org.tcl.app.core.domain.util.onFailure
import org.tcl.app.core.domain.util.onSuccess
import org.tcl.app.notification.presentation.builder.NotificationBuilderRoot
import org.tcl.app.notification.presentation.inbox.NotificationInboxRoot
import org.tcl.app.onboarding.presentation.account.OnboardingAccountRoot
import org.tcl.app.onboarding.presentation.profile.OnboardingProfileRoot
import org.tcl.app.onboarding.presentation.welcome.OnboardingWelcomeRoot
import org.tcl.app.user.domain.UserRemoteDataSource
import org.tcl.app.user.presentation.editor.UserEditorRoot
import org.tcl.app.user.presentation.list.UserListRoot
import org.tcl.app.user.presentation.profile.UserProfileRoot

@Composable
fun NavigationRoot() {
    val appViewModel: AppViewModel = koinViewModel()
    val userRemoteDataSource: UserRemoteDataSource = getKoin().get()
    val appState by appViewModel.state.collectAsStateWithLifecycle()

    val navigationState = rememberNavigationState(
        startRoute = AppGraph.BookingList,
        topLevelRoutes = setOf(AppGraph.OnboardingWelcome, AppGraph.BookingList, AppGraph.BookingCourt, AppGraph.UserProfile),
    )
    val navigator = rememberNavigator(navigationState)
    var isReady by remember { mutableStateOf(false) }

    // Navigate once startup auth check completes
    LaunchedEffect(appState.isLoading) {
        if (!appState.isLoading) {
            if (appState.isLoggedIn) {
                userRemoteDataSource.getCurrentUser()
                    .onSuccess { user ->
                        appViewModel.updateUserId(user.id)
                        navigator.navigate(if (user.firstName.isBlank()) AppGraph.OnboardingProfile else AppGraph.BookingList)
                    }
                    .onFailure { }
            } else {
                navigator.navigate(AppGraph.OnboardingWelcome)
            }
            isReady = true
        }
    }

    // Navigate to welcome when logged out during a session
    LaunchedEffect(appState.isLoggedIn) {
        if (!appState.isLoggedIn && !appState.isLoading) {
            navigator.navigate(AppGraph.OnboardingWelcome)
        }
    }

    BrowserNavigationEffect(remember { derivedStateOf { navigationState.stacksInUse.lastOrNull() } })

    val entryProvider = entryProvider<NavKey> {
        entry<AppGraph.OnboardingWelcome> {
            OnboardingWelcomeRoot(
                onNavigate = { route -> navigator.navigate(route) },
            )
        }

        entry<AppGraph.OnboardingAccount> {
            OnboardingAccountRoot(
                onNavigateBack = { navigator.goBack() },
                onNavigate = { route -> navigator.navigate(route) },
                onRegistered = { appViewModel.setLoggedIn() },
            )
        }

        entry<AppGraph.OnboardingProfile> {
            OnboardingProfileRoot(
                onNavigateBack = { navigator.goBack() },
                onComplete = {
                    navigator.navigate(AppGraph.BookingList)
                },
            )
        }

        entry<AppGraph.AuthLogin> {
            AuthLoginRoot(
                onNavigateBack = { navigator.goBack() },
                onSuccess = {
                    appViewModel.setLoggedIn()
                    navigator.navigate(AppGraph.BookingList)
                },
            )
        }

        entry<AppGraph.BookingList> {
            BookingListRoot(
                onNavigate = { route -> navigator.navigate(route) },
                currentRoute = navigationState.topLevelRoute
            )
        }

        entry<AppGraph.CreateBooking> {
            BookingEditorRoot(
                date = it.date,
                courtId = it.courtId,
                startTime = it.startTime,
                onCourtBooked = { booking ->
                    navigator.navigate(AppGraph.BookingSuccess(booking = booking))
                },
                onNavigateBack = { navigator.goBack() },
            )
        }

        entry<AppGraph.BookingSuccess> {
            BookingSuccessRoot(
                booking = it.booking,
                onNavigateHome = {
                    navigator.navigateToTopLevel()
                }
            )
        }

        entry<AppGraph.BookingCourt> {
            BookingCourtRoot(
                onNavigate = { route -> navigator.navigate(route) },
                currentRoute = navigationState.topLevelRoute
            )
        }

        entry<AppGraph.UserProfile> {
            UserProfileRoot(
                onNavigate = { route -> navigator.navigate(route) },
                onLoggedOut = { appViewModel.setLoggedOut() },
                currentRoute = navigationState.topLevelRoute
            )
        }

        entry<AppGraph.UserList> {
            UserListRoot(
                onNavigate = { route -> navigator.navigate(route) },
                onNavigateBack = { navigator.goBack() },
            )
        }

        entry<AppGraph.UserEditor> {
            UserEditorRoot(
                userId = it.userId,
                onNavigateBack = { navigator.goBack() },
            )
        }

        entry<AppGraph.NotificationInbox> {
            NotificationInboxRoot(
                onNavigateBack = { navigator.goBack() },
            )
        }

        entry<AppGraph.NotificationBuilder> {
            NotificationBuilderRoot(
                onNavigateBack = { navigator.goBack() },
            )
        }
    }

    if (isReady) {
        NavDisplay(
            entries = navigationState.toEntries(entryProvider),
            onBack = navigator::goBack,
            transitionSpec = {
                fadeIn() togetherWith ExitTransition.KeepUntilTransitionsFinished
            },
        )
    }
}
