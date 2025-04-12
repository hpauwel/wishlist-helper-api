package be.hpauwel.wishlisthelper.repository

import be.hpauwel.wishlisthelper.model.Wishlist
import org.springframework.data.repository.CrudRepository
import java.util.UUID

interface WishlistRepository : CrudRepository<Wishlist, UUID> {
    fun findAllByOwner_Id(id: UUID): List<Wishlist>
    fun findAllByOwner_IdAndIsPublic(id: UUID, isPublic: Boolean): List<Wishlist>
}