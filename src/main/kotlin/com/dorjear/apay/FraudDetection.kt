package com.dorjear.apay

import java.lang.IllegalArgumentException
import java.math.BigDecimal
import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

fun main(args: Array<String>) {
    val startTime = Date().time
    val executor = Executors.newFixedThreadPool(1)
    if (GlobalConfig.cleanCacheEnabled) startCacheCleaner(executor)
    val (fraudAmount: BigDecimal, fileFullPath: String) = getFraudAmountAndFileFullPathFromParam(args)
    val lines = Files.lines(Paths.get(fileFullPath))
    lines.forEach { processTransaction(it, fraudAmount) }
    println("Total processing time is: ${Date().time - startTime}")
    lines.close()
    executor.shutdown()
}

private fun getFraudAmountAndFileFullPathFromParam(args: Array<String>): Pair<BigDecimal, String> {
    try {
        return Pair(args[0].toBigDecimal(), args[1])
    } catch (e: Exception) {
        println("Please pass 2 params. First is the amount as fraud. Second is the transaction file full path")
        println("e.g. java -jar fraud-detection.jar 100 /temp/transactions.csv")
        throw IllegalArgumentException()
    }
}

private fun startCacheCleaner(executor: ExecutorService) {
    executor.submit {
        while (true) {
            val cutoffTime = TransactionTimeQueue.take()
            TransactionCacheAdapter.getCache().forEach {
                it.value.clearExpiredTransaction(cutoffTime)
            }
        }
    }
}

fun processTransaction(transactionLine: String, fraudAmount: BigDecimal) {
    try {
        val split: List<String> = transactionLine.split(",")
        val transaction = Transaction(
            split[0],
            LocalDateTime.parse(split[1], DateTimeFormatter.ISO_LOCAL_DATE_TIME),
            split[2].toBigDecimal()
        )
        val position = TransactionCacheAdapter.getOrInit(transaction.cardNo)
        position.storeTransaction(transaction)
        if (position.total > fraudAmount) alertFraund(transaction, position.total)
        if (GlobalConfig.cleanCacheEnabled) TransactionTimeQueue.put(transaction.transactionTime) // This queue is for cleaning the cache.
    } catch (e: Throwable) {
        println("!!Unexpected Error!! -- $e -- Original data: $transactionLine")
    }
}

fun alertFraund(transaction: Transaction, totalAmount: BigDecimal) {
    println(
        "!!! Fraud detected on transaction ${transaction.cardNo} " +
            "at ${transaction.transactionTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)} " +
            "with total amount $totalAmount " +
            "in past ${GlobalConfig.fraudCheckPeriodHours} hours"
    )
}
