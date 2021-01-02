package com.dorjear.apay

import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class TransactionCacheAdapterTest {
    companion object {
        @BeforeAll
        @JvmStatic
        fun init() {
            TransactionCacheAdapter.getOrInit(TestData.cardNo1).storeTransaction(TestData.transaction01t1)
            TransactionCacheAdapter.getOrInit(TestData.cardNo1).storeTransaction(TestData.transaction01t1)
            TransactionCacheAdapter.getOrInit(TestData.cardNo2)
            TransactionCacheAdapter.getOrInit(TestData.cardNo3)
        }
    }

    @Test
    fun verifyCorrectCardNo1() {
        val transactionsWithTotal = TransactionCacheAdapter.getOrInit(TestData.cardNo1)
        assert(transactionsWithTotal.getTransaction().size == 2)
        assert(transactionsWithTotal.total == TestData.bigDecimal20)
    }

    @Test
    fun verifyCorrectCardNo2() {
        val transactionsWithTotal = TransactionCacheAdapter.getOrInit(TestData.cardNo2)
        assert(transactionsWithTotal.getTransaction().size == 0)
        assert(transactionsWithTotal.total == BigDecimal(0.0))
    }
}