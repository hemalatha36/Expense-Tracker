package com.example.myexpensetracker.data.local.dao

import androidx.room.*
import com.example.myexpensetracker.data.model.Category
import com.example.myexpensetracker.data.model.PaymentMode
import com.example.myexpensetracker.data.model.TransactionType
import com.example.myexpensetracker.data.model.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    @Query("SELECT * FROM transactions")
    fun getAllTransactions(): Flow<List<Transaction>>

    @Insert
    suspend fun insertTransaction(transaction: Transaction)

    @Update
    suspend fun updateTransaction(transaction: Transaction)

    @Delete
    suspend fun deleteTransaction(transaction: Transaction)

    @Query("SELECT * FROM categories")
    fun getAllCategories(): Flow<List<Category>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCategory(category: Category)

    // Methods for payment modes
    @Query("SELECT * FROM payment_modes")
    fun getAllPaymentModes(): Flow<List<PaymentMode>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPaymentMode(paymentMode: PaymentMode)

    @Query("DELETE FROM transactions")
    suspend fun deleteAllTransactions()

    @Query("SELECT SUM(amount) FROM transactions WHERE type = :type")
    fun getTotalByType(type: TransactionType): Flow<Double>

    @Query("SELECT SUM(amount) FROM transactions WHERE category = :category")
    fun getTotalByCategory(category: String): Flow<Double>

    @Query("SELECT SUM(amount) FROM transactions WHERE paymentMode = :paymentMode")
    fun getTotalByPaymentMode(paymentMode: String): Flow<Double>
}
