package org.tcl.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State

@Composable
expect fun BrowserNavigationEffect(currentDestination: State<Any?>)
