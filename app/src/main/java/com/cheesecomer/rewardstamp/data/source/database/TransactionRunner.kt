package com.cheesecomer.rewardstamp.data.source.database

import androidx.room.RoomDatabase
import androidx.room.withTransaction

interface TransactionRunner {
    suspend fun <R> run(block: suspend () -> R): R
}

class RoomTransactionRunner(
    private val database: RoomDatabase,
) : TransactionRunner {
    override suspend fun <R> run(block: suspend () -> R): R = database.withTransaction(block)
}
