package com.dorjear.apay

import java.math.BigDecimal
import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.LinkedList
import java.util.Queue

val cache: MutableMap<String, TransactionsWithTotal> = HashMap()
const val fraudCheckPeriodHours = 24L

fun main(args: Array<String>) {
    val startTime = Date().time
    val lines = Files.lines(Paths.get(args[1]))
    lines.forEach { processTransaction(it, args[0].toBigDecimal()) }
    println("Processing time is : ${Date().time - startTime}")
    lines.close()
}

fun processTransaction(transactionLine: String, fraudAmount: BigDecimal) {
    val split: List<String> = transactionLine.split(",")
    val transaction =
        Transaction(split[0], LocalDateTime.parse(split[1], DateTimeFormatter.ISO_LOCAL_DATE_TIME), split[2].toBigDecimal())
    val position = cache.getOrPut(transaction.cardNo) { TransactionsWithTotal(LinkedList()) }
    position.storeTransaction(transaction)
    if (position.total > fraudAmount) alertFraund(transaction, position.total)
}

fun alertFraund(transaction: Transaction, totalAmount: BigDecimal) {
    println(
        "!!! Fraud detected on transaction ${transaction.cardNo} " +
            "at ${transaction.transactionTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)} " +
            "with total amount $totalAmount " +
            "in past $fraudCheckPeriodHours hours"
    )
}

data class Transaction(val cardNo: String, val transactionTime: LocalDateTime, val amount: BigDecimal)

/**
 * To check the total amount in past hours period for every transaction, the total amount will be access frequently.
 * This class is to store the transactions within past period(default to 24 hours) and the total amount so that it don't need to
 * traverse the transaction list to calculate the total amount.
 */
class TransactionsWithTotal(private val transactions: Queue<Transaction>) {
    var total: BigDecimal = BigDecimal(0.0)
    fun storeTransaction(newTransaction: Transaction) {
        while (transactions.isNotEmpty()) {
            if (transactions.peek().transactionTime.plusHours(fraudCheckPeriodHours)
                    .isBefore(newTransaction.transactionTime)
            ) {
                total -= transactions.poll().amount
            } else {
                break
            }
        }
        transactions.add(newTransaction)
        total += newTransaction.amount
    }
}