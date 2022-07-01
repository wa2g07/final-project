package it.polito.wa2.g07.lab4.utils

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import java.security.Key
import java.util.*

fun makeJWT(sub: String, iat: Date, exp: Date, zones : String, key: Key) : String{
    return Jwts.builder().setSubject(sub).signWith(key, SignatureAlgorithm.HS384).setIssuedAt(iat).setExpiration(exp).claim("zid", zones).compact()
}