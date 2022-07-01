package it.polito.wa2.g07.loginservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(exclude = [SecurityAutoConfiguration::class])
class LoginServiceApplication

fun main(args: Array<String>) {
    runApplication<LoginServiceApplication>(*args)
}
