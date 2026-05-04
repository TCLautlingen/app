package org.tcl.app.core.data

import eu.anifantakis.lib.ksafe.KSafe
import eu.anifantakis.lib.ksafe.invoke
import org.tcl.app.auth.AuthTokens

class KSafeSecureStorage(ksafe: KSafe) : SecureStorage {
    override var tokens: AuthTokens by ksafe(AuthTokens())

    override fun clearAuthTokens() {
        tokens = AuthTokens()
    }
}
