package be.hpauwel.wishlisthelperrest.controller

import be.hpauwel.wishlisthelperrest.model.User
import be.hpauwel.wishlisthelperrest.model.dto.user.LoginReq
import be.hpauwel.wishlisthelperrest.model.dto.user.UserGetDTO
import be.hpauwel.wishlisthelperrest.model.dto.user.UserPostDTO
import be.hpauwel.wishlisthelperrest.service.UserService
import be.hpauwel.wishlisthelperrest.service.util.JwtUtil
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.*

class AuthControllerTest {

    private val userService: UserService = mockk()
    private val authenticationManager: AuthenticationManager = mockk()
    private val jwtUtil: JwtUtil = JwtUtil()
    private val passwordEncoder: PasswordEncoder = mockk()
    private val authController = AuthController(userService, authenticationManager, jwtUtil, passwordEncoder)

    @Test
    fun `getUsers should return list of users`() {
        val users = listOf(UserGetDTO(id = UUID.randomUUID(), email = "user1@example.com"))
        every { userService.findAll() } returns users

        val result = authController.getUsers()
        assertEquals(HttpStatus.OK, result.statusCode)
        assertEquals(users.map { UserGetDTO(it.id, it.email) }, result.body)
    }

    @Test
    fun `register should return created user`() {
        val dto = UserPostDTO(email = "newuser@example.com", password = "password123")
        val user = User(id = UUID.randomUUID(), email = dto.email, password = dto.password, wishlists = emptyList())
        every { userService.save(dto) } returns user
        every {
            passwordEncoder.encode(dto.password)
        } returns "password123"

        val result = authController.register(dto)
        assertEquals(HttpStatus.OK, result.statusCode)
        assertEquals(user, result.body)
    }

    @Test
    fun `register should return bad request when user already exists`() {
        val dto = UserPostDTO(email = "existinguser@example.com", password = "password123")
        every { userService.save(dto) } throws IllegalArgumentException("User with email ${dto.email} already exists")
        every {
            passwordEncoder.encode(dto.password)
        } returns "password123"

        val result = authController.register(dto)
        assertEquals(HttpStatus.BAD_REQUEST, result.statusCode)
    }

    @Test
    fun `getUserById should return user when found`() {
        val id = UUID.randomUUID()
        val user = User(id = id, email = "user@example.com", password = "password123", wishlists = emptyList())
        every { userService.findUserById(id.toString()) } returns user

        val result = authController.getUserById(id)
        assertEquals(HttpStatus.OK, result.statusCode)
        assertEquals(UserGetDTO(id = id, email = user.email), result.body)
    }

    @Test
    fun `getUserById should return not found when user does not exist`() {
        val id = UUID.randomUUID()
        every { userService.findUserById(id.toString()) } returns null

        val result = authController.getUserById(id)
        assertEquals(HttpStatus.NOT_FOUND, result.statusCode)
    }

    @Test
    fun `getUserByEmail should return user when found`() {
        val email = "user@example.com"
        val user = User(id = UUID.randomUUID(), email = email, password = "password123", wishlists = emptyList())
        every { userService.findUserByEmail(email) } returns user

        val result = authController.getUserByEmail(email)
        assertEquals(HttpStatus.OK, result.statusCode)
        assertEquals(UserGetDTO(id = user.id!!, email = user.email), result.body)
    }

    @Test
    fun `getUserByEmail should return not found when user does not exist`() {
        val email = "user@example.com"
        every { userService.findUserByEmail(email) } returns null

        val result = authController.getUserByEmail(email)
        assertEquals(HttpStatus.NOT_FOUND, result.statusCode)
    }

    @Test
    fun `login should return token when credentials are valid`() {
        val loginReq = LoginReq(email = "user@example.com", password = "password123")

        every { authenticationManager.authenticate(any<UsernamePasswordAuthenticationToken>()) } returns UsernamePasswordAuthenticationToken(
            loginReq.email,
            loginReq.password
        )

        every {
            passwordEncoder.encode(loginReq.password)
        } returns "password123"

        val result = authController.login(loginReq)
        assertEquals(HttpStatus.OK, result.statusCode)
    }

    @Test
    fun `login should return bad request when credentials are invalid`() {
        val loginReq = LoginReq(email = "user@example.com", password = "wrongpassword")

        every { authenticationManager.authenticate(any<UsernamePasswordAuthenticationToken>()) } throws BadCredentialsException(
            "Invalid credentials"
        )

        val result = authController.login(loginReq)
        assertEquals(HttpStatus.BAD_REQUEST, result.statusCode)
    }
}