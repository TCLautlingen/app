package org.tcl.app

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.JWTVerifier
import io.ktor.server.application.ApplicationCall
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import java.util.Date
import javax.naming.AuthenticationException

object JwtConfig {
    private const val ISSUER = "tcl-app"
    private val SECRET = System.getenv("JWT_SECRET")

    private const val ACCESS_TOKEN_EXPIRES_MS = 15 * 60 * 1000L // 15 minutes

    val verifier: JWTVerifier = JWT
        .require(Algorithm.HMAC256(SECRET))
        .withIssuer(ISSUER)
        .build()

    fun generateAccessToken(userId: Int): String =
        JWT.create()
            .withIssuer(ISSUER)
            .withClaim("userId", userId)
            .withExpiresAt(Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRES_MS))
            .sign(Algorithm.HMAC256(SECRET))

    fun ApplicationCall.userId(): Int =
        principal<JWTPrincipal>()
            ?.payload
            ?.getClaim("userId")
            ?.asInt()
            ?: throw AuthenticationException("No userId in token")
}