package com.example.myexpensetracker.data.local.dao


import androidx.room.*
import com.example.myexpensetracker.data.model.Balance
import kotlinx.coroutines.flow.Flow
interface BalanceDao {

    @Query("SELECT * FROM balance WHERE id = 0 LIMIT 1")
    fun getBalance(): Flow<Balance>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateBalance(balance: Balance)

    @Query("UPDATE balance SET totalBalance = :totalBalance, income = :income, expenses = :expenses WHERE id = 0")
    suspend fun updateBalance(totalBalance: Double, income: Double, expenses: Double)
}