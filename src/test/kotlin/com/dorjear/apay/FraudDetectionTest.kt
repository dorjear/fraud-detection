package com.dorjear.apay

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.LinkedList

class FraudDetectionTest {
    @Test
    fun testAlertFraud() {
        val date20201231T235959 = LocalDateTime.of(2020, 12, 31, 23, 59, 59)
        alertFraund(Transaction("001", date20201231T235959, BigDecimal(10.0)), BigDecimal(100.0))
    }

    @Test
    fun testMain() {
        main(arrayOf("100", "src/test/resources/transactions.csv"))
    }

    @Test
    fun testMainInvalidParameter() {
        Assertions.assertThrows(java.nio.file.NoSuchFileException::class.java) {
            main(arrayOf("aaa", "transactions.csv"))
        }
    }

    @Test
    fun testTransactionsWithTotal() {
        val queue = LinkedList<Transaction>()
        val transactionsWithTotal = TransactionsWithTotal(queue)
        val date20201231T235959 = LocalDateTime.of(2020, 12, 31, 23, 59, 59)
        val date20210101T235958 = LocalDateTime.of(2021, 1, 1, 23, 59, 58)
        val date20210102T000000 = LocalDateTime.of(2021, 1, 2, 0, 0, 0)

        transactionsWithTotal.storeTransaction(Transaction("003", date20201231T235959, BigDecimal(10.0)))
        assert(transactionsWithTotal.total == BigDecimal(10.0))

        transactionsWithTotal.storeTransaction(Transaction("003", date20210101T235958, BigDecimal(20.0)))
        assert(transactionsWithTotal.total == BigDecimal(30.0))

        transactionsWithTotal.storeTransaction(Transaction("003", date20210102T000000, BigDecimal(80.0)))
        assert(transactionsWithTotal.total == BigDecimal(100.0))
    }

}