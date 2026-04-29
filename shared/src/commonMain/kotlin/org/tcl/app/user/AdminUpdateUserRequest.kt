package org.tcl.app.user

import kotlinx.serialization.Serializable

@Serializable
data class AdminUpdateUserRequest(
    val isMember: Boolean? = null,
    val isAdmin: Boolean? = null,
)
