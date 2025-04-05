package be.hpauwel.wishlisthelperrest.model.dto.user

data class LoginRes(
    val email: String,
    val token: String,
    val expiresIn: Long,
)
