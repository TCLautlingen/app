package org.tcl.app.user

import org.tcl.app.auth.AuthTokens
import org.tcl.app.auth.RegisterResult
import org.tcl.app.auth.RefreshTokenRepository
import org.tcl.app.device.DeviceRepository
import org.tcl.app.security.JwtConfig
import java.security.SecureRandom
import java.security.spec.KeySpec
import java.util.HexFormat
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

class UserService(
    private val userRepository: UserRepository,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val deviceRepository: DeviceRepository
) {
    suspend fun registerUser(
        email: String,
        password: String,
        firstName: String,
        lastName: String
    ): RegisterResult {
        val passwordSalt = generateRandomSalt()
        val passwordHash = generateHash(password, passwordSalt)

        val user = userRepository.createUser(
            email = email,
            passwordHash = passwordHash,
            passwordSalt = passwordSalt,
            firstName = firstName,
            lastName = lastName
        ) ?: return RegisterResult.EmailAlreadyExists

        val accessToken = JwtConfig.generateAccessToken(user.id)
        val refreshToken = generateSecureOpaqueToken()

        refreshTokenRepository.addRefreshToken(
            userId = user.id,
            refreshToken = refreshToken,
            expiresAt = System.currentTimeMillis() + REFRESH_TOKEN_DURATION_MS
        )

        return RegisterResult.Success(AuthTokens(accessToken, refreshToken))
    }

    suspend fun login(email: String, password: String): AuthTokens? {
        val user = userRepository.authUserByEmail(email) ?: return null
        val passwordHash = generateHash(password, user.passwordSalt)

        if (passwordHash != user.passwordHash) {
            return null
        }

        val accessToken = JwtConfig.generateAccessToken(user.id)
        val refreshToken = generateSecureOpaqueToken()

        refreshTokenRepository.addRefreshToken(
            userId = user.id,
            refreshToken = refreshToken,
            expiresAt = System.currentTimeMillis() + REFRESH_TOKEN_DURATION_MS
        )

        return AuthTokens(
            accessToken = accessToken,
            refreshToken = refreshToken
        )
    }

    suspend fun logout(deviceUniqueId: String, refreshToken: String): Boolean {
        deviceRepository.removeDevice(deviceUniqueId)
        return refreshTokenRepository.removeRefreshToken(refreshToken)

    }

    suspend fun refreshTokens(refreshToken: String): AuthTokens? {
        val storedToken = refreshTokenRepository.tokenByToken(refreshToken)
            ?: return null

        if (storedToken.expiresAt < System.currentTimeMillis()) {
            refreshTokenRepository.removeRefreshToken(refreshToken)
            return null
        }

        refreshTokenRepository.removeRefreshToken(refreshToken)

        val newAccessToken = JwtConfig.generateAccessToken(storedToken.userId)
        val newRefreshToken = generateSecureOpaqueToken()

        refreshTokenRepository.addRefreshToken(
            userId = storedToken.userId,
            refreshToken = newRefreshToken,
            expiresAt = System.currentTimeMillis() + REFRESH_TOKEN_DURATION_MS
        )

        return AuthTokens(
            accessToken = newAccessToken,
            refreshToken = newRefreshToken
        )
    }

    suspend fun getAllUsers(searchQuery: String): List<User> {
        return userRepository.allUsers(searchQuery)
    }

     suspend fun getUserById(id: Int): User? {
        return userRepository.userById(id)
     }

    suspend fun updateDevice(userId: Int, deviceUniqueId: String, notificationToken: String) {
        deviceRepository.upsertDevice(userId, deviceUniqueId, notificationToken)
    }
}

private fun ByteArray.toHexString(): String = HexFormat.of().formatHex(this)

private fun generateRandomSalt(): String {
    val random = SecureRandom()
    val salt = ByteArray(16)
    random.nextBytes(salt)

    return salt.toHexString()
}

private const val REFRESH_TOKEN_DURATION_MS = 30L * 24 * 60 * 60 * 1000
private const val ALGORITHM = "PBKDF2WithHmacSHA512"
private const val ITERATIONS = 120_000
private const val KEY_LENGTH = 256
private const val SECRET = "SomeRandomSecret"

private fun generateHash(password: String, salt: String): String {
    val combinedSalt = "$salt$SECRET".toByteArray()

    val factory: SecretKeyFactory = SecretKeyFactory.getInstance(ALGORITHM)
    val spec: KeySpec = PBEKeySpec(password.toCharArray(), combinedSalt, ITERATIONS, KEY_LENGTH)
    val key: SecretKey = factory.generateSecret(spec)
    val hash: ByteArray = key.encoded

    return hash.toHexString()
}

private fun generateSecureOpaqueToken(): String {
    val bytes = ByteArray(32)
    SecureRandom().nextBytes(bytes)
    return bytes.toHexString()
}