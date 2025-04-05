package be.hpauwel.wishlisthelperrest.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.util.UUID

@Entity(name = "owner")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID?,
    val email: String,
    val password: String
)
