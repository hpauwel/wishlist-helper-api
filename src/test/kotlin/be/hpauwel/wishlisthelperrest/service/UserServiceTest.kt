package be.hpauwel.wishlisthelperrest.service

import be.hpauwel.wishlisthelperrest.model.User
import be.hpauwel.wishlisthelperrest.model.dto.UserPostDTO
import be.hpauwel.wishlisthelperrest.repository.UserRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

class UserServiceTest {

    private val userRepository: UserRepository = mockk()
    private val userService = UserService(userRepository)

    @Test
    fun `findUserByEmail should return user when found`() {
        val email = "user@example.com"
        val user = User(id = UUID.randomUUID(), email = email, password = "password123")
        every { userRepository.findUserByEmail(email) } returns user

        val result = userService.findUserByEmail(email)
        assertNotNull(result)
        assertEquals(email, result?.email)
    }

    @Test
    fun `findUserByEmail should return null when not found`() {
        val email = "user@example.com"
        every { userRepository.findUserByEmail(email) } returns null

        val result = userService.findUserByEmail(email)
        assertNull(result)
    }

    @Test
    fun `save should throw exception when user already exists`() {
        val dto = UserPostDTO(email = "user@example.com", password = "password123")
        every { userRepository.findUserByEmail(dto.email) } returns User(id = UUID.randomUUID(), email = dto.email, password = dto.password)

        assertThrows<IllegalArgumentException> { userService.save(dto) }
    }

    @Test
    fun `save should save user when user does not exist`() {
        val dto = UserPostDTO(email = "newuser@example.com", password = "password123")
        every { userRepository.findUserByEmail(dto.email) } returns null
        every { userRepository.save(any()) } returns User(id = UUID.randomUUID(), email = dto.email, password = dto.password)

        val result = userService.save(dto)
        assertNotNull(result)
        assertEquals(dto.email, result.email)
        verify { userRepository.save(any()) }
    }
}