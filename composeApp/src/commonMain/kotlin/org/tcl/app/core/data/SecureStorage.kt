package org.tcl.app.core.data

import eu.anifantakis.lib.ksafe.KSafe
import eu.anifantakis.lib.ksafe.invoke
import org.tcl.app.auth.AuthTokens
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class SecureStorage(ksafe: KSafe) {
    var tokens by ksafe(AuthTokens())

    fun clearAuthTokens() {
        tokens = AuthTokens()
    }
}