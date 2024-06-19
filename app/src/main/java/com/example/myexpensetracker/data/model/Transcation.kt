package com.example.myexpensetracker.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey val id: String,
    val type: TransactionType,
    val date: String,
    val amount: Double,
    val description: String,
    val isExpense: Boolean,
    val category: String,
    val paymentMode: String
)

enum class TransactionType { Income, Expense }

@Entity(tableName = "balance")
data class Balance(
    @PrimaryKey val id: Int = 0,
    val totalBalance: Double,
    val income: Double,
    val expenses: Double
)

@Entity(tableName = "categories")
data class Category(
    @PrimaryKey val name: String
)

@Entity(tableName = "payment_modes")
data class PaymentMode(
    @PrimaryKey val name: String
)