package it.polito.wa2.g07.utils

import kotlin.random.Random


const val STRING_LENGTH = 6

fun generateActivationCode(): String {
  val charPool: List<Char> = ('0'..'9').toList()
  return (1..STRING_LENGTH)
    .map { Random.nextInt(0, charPool.size) }
    .map(charPool::get)
    .joinToString("")
}