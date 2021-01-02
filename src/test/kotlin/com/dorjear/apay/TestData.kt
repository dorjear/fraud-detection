package com.dorjear.apay

import java.math.BigDecimal
import java.time.LocalDateTime

object TestData {
    const val cardNo1 = "001"
    const val cardNo2 = "002"
    const val cardNo3 = "003"
    val bigDecimal100 = BigDecimal(100.0)
    val bigDecimal10 = BigDecimal(10.0)
    val bigDecimal20 = BigDecimal(20.0)
    val bigDecimal80 = BigDecimal(80.0)
    val date20201231T235959 = LocalDateTime.of(2020, 12, 31, 23, 59, 59)
    val date20210101T235958 = LocalDateTime.of(2021, 1, 1, 23, 59, 58)
    val date20210102T000000 = LocalDateTime.of(2021, 1, 2, 0, 0, 0)
    val transaction01t1 = Transaction(cardNo1, date20201231T235959, bigDecimal10)
    val transaction03t1 = Transaction(cardNo3, date20201231T235959, bigDecimal10)
    val transaction03t2 = Transaction(cardNo3, date20210101T235958, bigDecimal20)
    val transaction03t3 = Transaction(cardNo3, date20210102T000000, bigDecimal80)
}