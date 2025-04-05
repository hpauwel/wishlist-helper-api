package be.hpauwel.wishlisthelperrest.service

import be.hpauwel.wishlisthelperrest.model.dto.wishlist.WishlistGetDTO
import be.hpauwel.wishlisthelperrest.repository.WishlistRepository

class WishlistService(private val repository: WishlistRepository) {
    fun getAllWishlists(): List<WishlistGetDTO> {
        val wishlists = repository.findAll().toList()

        return wishlists.map { wishlist ->
            WishlistGetDTO(
                id = wishlist.id!!,
                title = wishlist.title,
                description = wishlist.description,
                createdAt = wishlist.createdAt.toString(),
                isPublic = wishlist.isPublic
            )
        }
    }
}