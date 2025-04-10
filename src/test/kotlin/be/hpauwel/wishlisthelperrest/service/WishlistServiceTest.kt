package be.hpauwel.wishlisthelperrest.service

import be.hpauwel.wishlisthelperrest.model.User
import be.hpauwel.wishlisthelperrest.model.Wishlist
import be.hpauwel.wishlisthelperrest.repository.WishlistRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.*

class WishlistServiceTest {

    private val wishlistRepository: WishlistRepository = mockk()
    private val wishlistService = WishlistService(wishlistRepository)

    @Test
    fun `getAllWishlists should return list of WishlistGetDTO`() {
        val owner =
            User(id = UUID.randomUUID(), email = "owner@example.com", password = "password123", wishlists = emptyList())
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
        every { wishlistRepository.findAll() } returns wishlists

        val result = wishlistService.getAllWishlists()
        assertEquals(1, result.size)
        assertEquals(wishlists[0].id, result[0].id)
        assertEquals(wishlists[0].title, result[0].title)
        assertEquals(wishlists[0].description, result[0].description)
        assertEquals(wishlists[0].createdAt.toString(), result[0].createdAt)
        assertEquals(wishlists[0].isPublic, result[0].isPublic)
    }

    @Test
    fun `getAllWishlists should return empty list when no wishlists exist`() {
        every { wishlistRepository.findAll() } returns emptyList()

        val result = wishlistService.getAllWishlists()
        assertTrue(result.isEmpty())
    }

}