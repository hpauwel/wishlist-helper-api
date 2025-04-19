package be.hpauwel.wishlisthelper.model.dto.wishlist

import java.time.LocalDateTime
import java.util.*

data class WishlistGetDTO(
    val id : UUID,
    val title: String,
    val description: String,
    val createdAt: LocalDateTime,
    val isPublic: Boolean
)
