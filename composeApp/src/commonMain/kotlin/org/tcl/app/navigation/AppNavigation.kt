package org.tcl.app.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.tcl.app.booking.presentation.editor.BookingEditorRoot
import org.tcl.app.booking.presentation.list.BookingListRoot

@Composable
fun AppNavigation() {
    val navStack = rememberNavBackStack(
        configuration = SavedStateConfiguration {
            serializersModule = SerializersModule {
                polymorphic(NavKey::class) {
                    subclass(
                        AppGraph.Login::class,
                        AppGraph.Login.serializer(),
                    )
                    subclass(
                        AppGraph.BookingsList::class,
                        AppGraph.BookingsList.serializer(),
                    )
                    subclass(
                        AppGraph.CreateBooking::class,
                        AppGraph.CreateBooking.serializer(),
                    )
                }
            }
        },
        AppGraph.BookingsList,
    )

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
            entry<AppGraph.Login> {

            }

            entry<AppGraph.BookingsList> {
                BookingListRoot(
                    onNavigate = { route ->
                        navStack.add(route)
                    },
                )
            }

            entry<AppGraph.CreateBooking> {
                BookingEditorRoot(
                    onNavigateBack = { navStack.removeLastOrNull() },
                )
            }
        },
    )
}