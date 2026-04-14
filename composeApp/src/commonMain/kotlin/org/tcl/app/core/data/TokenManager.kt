package org.tcl.app.core.data

import eu.anifantakis.lib.ksafe.KSafe
import eu.anifantakis.lib.ksafe.invoke
import org.tcl.app.AuthTokens

class TokenManager(ksafe: KSafe) {
    var tokens by ksafe(AuthTokens())

    fun clear() {
        tokens = AuthTokens()
    }
}