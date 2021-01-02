package com.dorjear.apay

import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.util.LinkedList

class TransactionsWithTotalTest {

    @Test
    fun testTransactionsWithTotal() {
        val queue = LinkedList<Transaction>()
        val transactionsWithTotal = TransactionsWithTotal(queue)
        transactionsWithTotal.storeTransaction(TestData.transaction03t1)
        assert(transactionsWithTotal.total == TestData.bigDecimal10)
        assert(transactionsWithTotal.getTransaction().size == 1)

        transactionsWithTotal.storeTransaction(TestData.transaction03t2)
        assert(transactionsWithTotal.total == BigDecimal(30.0))
        assert(transactionsWithTotal.getTransaction().size == 2)

        transactionsWithTotal.storeTransaction(TestData.transaction03t3)
        assert(transactionsWithTotal.total == TestData.bigDecimal100)
        assert(transactionsWithTotal.getTransaction().size == 2)
    }

}