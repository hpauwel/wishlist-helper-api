package be.hpauwel.wishlisthelperrest.repository

import be.hpauwel.wishlisthelperrest.model.User
import org.springframework.data.repository.CrudRepository
import java.util.*

interface UserRepository : CrudRepository<User, UUID> {
    fun findUserByEmail(email: String): User?
}