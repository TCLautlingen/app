package org.tcl.app.core.data

import eu.anifantakis.lib.ksafe.KSafe
import eu.anifantakis.lib.ksafe.invoke
import org.tcl.app.AuthTokens
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class SecureStorage(ksafe: KSafe) {
    var tokens by ksafe(AuthTokens())

    var deviceUniqueId: String by ksafe("")
        private set

    init {
        if (deviceUniqueId.isBlank()) {
            deviceUniqueId = Uuid.random().toString()
        }
    }

    fun clearAuthTokens() {
        tokens = AuthTokens()
    }
}