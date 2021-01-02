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
        processTransaction(TestData.transaction03t1Line, TestData.bigDecimal100)
        processTransaction(TestData.transaction03t2Line, TestData.bigDecimal100)
        processTransaction(TestData.transaction03t3Line, TestData.bigDecimal100)
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