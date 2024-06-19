package com.example.myexpensetracker.data.repository

import com.example.myexpensetracker.data.local.dao.TransactionDao
import com.example.myexpensetracker.data.model.Category
import com.example.myexpensetracker.data.model.PaymentMode
import com.example.myexpensetracker.data.model.Transaction
import com.example.myexpensetracker.data.model.TransactionType
import kotlinx.coroutines.flow.Flow

class TransactionRepository(private val transactionDao: TransactionDao) {

    fun getAllTransactions(): Flow<List<Transaction>> = transactionDao.getAllTransactions()

    suspend fun insertTransaction(transaction: Transaction) {
        transactionDao.insertTransaction(transaction)
    }

    suspend fun updateTransaction(transaction: Transaction) {
        transactionDao.updateTransaction(transaction)
    }

    suspend fun deleteTransaction(transaction: Transaction) {
        transactionDao.deleteTransaction(transaction)
    }

    suspend fun deleteAllTransactions() {
        transactionDao.deleteAllTransactions()
    }

    // Get all categories
    fun getAllCategories(): Flow<List<Category>> {
        return transactionDao.getAllCategories()
    }

    // Insert category
    suspend fun insertCategory(category: Category) {
        transactionDao.insertCategory(category)
    }

    // Get all payment modes
    fun getAllPaymentModes(): Flow<List<PaymentMode>> {
        return transactionDao.getAllPaymentModes()
    }

    // Insert payment mode
    suspend fun insertPaymentMode(paymentMode: PaymentMode) {
        transactionDao.insertPaymentMode(paymentMode)
    }

    fun getTotalByType(type: TransactionType): Flow<Double> = transactionDao.getTotalByType(type)

    fun getTotalByCategory(category: String): Flow<Double> = transactionDao.getTotalByCategory(category)

    fun getTotalByPaymentMode(paymentMode: String): Flow<Double> = transactionDao.getTotalByPaymentMode(paymentMode)
}
