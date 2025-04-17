package be.hpauwel.wishlisthelper.model.dto.user.authentication

data class LoginReq(
    val email: String,
    val password: String
)