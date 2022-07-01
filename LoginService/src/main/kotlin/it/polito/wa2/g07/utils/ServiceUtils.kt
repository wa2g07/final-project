package it.polito.wa2.g07.utils

import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import java.security.Key

fun checkEmail(email: String): Boolean {
  if (!email.matches("^[A-Za-z](.*)([@])(.+)(\\.)(.+)".toRegex())) return false

  return true
}

fun checkPassword(password: String): Boolean {
  if (password.contains(" ") ||
    password.length < 8 ||
    !password.contains("[a-z]+".toRegex()) ||
    !password.contains("[A-Z]+".toRegex()) ||
    !password.contains("[0-9]+".toRegex()) ||
    !password.contains("[^a-zA-Z\\d\\s:]+".toRegex())
  ) return false

  return true
}

fun currentTime(): Date {
  return Date(
    LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()
      .toEpochMilli()
  )
}

fun makeDate(plusMinutes: Long): Date {
  return Date(
          LocalDateTime.now().plusMinutes(plusMinutes)
                  .atZone(ZoneId.systemDefault()).toInstant()
                  .toEpochMilli()
  )
}

fun makeJWT(
        sub: String, iat: Date, exp: Date, roles: String, key: Key
): String {
  return Jwts.builder().setSubject(sub)
          .signWith(key, SignatureAlgorithm.HS384)
          .setIssuedAt(iat).setExpiration(exp)
          .claim("roles", roles).compact()
}