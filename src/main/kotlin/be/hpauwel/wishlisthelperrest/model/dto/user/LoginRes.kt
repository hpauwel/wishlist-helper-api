package be.hpauwel.wishlisthelperrest.model.dto.user

import java.time.LocalDateTime

data class LoginRes(
    val email: String,
    val token: String,
    val expiresIn: LocalDateTime,
)
