package be.hpauwel.wishlisthelper.service

import be.hpauwel.wishlisthelper.model.User
import be.hpauwel.wishlisthelper.model.dto.user.UserGetDTO
import be.hpauwel.wishlisthelper.model.dto.user.UserPostDTO
import be.hpauwel.wishlisthelper.model.dto.wishlist.WishlistGetDTO
import be.hpauwel.wishlisthelper.repository.UserRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService(private val userRepository: UserRepository) : UserDetailsService {
    private val logger = KotlinLogging.logger {}

    fun findUserByEmail(email: String): UserGetDTO? {
        val user = userRepository.findUserByEmail(email)
            ?: return null
        logger.info { "User with email $email found" }
        logger.debug { "User details: $user" }

        val wishlists = user.wishlists.map {
            WishlistGetDTO(
                id = it.id!!,
                title = it.title,
                description = it.description,
                createdAt = it.createdAt,
                isPublic = it.isPublic,
            )
        }

        return UserGetDTO(user.id!!, user.email, wishlists)
    }

    @Throws(IllegalArgumentException::class)
    fun findUserById(id: String): User? {
        val userId = UUID.fromString(id)
        return userRepository.findByIdOrNull(userId)
            ?: throw IllegalArgumentException("User with ID $id not found")
    }

    @Throws(IllegalArgumentException::class)
    fun save(dto: UserPostDTO): User {
        val user = userRepository.findUserByEmail(dto.email)

        if (user == null) {
            val newUser = User(
                id = null,
                email = dto.email,
                password = dto.password,
                wishlists = emptyList()
            )
            logger.info { "Saving new user: $newUser" }
            return userRepository.save(newUser)
        }

        logger.warn { "User with email ${dto.email} already exists" }
        throw IllegalArgumentException("User with email ${dto.email} already exists")
    }

    override fun loadUserByUsername(username: String): UserDetails? {
        val user = userRepository.findUserByEmail(username)
            ?: throw IllegalArgumentException("User with email $username not found")
        logger.info { "Loading user by username: $username" }
        logger.debug { "User details: $user" }
        val authorities = AuthorityUtils.createAuthorityList("ROLE_USER")

        return org.springframework.security.core.userdetails.User(
            user.email,
            user.password,
            authorities
        )
    }
}
