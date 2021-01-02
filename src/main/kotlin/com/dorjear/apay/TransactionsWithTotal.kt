package com.dorjear.apay

import org.jetbrains.annotations.TestOnly
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.Queue

/**
 * To check the total amount in past hours period for every transaction, the total amount will be access frequently.
 * This class is to store the transactions within past period(default to 24 hours) and the total amount so that it don't need to
 * traverse the transaction list to calculate the total amount.
 */
class TransactionsWithTotal(private val transactions: Queue<Transaction>) {
    var total: BigDecimal = BigDecimal(0.0)
    fun storeTransaction(newTransaction: Transaction) {
        clearExpiredTransaction(newTransaction.transactionTime)
        transactions.add(newTransaction)
        total += newTransaction.amount
    }

    fun clearExpiredTransaction(cutOffTime: LocalDateTime){
        while (transactions.isNotEmpty()) {
            if (transactions.peek().transactionTime.plusHours(GlobalConfig.fraudCheckPeriodHours).isBefore(cutOffTime)) {
                total -= transactions.poll().amount
            } else {
                break
            }
        }
    }

    @TestOnly
    fun getTransaction(): Queue<Transaction> {
        return transactions
    }
}