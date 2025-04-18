package be.hpauwel.wishlisthelper.service

import be.hpauwel.wishlisthelper.model.User
import be.hpauwel.wishlisthelper.model.Wishlist
import be.hpauwel.wishlisthelper.repository.WishlistRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.*

class WishlistServiceTest {

    private val wishlistRepository: WishlistRepository = mockk()
    private val wishlistService = WishlistService(wishlistRepository)

    @Test
    fun `getAllWishlists should return empty list when no wishlists exist`() {
        every { wishlistRepository.findAll() } returns emptyList()

        val result = wishlistService.getAllWishlists()
        assertTrue(result.isEmpty())
    }

    @Test
    fun `getAllWishlistForUser should return list of WishlistGetDTO`() {
        val owner = User(
            id = UUID.randomUUID(),
            email = "owner@example.com",
            password = "mysecretpassword",
            wishlists = emptyList()
        )
        val wishlists = listOf(
            Wishlist(
                id = UUID.randomUUID(),
                title = "Birthday Wishlist",
                description = "Wishlist for my birthday",
                createdAt = LocalDateTime.now(),
                isPublic = true,
                owner = owner
            )
        )

        every { wishlistRepository.findAllByOwner_Id(any()) } returns wishlists
        val result = wishlistService.getAllWishlistsForUser(owner.id!!)

        assertFalse(result.isEmpty())
        assertEquals(1, result.size)
    }

    @Test
    fun `getAllWishlistsForUser should return empty list when no wishlists exist`() {
        every { wishlistRepository.findAllByOwner_Id(any()) } returns emptyList()

        val result = wishlistService.getAllWishlistsForUser(UUID.randomUUID())
        assertTrue(result.isEmpty())
    }

}