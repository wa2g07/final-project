package it.polito.wa2.g07.loginservice.security

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtAuthenticationTokenFilter : OncePerRequestFilter() {

  private val logger: Logger = LoggerFactory.getLogger("JwtAuthenticationTokenFilter")

  @Value("\${header.authorization}")
  lateinit var nameAuthHeader: String

  @Value("\${header.authorization.prefix}")
  lateinit var nameAuthHeaderPrefix : String

  @Autowired
  private val jwtUtils: JwtUtils? = null

  @Throws(ServletException::class, IOException::class)
  override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
    if(request.requestURI.contains("/admin/") || request.requestURI.contains("/password")) {
      try {
        val jwt: String? = parseJwt(request)
        if (jwt != null && jwtUtils!!.validateJwt(jwt)) {
          val userDetails = jwtUtils.getDetailsJwt(jwt)
          val role = jwtUtils.getJwtRoles(jwt)
          val authentication = UsernamePasswordAuthenticationToken(
            userDetails, null, getAuthorities(role)
          )

          authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
          SecurityContextHolder.getContext().authentication = authentication
        }
      } catch (e: Exception) {
        logger.error("Cannot set user authentication: {}", e)
      }
    }
    filterChain.doFilter(request, response)
  }

  private fun parseJwt(request: HttpServletRequest): String? {

    val headerAuth = request.getHeader(nameAuthHeader)
    return if (headerAuth.isNotEmpty() && headerAuth.startsWith(nameAuthHeaderPrefix)) {
      headerAuth.substring(7, headerAuth.length)
    } else null
  }
}