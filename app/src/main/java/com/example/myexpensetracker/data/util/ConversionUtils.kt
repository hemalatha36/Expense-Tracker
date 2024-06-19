// In com.example.myexpensetracker.data.util.ConversionUtils.kt
package com.example.myexpensetracker.data.util

import com.example.myexpensetracker.data.model.Transaction as RoomTransaction
import com.example.myexpensetracker.data.model.TransactionType as RoomTransactionType
import com.example.myexpensetracker.ui.pages.Transaction as UiTransaction
import com.example.myexpensetracker.ui.pages.TransactionType as UiTransactionType

fun UiTransaction.toRoomTransaction(): RoomTransaction {
    return RoomTransaction(
        id = this.id,
        type = this.type.toRoomTransactionType(),
        date = this.date,
        amount = this.amount,
        description = this.description,
        isExpense = this.isExpense,
        category = this.category,
        paymentMode = this.paymentMode
    )
}

fun RoomTransaction.toUiTransaction(): UiTransaction {
    return UiTransaction(
        id = this.id,
        type = this.type.toUiTransactionType(),
        date = this.date,
        amount = this.amount,
        description = this.description,
        isExpense = this.isExpense,
        category = this.category,
        paymentMode = this.paymentMode
    )
}

fun UiTransactionType.toRoomTransactionType(): RoomTransactionType {
    return when (this) {
        UiTransactionType.Income -> RoomTransactionType.Income
        UiTransactionType.Expense -> RoomTransactionType.Expense
    }
}

fun RoomTransactionType.toUiTransactionType(): UiTransactionType {
    return when (this) {
        RoomTransactionType.Income -> UiTransactionType.Income
        RoomTransactionType.Expense -> UiTransactionType.Expense
    }
}
