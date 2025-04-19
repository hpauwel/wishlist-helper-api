package be.hpauwel.wishlisthelper.model.dto.user

import be.hpauwel.wishlisthelper.model.dto.wishlist.WishlistGetDTO
import java.util.*

data class UserGetDTO(
    val id: UUID,
    val email: String,
    val wishlists: List<WishlistGetDTO>
)
