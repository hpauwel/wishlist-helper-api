package be.hpauwel.wishlisthelper.service

import be.hpauwel.wishlisthelper.model.Wishlist
import be.hpauwel.wishlisthelper.model.dto.wishlist.WishlistGetDTO
import be.hpauwel.wishlisthelper.repository.WishlistRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import java.util.UUID

class WishlistService(private val repository: WishlistRepository) {
    private val logger = KotlinLogging.logger {}

    fun getAllWishlists(): List<WishlistGetDTO> {
        logger.info { "Fetching all wishlists" }
        val wishlists = repository.findAll().toList()

        return mapWishlistsToWishlistsGetDTOs(wishlists)
    }

    fun getAllWishlistForUser(id: UUID): List<WishlistGetDTO> {
        logger.info { "Fetching all wishlists for user with id=$id" }
        val wishlists = repository.findAllByOwner_Id(id)

        return mapWishlistsToWishlistsGetDTOs(wishlists)
    }

    private fun mapWishlistsToWishlistsGetDTOs(wishlists: List<Wishlist>): List<WishlistGetDTO> {
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