package com.example.myexpensetracker.data.repository

import com.example.myexpensetracker.data.local.dao.BalanceDao
import com.example.myexpensetracker.data.model.Balance
import kotlinx.coroutines.flow.Flow

class BalanceRepository(private val balanceDao: BalanceDao) {

    fun getBalance(): Flow<Balance> = balanceDao.getBalance()

    suspend fun insertOrUpdateBalance(balance: Balance) {
        balanceDao.insertOrUpdateBalance(balance)
    }

    suspend fun updateBalance(totalBalance: Double, income: Double, expenses: Double) {
        balanceDao.updateBalance(totalBalance, income, expenses)
    }
}