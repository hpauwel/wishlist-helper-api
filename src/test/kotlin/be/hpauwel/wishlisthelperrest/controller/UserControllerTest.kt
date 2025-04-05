package be.hpauwel.wishlisthelperrest.controller

import be.hpauwel.wishlisthelperrest.model.User
import be.hpauwel.wishlisthelperrest.model.dto.user.UserGetDTO
import be.hpauwel.wishlisthelperrest.model.dto.user.UserPostDTO
import be.hpauwel.wishlisthelperrest.service.UserService
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import java.util.*

class UserControllerTest {

    private val userService: UserService = mockk()
    private val userController = UserController(userService)

    @Test
    fun `getUsers should return list of users`() {
        val users = listOf(UserGetDTO(id = UUID.randomUUID(), email = "user1@example.com"))
        every { userService.findAll() } returns users

        val result = userController.getUsers()
        assertEquals(users, result.body)
    }

    @Test
    fun `create should return created user`() {
        val dto = UserPostDTO(email = "newuser@example.com", password = "password123")
        val user = User(id = UUID.randomUUID(), email = dto.email, password = dto.password)
        every { userService.save(dto) } returns user

        val result = userController.create(dto)
        assertEquals(HttpStatus.OK, result.statusCode)
        assertEquals(user, result.body)
    }

    @Test
    fun `create should return bad request when user already exists`() {
        val dto = UserPostDTO(email = "existinguser@example.com", password = "password123")
        every { userService.save(dto) } throws IllegalArgumentException("User with email ${dto.email} already exists")

        val result = userController.create(dto)
        assertEquals(HttpStatus.BAD_REQUEST, result.statusCode)
    }

    @Test
    fun `getUserById should return user when found`() {
        val id = UUID.randomUUID()
        val user = User(id = id, email = "user@example.com", password = "password123")
        every { userService.findUserById(id.toString()) } returns user

        val result = userController.getUserById(id)
        assertEquals(HttpStatus.OK, result.statusCode)
        assertEquals(UserGetDTO(id = id, email = user.email), result.body)
    }

    @Test
    fun `getUserById should return not found when user does not exist`() {
        val id = UUID.randomUUID()
        every { userService.findUserById(id.toString()) } returns null

        val result = userController.getUserById(id)
        assertEquals(HttpStatus.NOT_FOUND, result.statusCode)
    }
}