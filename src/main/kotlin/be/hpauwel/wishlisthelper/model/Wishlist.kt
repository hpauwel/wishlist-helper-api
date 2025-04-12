package be.hpauwel.wishlisthelper.model

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
data class Wishlist(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID?,
    val title: String,
    val description: String,
    val createdAt: LocalDateTime,
    val isPublic: Boolean,
    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    val owner: User
)
