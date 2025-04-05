package be.hpauwel.wishlisthelperrest.service

import be.hpauwel.wishlisthelperrest.model.dto.wishlist.WishlistGetDTO
import be.hpauwel.wishlisthelperrest.repository.WishlistRepository
import io.github.oshai.kotlinlogging.KotlinLogging

class WishlistService(private val repository: WishlistRepository) {
    private val logger = KotlinLogging.logger {}

    fun getAllWishlists(): List<WishlistGetDTO> {
        logger.info { "Fetching all wishlists" }
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