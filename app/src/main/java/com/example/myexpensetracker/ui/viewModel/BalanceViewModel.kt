package com.example.myexpensetracker.ui.viewModel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myexpensetracker.data.repository.BalanceRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class BalanceViewModel(private val repository: BalanceRepository) : ViewModel() {
    private val _isBalanceVisible = MutableStateFlow(true)
    val isBalanceVisible: StateFlow<Boolean> = _isBalanceVisible

    private val _totalBalance = MutableStateFlow("$0.00")
    val totalBalance: StateFlow<String> = _totalBalance

    private val _income = MutableStateFlow("$0.00")
    val income: StateFlow<String> = _income

    private val _expenses = MutableStateFlow("$0.00")
    val expenses: StateFlow<String> = _expenses

    init {
        loadBalance()
    }

    fun toggleBalanceVisibility() {
        viewModelScope.launch {
            _isBalanceVisible.value = !_isBalanceVisible.value
        }
    }

    private fun loadBalance() {
        viewModelScope.launch {
            repository.getBalance().collect { balance ->
                _totalBalance.value = "$${balance.totalBalance}"
                _income.value = "$${balance.income}"
                _expenses.value = "$${balance.expenses}"
            }
        }
    }

    fun updateBalance(totalBalance: Double, income: Double, expenses: Double) {
        viewModelScope.launch {
            repository.updateBalance(totalBalance, income, expenses)
            loadBalance() // Refresh balance after updating
        }
    }
}