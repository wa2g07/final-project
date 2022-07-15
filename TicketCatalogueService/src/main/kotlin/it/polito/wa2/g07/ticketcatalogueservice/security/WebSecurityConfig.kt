package it.polito.wa2.g07.ticketcatalogueservice.security

import org.springframework.context.annotation.Bean
import org.springframework.http.HttpStatus
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository
import reactor.core.publisher.Mono


@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
class WebSecurityConfig {

    @Bean
    fun createAuthenticationFilter() : JwtTokenAuthenticationFilter?{
        return JwtTokenAuthenticationFilter()
    }

   @Bean
   fun securityWebFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain{
     return http
            .exceptionHandling()
            .authenticationEntryPoint { swe, e -> Mono.fromRunnable { swe.response.statusCode = HttpStatus.UNAUTHORIZED } }.accessDeniedHandler { swe, e -> Mono.fromRunnable { swe.response.statusCode = HttpStatus.FORBIDDEN } }.and()
            .csrf().disable()
            .formLogin().disable()
            .httpBasic().disable()
             .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
             .authorizeExchange()
            .pathMatchers("/shop/**","/orders/**").hasAnyRole("CUSTOMER", "ADMIN")
            .pathMatchers("/admin/**").hasRole("ADMIN")
            .pathMatchers("/tickets/**").permitAll()
            .anyExchange().authenticated()
             .and()
             .addFilterAt(createAuthenticationFilter(), SecurityWebFiltersOrder.HTTP_BASIC)
             .build()
   }
}
  @Bean
  fun passwordEncoder(): PasswordEncoder {
    return BCryptPasswordEncoder()
  }


