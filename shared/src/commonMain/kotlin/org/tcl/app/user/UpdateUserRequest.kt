package org.tcl.app.user

import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserRequest(
    val firstName: String? = null,
    val lastName: String? = null,
    val phoneNumber: String? = null,
    val address: String? = null,
    val isMember: Boolean? = null,
)
