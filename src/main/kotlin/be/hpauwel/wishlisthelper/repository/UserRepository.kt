package be.hpauwel.wishlisthelper.repository

import be.hpauwel.wishlisthelper.model.User
import org.springframework.data.repository.CrudRepository
import java.util.*

interface UserRepository : CrudRepository<User, UUID> {
    fun findUserByEmail(email: String): User?
}