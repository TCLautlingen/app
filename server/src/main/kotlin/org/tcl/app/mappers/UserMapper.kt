package org.tcl.app.mappers

import org.tcl.app.db.entities.UserEntity
import org.tcl.app.models.AuthUser
import org.tcl.app.user.DetailedUser
import org.tcl.app.user.User

fun entityToUser(entity: UserEntity) : User = User(
    id = entity.id.value,
    email = entity.email,
    firstName = entity.profile?.firstName ?: "",
    lastName = entity.profile?.lastName ?: ""
)

fun entityToDetailedUser(entity: UserEntity) : DetailedUser = DetailedUser(
    id = entity.id.value,
    email = entity.email,
    firstName = entity.profile?.firstName ?: "",
    lastName = entity.profile?.lastName ?: "",
    isMember = entity.isMember,
    isAdmin = entity.isAdmin,
    phoneNumber = entity.profile?.phoneNumber,
    address = entity.profile?.address,
)

fun entityToAuthUser(entity: UserEntity) = AuthUser(
    id = entity.id.value,
    email = entity.email,
    passwordHash = entity.passwordHash,
    passwordSalt = entity.passwordSalt,
    isAdmin = entity.isAdmin,
)