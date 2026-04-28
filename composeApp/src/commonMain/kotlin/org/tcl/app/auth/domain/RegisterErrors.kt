package org.tcl.app.auth.domain

import org.tcl.app.auth.RegisterError
import org.tcl.app.core.domain.util.Error

data class RegisterErrors(
    val errors: List<RegisterError>,
) : Error