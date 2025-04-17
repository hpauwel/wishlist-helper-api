package be.hpauwel.wishlisthelper.controller

import be.hpauwel.wishlisthelper.model.User
import be.hpauwel.wishlisthelper.model.dto.user.authentication.LoginReq
import be.hpauwel.wishlisthelper.model.dto.user.authentication.LoginRes
import be.hpauwel.wishlisthelper.model.dto.user.UserGetDTO
import be.hpauwel.wishlisthelper.model.dto.user.UserPostDTO
import be.hpauwel.wishlisthelper.service.UserService
import be.hpauwel.wishlisthelper.service.util.JwtUtil
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val service: UserService,
    private val authenticationManager: AuthenticationManager,
    private val jwtUtil: JwtUtil,
    private val passwordEncoder: PasswordEncoder
) {
    private val logger = KotlinLogging.logger {}

    @GetMapping
    fun getUsers(): ResponseEntity<List<UserGetDTO>> {
        logger.info { "Fetching all users" }

        val userDtos = service.findAll().map { UserGetDTO(it.id, it.email) }
        logger.debug { "Fetched ${userDtos.size} users" }

        return ResponseEntity.ok(userDtos)
    }

    @PostMapping("/register")
    fun register(@RequestBody dto: UserPostDTO): ResponseEntity<User> {
        try {
            logger.info { "Registering user with email: ${dto.email}" }
            val user = UserPostDTO(
                email = dto.email,
                password = passwordEncoder.encode(dto.password),
            )

            val createdUser = service.save(user)
            logger.info { "Created user with ID: ${createdUser.id}" }
            return ResponseEntity.ok(createdUser)
        } catch (e: IllegalArgumentException) {
            logger.error(e) { "Error creating user: ${e.message}" }
            return ResponseEntity.badRequest().build()
        }
    }

    @GetMapping("/{id}")
    fun getUserById(@PathVariable("id") id: UUID): ResponseEntity<UserGetDTO> {
        logger.info { "Fetching user with ID: $id" }

        val user = service.findUserById(id.toString())
        return if (user != null) {
            logger.info { "User found: $user" }
            val userDto = UserGetDTO(
                id = user.id!!,
                email = user.email
            )

            ResponseEntity.ok(userDto)
        } else {
            logger.warn { "User with ID: $id not found" }
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/email/{email}")
    fun getUserByEmail(@PathVariable("email") email: String): ResponseEntity<UserGetDTO> {
        logger.info { "Fetching user by email: $email" }
        val user = service.findUserByEmail(email)

        return if (user != null) {
            logger.info { "User found: $user" }
            val userDto = UserGetDTO(
                id = user.id!!,
                email = user.email
            )

            ResponseEntity.ok(userDto)
        } else {
            logger.warn { "User with email: $email not found" }
            ResponseEntity.notFound().build()
        }
    }

    @ResponseBody
    @PostMapping("/login")
    fun login(@RequestBody loginReq: LoginReq): ResponseEntity<LoginRes> {
        try {
            logger.info { "Logging in user with email: ${loginReq.email}" }
            val authentication = authenticationManager
                .authenticate(
                    UsernamePasswordAuthenticationToken(
                        loginReq.email,
                        loginReq.password
                    )
                )
            val email = authentication.name
            val password = loginReq.password
            val user = User(
                id = null,
                email = email,
                password = password,
                wishlists = emptyList(),
            )
            val token = jwtUtil.createToken(user)
            val expiresIn = jwtUtil.getExpiryTime(token)

            val loginRes = LoginRes(
                email = email,
                token = token,
                expiresIn = expiresIn
            )

            logger.info { "User logged in successfully: ${loginRes.email}" }
            return ResponseEntity.ok(loginRes)
        } catch (e: BadCredentialsException) {
            logger.error(e) { "Invalid credentials: ${e.message}" }
            return ResponseEntity.badRequest().build()
        } catch (e: Exception) {
            logger.error(e) { "Error during login: ${e.message}" }
            return ResponseEntity.status(500).build()
        }
    }

    @ResponseBody
    @PostMapping("/logout")
    fun logout(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ): ResponseEntity<Void> {
        logger.debug { "Logging out user" }
        SecurityContextLogoutHandler().logout(request, response, authentication)
        return ResponseEntity.ok().build()
    }
}