package com.miassolutions.rentatool.app

import java.time.LocalDate

fun main() {
    val now : String = LocalDate.now().toString()
    println(now)

    val date = LocalDate.parse("2029-01-01")
    println(date)
}