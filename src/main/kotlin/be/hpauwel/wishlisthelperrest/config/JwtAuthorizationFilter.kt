package be.hpauwel.wishlisthelperrest.config

import be.hpauwel.wishlisthelperrest.service.util.JwtUtil
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder

@Component
class JwtAuthorizationFilter(
    private val jwtUtil: JwtUtil,
    private val ObjectMapper: ObjectMapper,
) : OncePerRequestFilter()
{
    private val logger = KotlinLogging.logger {}

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            val token: String? = jwtUtil.resolveToken(request)
            if (token == null) {
                filterChain.doFilter(request, response)
                return
            }

            logger.debug {
                "Token: $token"
            }

            val claims = jwtUtil.resolveClaims(request)
            val email = claims.subject
            logger.debug {
                "Email: $email"
            }

            val authentication: Authentication = UsernamePasswordAuthenticationToken(email, claims, ArrayList<GrantedAuthority>())
            SecurityContextHolder.getContext().authentication = authentication
        } catch (e: Exception) {
            logger.error {
                "Error in JWT filter: ${e.message}"
            }
            response.status = HttpServletResponse.SC_UNAUTHORIZED
            response.contentType = "application/json"
            response.writer.write(ObjectMapper.writeValueAsString(mapOf("error" to e.message)))
            return
        }

        filterChain.doFilter(request, response)
    }
}