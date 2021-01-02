package com.dorjear.apay

import reactor.core.publisher.Flux
import reactor.core.scheduler.Schedulers
import java.lang.IllegalArgumentException
import java.math.BigDecimal
import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date

fun main(args: Array<String>) {
    val startTime = Date().time
    val (fraudAmount: BigDecimal, fileFullPath: String) = getFraudAmountAndFileFullPathFromParam(args)

    val lines = Files.lines(Paths.get(fileFullPath))
    Flux.fromStream(lines)
        .map { transactionFromLine(it) }
        .groupBy { m -> m.cardNo }
        .flatMap { groupFlux ->
            groupFlux.publishOn(Schedulers.boundedElastic())
        }
        .map {
            processTransaction(it, fraudAmount)
        }
        .onErrorContinue{ e, transaction ->
            println("!!Unexpected Error!! -- $e -- Original data: $transaction")
        }
        .doOnComplete {
            println("Total processing time is: ${Date().time - startTime}")
            lines.close()
        }
        .subscribe{}

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

fun transactionFromLine(transactionLine: String): Transaction {
    val split: List<String> = transactionLine.split(",")
    return Transaction(split[0], LocalDateTime.parse(split[1], DateTimeFormatter.ISO_LOCAL_DATE_TIME), split[2].toBigDecimal())
}

fun processTransaction(transaction: Transaction, fraudAmount: BigDecimal) {
    val position = TransactionCacheAdapter.getOrInit(transaction.cardNo)
    position.storeTransaction(transaction)
    if (position.total > fraudAmount) alertFraund(transaction, position.total)
}

fun alertFraund(transaction: Transaction, totalAmount: BigDecimal){
    println("!!! Fraud detected on transaction ${transaction.cardNo} " +
        "at ${transaction.transactionTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)} " +
        "with total amount $totalAmount " +
        "in past ${GlobalConfig.fraudCheckPeriodHours} hours")
}
