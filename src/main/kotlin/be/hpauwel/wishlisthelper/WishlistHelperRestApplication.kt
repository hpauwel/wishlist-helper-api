package be.hpauwel.wishlisthelper

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class WishlistHelperRestApplication

fun main(args: Array<String>) {
    runApplication<WishlistHelperRestApplication>(*args)
}
