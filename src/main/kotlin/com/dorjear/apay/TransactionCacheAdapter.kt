package com.dorjear.apay

import java.util.LinkedList

/**
 * This class is to access the the TransactionsWithTotal object from account number. At this time being the objects are stored in a Map in JVM
 * Considering if the input data is too big to store in JVM, external storage e.g. cache, nosql or file may be adopted.
 */
object TransactionCacheAdapter {
    private val cache: MutableMap<String, TransactionsWithTotal> = HashMap()

    fun getOrInit(cardNo: String) : TransactionsWithTotal {
        return cache.getOrPut(cardNo){ TransactionsWithTotal(LinkedList()) }
    }
}

