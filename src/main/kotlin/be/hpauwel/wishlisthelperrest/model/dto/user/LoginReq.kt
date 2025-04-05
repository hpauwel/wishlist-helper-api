package be.hpauwel.wishlisthelperrest.model.dto.user

data class LoginReq(
    val email: String,
    val password: String
)
