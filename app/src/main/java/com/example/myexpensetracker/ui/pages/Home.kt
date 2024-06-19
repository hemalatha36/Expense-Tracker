package com.example.myexpensetracker.ui.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myexpensetracker.R
import com.example.myexpensetracker.ui.viewModel.TransactionViewModel
import com.example.myexpensetracker.ui.viewModel.BalanceViewModel



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseTrackerHome(transactionViewModel: TransactionViewModel) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text("Home") })
        },
    ) {
        BodyContent(paddingValues = it, transactionViewModel = transactionViewModel)
    }
}


@Composable
fun BodyContent(paddingValues: PaddingValues, transactionViewModel: TransactionViewModel) {
    val context = LocalContext.current
    val filteredTransactions by transactionViewModel.filteredTransactions.collectAsState()

    Column(modifier = Modifier.padding(paddingValues)) {
        BalanceCard()
        TransactionList(
            transactions = filteredTransactions,
            context = context,
            onDateSelected = { selectedDate ->
                transactionViewModel.onDateSelected(selectedDate)
            },
            onFilterSelected = { filterOption ->
                transactionViewModel.updateFilter(filterOption)
            }
        )
    }
}

//Balance Card
@Composable
fun BalanceCard(balanceViewModel: BalanceViewModel = viewModel()) {
    val isBalanceVisible by balanceViewModel.isBalanceVisible.collectAsState()
    val totalBalance by balanceViewModel.totalBalance.collectAsState()
    val income by balanceViewModel.income.collectAsState()
    val expenses by balanceViewModel.expenses.collectAsState()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .background(Color(0xFFFFF1F1))
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total Balance",
                    fontWeight = FontWeight.Bold
                )
                IconButton(onClick = { balanceViewModel.toggleBalanceVisibility() }) {
                    Icon(
                        painter = painterResource(id = if (isBalanceVisible) R.drawable.ic_visibility else R.drawable.ic_visibility_off),
                        contentDescription = "Toggle Balance Visibility"
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            if (isBalanceVisible) {
                Text(
                    text = totalBalance,
                    style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.primary)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                BalanceItem("Income", income)
                BalanceItem("Expenses", expenses)
            }
        }
    }
}





@Composable
fun BalanceItem(title: String, amount: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = title)
        Text(
            text = amount,
            fontWeight = FontWeight.Bold
        )
    }
}




