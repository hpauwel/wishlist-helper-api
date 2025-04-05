package be.hpauwel.wishlisthelperrest.model.dto.user

import java.util.*

data class UserGetDTO(
    val id: UUID,
    val email: String
)
