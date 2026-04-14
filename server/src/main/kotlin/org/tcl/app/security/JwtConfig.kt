package org.tcl.app.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import com.auth0.jwt.interfaces.JWTVerifier
import java.util.Date

object JwtConfig {
    private const val ISSUER = "tcl-app"
    private const val ACCESS_SECRET = "access-secret"
    private const val REFRESH_SECRET = "refresh-secret"


    val verifier: JWTVerifier = JWT
        .require(Algorithm.HMAC256(ACCESS_SECRET))
        .withIssuer(ISSUER)
        .build()

    fun generateAccessToken(userId: String): String =
        JWT.create()
            .withIssuer(ISSUER)
            .withClaim("userId", userId)
            .withExpiresAt(Date(System.currentTimeMillis() + 15 * 60 * 1000))
            .sign(Algorithm.HMAC256(ACCESS_SECRET))

    fun generateRefreshToken(userId: String): String =
        JWT.create()
            .withIssuer(ISSUER)
            .withClaim("userId", userId)
            .withExpiresAt(Date(System.currentTimeMillis() + 7L * 24 * 60 * 60 * 1000))
            .sign(Algorithm.HMAC256(REFRESH_SECRET))

    fun verifyRefreshToken(token: String): DecodedJWT? =
        JWT
            .require(Algorithm.HMAC256(REFRESH_SECRET))
            .withIssuer(ISSUER)
            .build()
            .verify(token)
}