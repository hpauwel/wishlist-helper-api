package be.hpauwel.wishlisthelperrest.controller

import be.hpauwel.wishlisthelperrest.model.User
import be.hpauwel.wishlisthelperrest.model.dto.user.UserGetDTO
import be.hpauwel.wishlisthelperrest.model.dto.user.UserPostDTO
import be.hpauwel.wishlisthelperrest.service.UserService
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/users")
class UserController(private val service: UserService) {
    private val logger = KotlinLogging.logger {}

    @GetMapping
    fun getUsers(): ResponseEntity<List<UserGetDTO>> {
        logger.info { "Fetching all users" }
        val users = service.findAll()
        val userDtos = users.map { UserGetDTO(it.id, it.email) }
        logger.debug { "Fetched ${userDtos.size} users" }

        return ResponseEntity.ok(userDtos)
    }

    @PostMapping
    fun create(@RequestBody dto: UserPostDTO): ResponseEntity<User> {
        try {
            val createdUser = service.save(dto)
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
}