package org.tcl.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import com.mmk.kmpnotifier.notification.NotifierManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.koin.compose.getKoin
import org.tcl.app.auth.domain.AuthRemoteDataSource
import org.tcl.app.auth.presentation.AuthRoot
import org.tcl.app.booking.presentation.court.BookingCourtRoot
import org.tcl.app.booking.presentation.editor.BookingEditorRoot
import org.tcl.app.booking.presentation.list.BookingListRoot
import org.tcl.app.booking.presentation.success.BookingSuccessRoot
import org.tcl.app.core.data.SecureStorage
import org.tcl.app.core.domain.util.onFailure
import org.tcl.app.core.domain.util.onSuccess
import org.tcl.app.notification.domain.NotificationRemoteDataSource
import org.tcl.app.notification.presentation.builder.NotificationBuilderRoot
import org.tcl.app.notification.presentation.inbox.NotificationInboxRoot
import org.tcl.app.onboarding.presentation.account.OnboardingAccountRoot
import org.tcl.app.onboarding.presentation.contact.OnboardingContactRoot
import org.tcl.app.onboarding.presentation.membership.OnboardingMembershipRoot
import org.tcl.app.onboarding.presentation.profile.OnboardingProfileRoot
import org.tcl.app.onboarding.presentation.rules.OnboardingRulesRoot
import org.tcl.app.onboarding.presentation.welcome.OnboardingWelcomeRoot
import org.tcl.app.user.domain.UserRemoteDataSource
import org.tcl.app.user.presentation.editor.UserEditorRoot
import org.tcl.app.user.presentation.list.UserListRoot
import org.tcl.app.user.presentation.profile.UserProfileRoot

@Composable
fun AppNavigation() {
    val secureStorage = getKoin().get<SecureStorage>()
    val authRemoteDataSource = getKoin().get<AuthRemoteDataSource>()
    val userRemoteDataSource = getKoin().get<UserRemoteDataSource>()
    val notificationRemoteDataSource = getKoin().get<NotificationRemoteDataSource>()
    var loggedIn by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        NotifierManager.addListener(object : NotifierManager.Listener {
            override fun onNewToken(token: String) {
                if (loggedIn) {
                    CoroutineScope(Dispatchers.Main).launch {
                        notificationRemoteDataSource.registerToken(
                            token = token,
                        )
                    }
                }
            }
        })
    }

    LaunchedEffect(Unit) {
        if (!secureStorage.tokens.refreshToken.isBlank()) {
            authRemoteDataSource.refresh(secureStorage.tokens.refreshToken)
                .onSuccess { authTokens ->
                    secureStorage.tokens = authTokens
                    loggedIn = true
                }
                .onFailure {
                    loggedIn = false
                }
        }
    }

    LaunchedEffect(loggedIn) {
        if (loggedIn) {
            val token = NotifierManager.getPushNotifier().getToken()
            if (token != null) {
                notificationRemoteDataSource.registerToken(
                    token = token,
                )
            }
        }
    }

    val navStack = rememberNavBackStack(
        configuration = SavedStateConfiguration {
            serializersModule = SerializersModule {
                polymorphic(NavKey::class) {
                    subclass(
                        AppGraph.OnboardingWelcome::class,
                        AppGraph.OnboardingWelcome.serializer(),
                    )
                    subclass(
                        AppGraph.Auth::class,
                        AppGraph.Auth.serializer(),
                    )
                    subclass(
                        AppGraph.BookingList::class,
                        AppGraph.BookingList.serializer(),
                    )
                    subclass(
                        AppGraph.CreateBooking::class,
                        AppGraph.CreateBooking.serializer(),
                    )
                    subclass(
                        AppGraph.BookingSuccess::class,
                        AppGraph.BookingSuccess.serializer(),
                    )
                    subclass(
                        AppGraph.BookingCourt::class,
                        AppGraph.BookingCourt.serializer()
                    )
                    subclass(
                        AppGraph.UserProfile::class,
                        AppGraph.UserProfile.serializer()
                    )
                    subclass(
                        AppGraph.UserList::class,
                        AppGraph.UserList.serializer()
                    )
                    subclass(
                        AppGraph.UserEditor::class,
                        AppGraph.UserEditor.serializer()
                    )
                    subclass(
                        AppGraph.NotificationInbox::class,
                        AppGraph.NotificationInbox.serializer()
                    )
                    subclass(
                        AppGraph.NotificationBuilder::class,
                        AppGraph.NotificationBuilder.serializer()
                    )
                }
            }
        },
        AppGraph.OnboardingWelcome,
    )

    /*
    LaunchedEffect(loggedIn) {
        if (loggedIn) {
            navStack.clear()
            navStack.add(AppGraph.BookingList)
        } else {
            navStack.clear()
            navStack.add(AppGraph.Auth)
        }
    }
     */

    NavDisplay(
        backStack = navStack,
        onBack = {
            navStack.removeLastOrNull()
        },
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
                )
            }

            entry<AppGraph.OnboardingMembership> {
                OnboardingMembershipRoot(
                    onNavigateBack = { navStack.removeLastOrNull() },
                    onNavigate = { route -> navStack.add(route) },
                )
            }

            entry<AppGraph.OnboardingProfile> {
                OnboardingProfileRoot(
                    onNavigateBack = { navStack.removeLastOrNull() },
                    onNavigate = { route -> navStack.add(route) },
                )
            }

            entry<AppGraph.OnboardingContact> {
                OnboardingContactRoot(
                    onNavigateBack = { navStack.removeLastOrNull() },
                    onNavigate = { route -> navStack.add(route) },
                )
            }

            entry<AppGraph.OnboardingRules> {
                OnboardingRulesRoot(
                    onNavigateBack = { navStack.removeLastOrNull() },
                    onNavigate = { route -> navStack.add(route) },
                )
            }

            entry<AppGraph.Auth> {
                AuthRoot(
                    onSuccess = { loggedIn = true },
                )
            }

            entry<AppGraph.BookingList> {
                BookingListRoot(
                    onNavigate = { route ->
                        navStack.add(route)
                    },
                    currentRoute = navStack.lastOrNull() as AppGraph?
                )
            }

            entry<AppGraph.CreateBooking> {
                BookingEditorRoot(
                    date = it.date,
                    courtId = it.courtId,
                    startTime = it.startTime,
                    onCourtBooked = { booking ->
                        navStack.add(
                            AppGraph.BookingSuccess(
                                booking = booking
                            )
                        )
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
                    onNavigate = { route ->
                        navStack.add(route)
                    },
                    currentRoute = navStack.lastOrNull() as AppGraph?
                )
            }

            entry<AppGraph.UserProfile> {
                UserProfileRoot(
                    onNavigate = { route ->
                        navStack.add(route)
                    },
                    onLoggedOut = { loggedIn = false },
                    currentRoute = navStack.lastOrNull() as AppGraph?
                )
            }

            entry<AppGraph.UserList> {
                UserListRoot(
                    onNavigate = { route ->
                        navStack.add(route)
                    },
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