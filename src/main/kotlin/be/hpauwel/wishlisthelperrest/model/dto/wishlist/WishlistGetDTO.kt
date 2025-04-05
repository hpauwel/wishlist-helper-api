package be.hpauwel.wishlisthelperrest.model.dto.wishlist

import java.util.*

data class WishlistGetDTO(
    val id : UUID,
    val title: String,
    val description: String,
    val createdAt: String,
    val isPublic: Boolean
)
