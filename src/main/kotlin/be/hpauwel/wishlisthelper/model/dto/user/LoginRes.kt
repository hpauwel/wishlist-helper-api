package be.hpauwel.wishlisthelper.model.dto.user

import java.time.LocalDateTime

data class LoginRes(
    val email: String,
    val token: String,
    val expiresIn: LocalDateTime,
)
