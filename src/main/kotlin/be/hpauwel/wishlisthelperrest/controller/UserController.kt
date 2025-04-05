package be.hpauwel.wishlisthelperrest.controller

import be.hpauwel.wishlisthelperrest.model.User
import be.hpauwel.wishlisthelperrest.model.dto.UserGetDTO
import be.hpauwel.wishlisthelperrest.model.dto.UserPostDTO
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
    fun getUsers() = service.findAll()

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
}