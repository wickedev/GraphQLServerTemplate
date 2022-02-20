package io.github.template.values

data class AuthResponseWithID(
    val userId: String,
    val accessToken: String,
    val tokenType: String = "Bearer",
    val expiresIn: Long,
    val scope: String,
    val refreshToken: String,
    val refreshExpiresIn: Long,
)
