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
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.koin.compose.getKoin
import org.tcl.app.auth.domain.AuthRepository
import org.tcl.app.booking.presentation.editor.BookingEditorRoot
import org.tcl.app.booking.presentation.list.BookingListRoot
import org.tcl.app.core.data.TokenManager
import org.tcl.app.auth.presentation.AuthRoot
import org.tcl.app.booking.presentation.court.BookingCourtRoot

@Composable
fun AppNavigation() {
    val tokenManager = getKoin().get<TokenManager>()
    val authRepository = getKoin().get<AuthRepository>()
    var loggedIn by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (!tokenManager.tokens.refreshToken.isBlank()) {
            val tokens = authRepository.refresh(tokenManager.tokens)

            if (tokens != null) {
                tokenManager.tokens = tokens
                loggedIn = true
            } else {
                loggedIn = false
            }
        }
    }

    val navStack = rememberNavBackStack(
        configuration = SavedStateConfiguration {
            serializersModule = SerializersModule {
                polymorphic(NavKey::class) {
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
                        AppGraph.BookingCourt::class,
                        AppGraph.BookingCourt.serializer()
                    )
                }
            }
        },
        if (loggedIn) AppGraph.BookingList else AppGraph.Auth,
    )

    LaunchedEffect(loggedIn) {
        if (loggedIn) {
            navStack.clear()
            navStack.add(AppGraph.BookingList)
        } else {
            navStack.clear()
            navStack.add(AppGraph.Auth)
        }
    }

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
            entry<AppGraph.Auth> {
                AuthRoot()
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
                    court = it.court,
                    startTime = it.startTime,
                    onNavigateBack = { navStack.removeLastOrNull() },
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
        },
    )
}