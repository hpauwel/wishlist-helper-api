package be.hpauwel.wishlisthelperrest.model.dto.wishlist

data class WishlistPostDTO(
    val title: String,
    val description: String,
    val isPublic: Boolean
) {
}