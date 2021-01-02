package com.dorjear.apay

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.lang.IllegalArgumentException

class FraudDetectionTest {
    @Test
    fun testAlertFraud(){
        alertFraund(TestData.transaction01t1, TestData.bigDecimal100)
    }

    @Test
    fun testProcessTransaction(){
        processTransaction(TestData.transaction03t1, TestData.bigDecimal100)
        processTransaction(TestData.transaction03t2, TestData.bigDecimal100)
        processTransaction(TestData.transaction03t3, TestData.bigDecimal100)
    }

    @Test
    fun testTransactionFromLine(){
        val transaction = transactionFromLine("001,2020-12-31T23:59:59,10")
        assert(transaction == TestData.transaction01t1)
    }

    @Test
    fun testMain(){
        main(arrayOf("100", "src/test/resources/transactions.csv"))
    }

    @Test
    fun testMainInvalidParameter(){
        Assertions.assertThrows(IllegalArgumentException::class.java){
            main(arrayOf("aaa", "transactions.csv"))
        }
    }

    @Test
    fun testMainParameter2(){
        Assertions.assertThrows(IllegalArgumentException::class.java){
            main(arrayOf())
        }
    }
}