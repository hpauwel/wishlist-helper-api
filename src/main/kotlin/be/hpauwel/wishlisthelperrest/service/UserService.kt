package be.hpauwel.wishlisthelperrest.service

import be.hpauwel.wishlisthelperrest.model.User
import be.hpauwel.wishlisthelperrest.model.dto.UserPostDTO
import be.hpauwel.wishlisthelperrest.repository.UserRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService(private val userRepository: UserRepository) {
    private val logger = KotlinLogging.logger {}

    fun findUserByEmail(email: String): User? = userRepository.findUserByEmail(email)

    fun findAll(): List<User> = userRepository.findAll().toList()

    fun findUserById(id: String): User? = userRepository.findByIdOrNull(UUID.fromString(id))

    @Throws(IllegalArgumentException::class)
    fun save(dto: UserPostDTO): User {
        if (findUserByEmail(dto.email) == null) {
            val user = User(
                id = null,
                email = dto.email,
                password = dto.password
            )

            logger.info { "Saving user ${user.email} to DB" }
            return userRepository.save(user)
        }

        logger.warn { "User with email ${dto.email} already exists" }
        throw IllegalArgumentException("User with email ${dto.email} already exists")
    }
}
