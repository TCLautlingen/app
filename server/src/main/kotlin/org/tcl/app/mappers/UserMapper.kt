package org.tcl.app.mappers

import org.tcl.app.entities.UserEntity
import org.tcl.app.models.AuthUser
import org.tcl.app.user.User

fun entityToUser(entity: UserEntity) : User = User(
    id = entity.id.value,
    email = entity.email,
    isMember = entity.isMember,
    isAdmin = entity.isAdmin,
    firstName = entity.profile.firstName,
    lastName = entity.profile.lastName
)

fun entityToAuthUser(entity: UserEntity) = AuthUser(
    entity.id.value,
    entity.email,
    entity.passwordHash,
    entity.passwordSalt
)