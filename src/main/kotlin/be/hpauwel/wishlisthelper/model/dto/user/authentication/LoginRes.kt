package be.hpauwel.wishlisthelper.model.dto.user.authentication

import java.time.LocalDateTime

data class LoginRes(
    val email: String,
    val token: String,
    val expiresIn: LocalDateTime,
)