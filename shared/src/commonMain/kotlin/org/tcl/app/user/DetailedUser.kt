package org.tcl.app.user

import kotlinx.serialization.Serializable

@Serializable
data class DetailedUser(
    val id: Int,
    val email: String,
    val firstName: String,
    val lastName: String,
    val isMember: Boolean,
    val isAdmin: Boolean,
    val phoneNumber: String? = null,
    val address: String? = null,
)


fun DetailedUser.toUser(): User {
    return User(
        id = id,
        email = email,
        firstName = firstName,
        lastName = lastName,
    )
}