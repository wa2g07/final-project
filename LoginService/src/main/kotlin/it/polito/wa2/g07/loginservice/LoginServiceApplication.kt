package it.polito.wa2.g07.loginservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity

@SpringBootApplication
@EnableWebSecurity
class LoginServiceApplication

fun main(args: Array<String>) {
    runApplication<LoginServiceApplication>(*args)
}
