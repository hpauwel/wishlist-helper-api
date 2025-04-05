package be.hpauwel.wishlisthelperrest.model.dto

import java.util.*

data class UserGetDTO(
    val id: UUID,
    val email: String
)
