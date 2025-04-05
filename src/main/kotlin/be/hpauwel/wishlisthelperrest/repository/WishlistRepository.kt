package be.hpauwel.wishlisthelperrest.repository

import be.hpauwel.wishlisthelperrest.model.Wishlist
import org.springframework.data.repository.CrudRepository
import java.util.UUID

interface WishlistRepository : CrudRepository<Wishlist, UUID> {

}