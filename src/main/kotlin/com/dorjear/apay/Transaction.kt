package com.dorjear.apay

import java.math.BigDecimal
import java.time.LocalDateTime

data class Transaction(val cardNo: String, val transactionTime: LocalDateTime, val amount: BigDecimal)