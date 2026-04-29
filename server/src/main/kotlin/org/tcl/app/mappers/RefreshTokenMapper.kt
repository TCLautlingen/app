package org.tcl.app.mappers

import org.tcl.app.db.entities.RefreshTokenEntity
import org.tcl.app.models.RefreshToken

fun entityToRefreshToken(entity: RefreshTokenEntity) = RefreshToken(
    userId = entity.user.id.value,
    token = entity.token,
    expiresAt = entity.expiresAt
)