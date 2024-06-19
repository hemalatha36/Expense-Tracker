package com.example.myexpensetracker.ui.viewModel

import android.app.Application
import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myexpensetracker.data.local.database.AppDatabase
import com.example.myexpensetracker.data.model.Category
import com.example.myexpensetracker.data.model.PaymentMode
import com.example.myexpensetracker.data.repository.TransactionRepository
import com.example.myexpensetracker.ui.pages.FilterOption
import com.example.myexpensetracker.ui.pages.Transaction as UiTransaction
import com.example.myexpensetracker.ui.pages.TransactionType as UiTransactionType
import com.example.myexpensetracker.data.util.toRoomTransaction
import com.example.myexpensetracker.data.util.toUiTransaction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MyExpenseeApp:Application(){
    companion object{
        lateinit var context :Context
    }
    override fun onCreate() {
        super.onCreate()
        context =this
    }
}

class TransactionViewModel() : ViewModel() {

    private val database = AppDatabase.getDatabase(MyExpenseeApp.context)
    private val transactionDatabase =database.transactionDao()
    private  val repository: TransactionRepository =TransactionRepository(transactionDatabase)

    private val _filteredTransactions = MutableStateFlow<List<UiTransaction>>(emptyList())
    val filteredTransactions: StateFlow<List<UiTransaction>> = _filteredTransactions.asStateFlow()

    private val _transactions = MutableStateFlow<List<UiTransaction>>(emptyList())
    val transactions: StateFlow<List<UiTransaction>> = _transactions.asStateFlow()

    var selectedFilter = MutableStateFlow(FilterOption.Latest)
        private set

    val categories: StateFlow<List<Category>> = repository.getAllCategories().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    val paymentModes: StateFlow<List<PaymentMode>> = repository.getAllPaymentModes().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private val _selectedDate = MutableStateFlow("")
    val selectedDate: StateFlow<String> = _selectedDate

    val selectedPaymentMode = mutableStateOf<String>("")

    init {
        loadTransactions()
        applyFilter()
    }

    fun validateIncomeFields(date: String, amount: String, description: String): Boolean {
        return date.isNotBlank() && amount.isNotBlank() && description.isNotBlank()
    }

    fun validateExpenseFields(
        date: String,
        amount: String,
        description: String,
        category: String,
        paymentMode: String
    ): Boolean {
        return date.isNotBlank() && amount.isNotBlank() && description.isNotBlank() && category.isNotBlank() && paymentMode.isNotBlank()
    }

    fun addTransaction(transaction: UiTransaction) {
        viewModelScope.launch {
            repository.insertTransaction(transaction.toRoomTransaction())
            loadTransactions()  // Refresh transactions after adding one
        }
    }

    fun updateTransaction(transaction: UiTransaction) {
        viewModelScope.launch {
            repository.updateTransaction(transaction.toRoomTransaction())
            loadTransactions()  // Refresh transactions after updating one
        }
    }

    fun deleteTransaction(transaction: UiTransaction) {
        viewModelScope.launch {
            repository.deleteTransaction(transaction.toRoomTransaction())
            loadTransactions()  // Refresh transactions after deleting one
        }
    }

    fun deleteAllTransactions() {
        viewModelScope.launch {
            repository.deleteAllTransactions()
            loadTransactions()  // Refresh transactions after deleting all
        }
    }
    private fun loadTransactions() {
        viewModelScope.launch {
            repository.getAllTransactions().collect { roomTransactions ->
                val uiTransactions: List<UiTransaction> = roomTransactions.map { it.toUiTransaction() }
                _transactions.value = uiTransactions
                applyFilter()
            }
        }
    }

    fun onDateSelected(date: String) {
        viewModelScope.launch {
            _selectedDate.value = date
            applyFilter()
        }
    }

    fun updateFilter(option: FilterOption) {
        viewModelScope.launch {
            selectedFilter.value = option
            applyFilter()
        }
    }

    private fun applyFilter() {
        val date = _selectedDate.value
        val transactions = _transactions.value
        val filtered = handleFilterOption(selectedFilter.value, transactions, date)
        _filteredTransactions.value = filtered
    }
    fun addCategory(categoryName: String) {
        viewModelScope.launch {
            repository.insertCategory(Category(categoryName))
            // Optionally, refresh categories list if needed
        }
    }

    // Add payment mode
    fun addPaymentMode(paymentModeName: String) {
        viewModelScope.launch {
            repository.insertPaymentMode(PaymentMode(paymentModeName))
        }
    }

    fun updatePaymentMode(paymentMode: String) {
        selectedPaymentMode.value = paymentMode
    }
    private fun handleFilterOption(
        filterOption: FilterOption,
        transactions: List<UiTransaction>,
        date: String
    ): List<UiTransaction> {
        return when (filterOption) {
            FilterOption.Latest -> transactions.sortedByDescending { it.date }
            FilterOption.Income -> transactions.filter { it.type == UiTransactionType.Income }
            FilterOption.Expense -> transactions.filter { it.type == UiTransactionType.Expense }
            FilterOption.Date -> transactions.filter { it.date == date }
        }
    }
}
