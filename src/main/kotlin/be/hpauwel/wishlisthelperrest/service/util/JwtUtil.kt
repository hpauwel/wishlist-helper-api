package be.hpauwel.wishlisthelperrest.service.util

import be.hpauwel.wishlisthelperrest.model.User
import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwtParser
import io.jsonwebtoken.Jwts
import jakarta.servlet.http.HttpServletRequest
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.jvm.Throws

@Component
class JwtUtil(
    private val secretKey: String = "test",
    val expirationTime: Long = 1000 * 60 * 60,
    val jwtParser: JwtParser = Jwts.parser().setSigningKey(secretKey),
    private val tokenHeader: String = "Authorization",
    private val tokenPrefix: String = "Bearer ",
) {
    fun createToken(user: User): String {
        val claims = Jwts.claims().setSubject(user.email)
        val tokenCreationTime = Date()
        val tokenValidity = Date(tokenCreationTime.time + TimeUnit.MINUTES.toMillis(expirationTime))

        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(tokenCreationTime)
            .setExpiration(tokenValidity)
            .signWith(io.jsonwebtoken.SignatureAlgorithm.HS256, secretKey)
            .compact()
    }

    private fun parseJwtClaims(token: String): Claims {
        return jwtParser.parseClaimsJws(token).body
    }

    fun resolveToken(request: HttpServletRequest): String? {
        val authHeader = request.getHeader(tokenHeader)
        return if (authHeader != null && authHeader.startsWith(tokenPrefix)) {
            authHeader.substring(tokenPrefix.length)
        } else null
    }

    @Throws(IllegalArgumentException::class)
    fun resolveClaims(request: HttpServletRequest): Claims {
        try {
            val token = resolveToken(request)
            return if (token != null) {
                parseJwtClaims(token)
            } else throw IllegalArgumentException("Token not found")
        } catch (e: Exception) {
            throw IllegalArgumentException("Invalid token", e)
        }
    }

    fun getExpiryTime(token: String): LocalDateTime {
        val claims = parseJwtClaims(token)
        val expirationDate = claims.expiration
        return LocalDateTime.ofInstant(expirationDate.toInstant(), java.time.ZoneId.systemDefault())
    }

    @Throws(IllegalArgumentException::class)
    fun validateClaims(claims: Claims): Boolean {
        try {
            return claims.expiration.after(Date())
        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException("Invalid token", e)
        }
    }
}