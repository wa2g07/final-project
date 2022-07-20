package it.polito.wa2.g07.loginservice.security

import io.jsonwebtoken.io.IOException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class AuthEntryPointJwt : AuthenticationEntryPoint {
  private val logger: Logger = LoggerFactory.getLogger("AuthEntryPointJwt")

  @Throws(IOException::class, ServletException::class)
  override fun commence(
    request: HttpServletRequest,
    response: HttpServletResponse,
    authException: org.springframework.security.core.AuthenticationException
  ) {
    logger.error("Unauthorized error: {}", authException.message)
    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error: Unauthorized")
  }

}