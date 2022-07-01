package it.polito.wa2.g07.loginservice.interceptors

import io.github.bucket4j.Bandwidth
import io.github.bucket4j.Bucket4j
import io.github.bucket4j.Refill
import org.springframework.http.HttpStatus
import org.springframework.web.servlet.HandlerInterceptor
import java.time.Duration
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


class RateLimiterInterceptor : HandlerInterceptor {
    private val limit = Bandwidth.classic(10, Refill.greedy(10, Duration.ofSeconds(1)))
    private val bucket = Bucket4j.builder()
        .addLimit(limit)
        .build()

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        if (bucket.tryConsume(1)){
            return true
        }
        response.status=HttpStatus.TOO_MANY_REQUESTS.value()
        return false
    }
}