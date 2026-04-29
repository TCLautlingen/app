package org.tcl.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
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
fun AppNavigation() {
    val appViewModel: AppViewModel = koinViewModel()
    val userRemoteDataSource: UserRemoteDataSource = org.koin.compose.getKoin().get()
    val appState by appViewModel.state.collectAsStateWithLifecycle()

    val navStack = rememberNavBackStack(
        configuration = SavedStateConfiguration {
            serializersModule = SerializersModule {
                polymorphic(NavKey::class) {
                    subclass(AppGraph.OnboardingWelcome::class, AppGraph.OnboardingWelcome.serializer())
                    subclass(AppGraph.OnboardingAccount::class, AppGraph.OnboardingAccount.serializer())
                    subclass(AppGraph.OnboardingProfile::class, AppGraph.OnboardingProfile.serializer())
                    subclass(AppGraph.AuthLogin::class, AppGraph.AuthLogin.serializer())
                    subclass(AppGraph.BookingList::class, AppGraph.BookingList.serializer())
                    subclass(AppGraph.CreateBooking::class, AppGraph.CreateBooking.serializer())
                    subclass(AppGraph.BookingSuccess::class, AppGraph.BookingSuccess.serializer())
                    subclass(AppGraph.BookingCourt::class, AppGraph.BookingCourt.serializer())
                    subclass(AppGraph.UserProfile::class, AppGraph.UserProfile.serializer())
                    subclass(AppGraph.UserList::class, AppGraph.UserList.serializer())
                    subclass(AppGraph.UserEditor::class, AppGraph.UserEditor.serializer())
                    subclass(AppGraph.NotificationInbox::class, AppGraph.NotificationInbox.serializer())
                    subclass(AppGraph.NotificationBuilder::class, AppGraph.NotificationBuilder.serializer())
                }
            }
        },
        AppGraph.OnboardingWelcome,
    )

    // Navigate once startup auth check completes
    LaunchedEffect(appState.isLoading) {
        if (!appState.isLoading && appState.isLoggedIn) {
            userRemoteDataSource.getCurrentUser()
                .onSuccess { user ->
                    navStack.clear()
                    navStack.add(if (user.firstName.isBlank()) AppGraph.OnboardingProfile else AppGraph.BookingList)
                }
                .onFailure {
                    navStack.clear()
                    navStack.add(AppGraph.BookingList)
                }
        }
    }

    // Navigate to welcome when logged out (transition from true → false)
    LaunchedEffect(appState.isLoggedIn) {
        if (!appState.isLoggedIn && !appState.isLoading) {
            navStack.clear()
            navStack.add(AppGraph.OnboardingWelcome)
        }
    }

    NavDisplay(
        backStack = navStack,
        onBack = { navStack.removeLastOrNull() },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
        entryProvider = entryProvider {
            entry<AppGraph.OnboardingWelcome> {
                OnboardingWelcomeRoot(
                    onNavigate = { route -> navStack.add(route) },
                )
            }

            entry<AppGraph.OnboardingAccount> {
                OnboardingAccountRoot(
                    onNavigateBack = { navStack.removeLastOrNull() },
                    onNavigate = { route -> navStack.add(route) },
                    onRegistered = { appViewModel.setLoggedIn() },
                )
            }

            entry<AppGraph.OnboardingProfile> {
                OnboardingProfileRoot(
                    onNavigateBack = { navStack.removeLastOrNull() },
                    onComplete = {
                        navStack.clear()
                        navStack.add(AppGraph.BookingList)
                    },
                )
            }

            entry<AppGraph.AuthLogin> {
                AuthLoginRoot(
                    onNavigateBack = { navStack.removeLastOrNull() },
                    onSuccess = {
                        appViewModel.setLoggedIn()
                        navStack.clear()
                        navStack.add(AppGraph.BookingList)
                    },
                )
            }

            entry<AppGraph.BookingList> {
                BookingListRoot(
                    onNavigate = { route -> navStack.add(route) },
                    currentRoute = navStack.lastOrNull() as AppGraph?
                )
            }

            entry<AppGraph.CreateBooking> {
                BookingEditorRoot(
                    date = it.date,
                    courtId = it.courtId,
                    startTime = it.startTime,
                    onCourtBooked = { booking ->
                        navStack.add(AppGraph.BookingSuccess(booking = booking))
                    },
                    onNavigateBack = { navStack.removeLastOrNull() },
                )
            }

            entry<AppGraph.BookingSuccess> {
                BookingSuccessRoot(
                    booking = it.booking,
                    onNavigateHome = {
                        navStack.clear()
                        navStack.add(AppGraph.BookingList)
                    }
                )
            }

            entry<AppGraph.BookingCourt> {
                BookingCourtRoot(
                    onNavigate = { route -> navStack.add(route) },
                    currentRoute = navStack.lastOrNull() as AppGraph?
                )
            }

            entry<AppGraph.UserProfile> {
                UserProfileRoot(
                    onNavigate = { route -> navStack.add(route) },
                    onLoggedOut = { appViewModel.setLoggedOut() },
                    currentRoute = navStack.lastOrNull() as AppGraph?
                )
            }

            entry<AppGraph.UserList> {
                UserListRoot(
                    onNavigate = { route -> navStack.add(route) },
                    onNavigateBack = { navStack.removeLastOrNull() },
                )
            }

            entry<AppGraph.UserEditor> {
                UserEditorRoot(
                    userId = it.userId,
                    onNavigateBack = { navStack.removeLastOrNull() },
                )
            }

            entry<AppGraph.NotificationInbox> {
                NotificationInboxRoot(
                    onNavigateBack = { navStack.removeLastOrNull() },
                )
            }

            entry<AppGraph.NotificationBuilder> {
                NotificationBuilderRoot(
                    onNavigateBack = { navStack.removeLastOrNull() },
                )
            }
        }
    )
}
