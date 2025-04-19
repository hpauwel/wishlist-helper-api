package be.hpauwel.wishlisthelper.service

import be.hpauwel.wishlisthelper.model.Wishlist
import be.hpauwel.wishlisthelper.model.dto.wishlist.WishlistGetDTO
import be.hpauwel.wishlisthelper.repository.WishlistRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service
import java.util.*

@Service
class WishlistService(private val repository: WishlistRepository) {
    private val logger = KotlinLogging.logger {}

    fun getAllWishlists(): List<WishlistGetDTO> {
        logger.info { "Fetching all wishlists" }
        val wishlists = repository.findAll().toList()

        return mapWishlistsToWishlistsGetDTOs(wishlists)
    }

    fun getAllWishlistsForUser(id: UUID): List<WishlistGetDTO> {
        logger.info { "Fetching all wishlists for user id=$id" }
        val wishlists = repository.findAllByOwner_Id(id)

        return mapWishlistsToWishlistsGetDTOs(wishlists)
    }

    fun getAllPublicWishlistsForUser(id: UUID): List<WishlistGetDTO> {
        logger.info { "Fetching all public wishlists for user id=$id" }
        val wishlists = repository.findAllByOwner_IdAndIsPublic(id, true)

        if (wishlists.isEmpty()) {
            logger.debug { "No public wishlists found for user id=$id" }
            return emptyList()
        }

        return mapWishlistsToWishlistsGetDTOs(wishlists)
    }

    private fun mapWishlistsToWishlistsGetDTOs(wishlists: List<Wishlist>): List<WishlistGetDTO> {
        return wishlists.map { wishlist ->
            WishlistGetDTO(
                id = wishlist.id!!,
                title = wishlist.title,
                description = wishlist.description,
                createdAt = wishlist.createdAt,
                isPublic = wishlist.isPublic
            )
        }
    }
}