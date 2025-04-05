package be.hpauwel.wishlisthelperrest.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import java.time.LocalDateTime
import java.util.*

@Entity
data class Wishlist(
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.UUID)
    val id: UUID?,
    val title: String,
    val description: String,
    val createdAt: LocalDateTime,
    val isPublic: Boolean
)
