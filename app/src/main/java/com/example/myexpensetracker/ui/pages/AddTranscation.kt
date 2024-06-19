package com.example.myexpensetracker.ui.pages

import android.app.DatePickerDialog
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.myexpensetracker.R
import com.example.myexpensetracker.ui.pages.Transaction
import com.example.myexpensetracker.ui.viewModel.TransactionViewModel
import java.text.SimpleDateFormat
import java.util.*
import com.example.myexpensetracker.ui.pages.Transaction as UiTransaction

enum class FilterOption(val title: String) {
    Latest("Latest"),
    Income("Income"),
    Expense("Expense"),
    Date("Date")
}



enum class TransactionType { Income, Expense }

data class Transaction(
    val id: String,
    val type: TransactionType,
    val date: String,
    val amount: Double,
    val description: String,
    val isExpense: Boolean,
    val category: String,
    val paymentMode: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionScreen(navController: NavController) {
    // Navigate to AddIncome screen
    val handleAddIncome = {
        navController.navigate(AllScreens.AddIncome.route)
    }

    // Navigate to AddExpense screen
    val handleAddExpense = {
        navController.navigate(AllScreens.AddExpense.route)
    }

    Column(modifier = Modifier.padding(16.dp)) {
        // Top App Bar
        TopAppBar(
            title = { Text("Add Transactions") },
            navigationIcon = {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            }
        )
        Spacer(modifier = Modifier.height(16.dp))

        // AddTransactionCards for adding income and expense
        AddTransactionCards(onAddIncome = handleAddIncome, onAddExpense = handleAddExpense)
        Spacer(modifier = Modifier.height(16.dp))

        // TransactionList to display transactions


    }
}


//TransactionList
@Composable
fun TransactionList(
    transactions: List<Transaction>,
    context: Context,
    onDateSelected: (String) -> Unit,
    onFilterSelected: (FilterOption) -> Unit,
    transactionViewModel: TransactionViewModel = viewModel()
) {
    var showAll by remember { mutableStateOf(false) }
    var showFilterPopup by remember { mutableStateOf(false) }
    val selectedFilter by transactionViewModel.selectedFilter.collectAsState()
    val selectedDate by transactionViewModel.selectedDate.collectAsState()
    val filteredTransactions by transactionViewModel.filteredTransactions.collectAsState()

    val transactionsToShow = if (showAll) filteredTransactions else filteredTransactions.takeLast(6)

    Column {
        // Header Row with 'Transactions' and 'See All'
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { showFilterPopup = true }) {
                Icon(
                    painter = painterResource(id =R.drawable.filter_list),
                    contentDescription = "Toggle Balance Visibility"
                )
            }
            TextButton(onClick = { showAll = !showAll }) {
                Text(
                    text = if (showAll) "Show Less" else "See All",
                    style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.primary)
                )
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            elevation = CardDefaults.cardElevation(2.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            // Transactions list or empty state
            if (filteredTransactions.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No transactions are found")
                }
            } else {
                LazyColumn {
                    items(transactionsToShow) { transaction ->
                        TransactionItem(transaction)
                    }
                }
            }
        }
    }
    // Filter Popup Dialog
    if (showFilterPopup) {
        AlertDialog(
            onDismissRequest = { showFilterPopup = false },
            title = { Text("Filter Options") },
            text = {
                Column {
                    FilterOption.values().forEach { option ->
                        val isSelected = option == selectedFilter
                        val textColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        val backgroundColor = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) else Color.Transparent

                        Text(
                            text = option.title,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .background(backgroundColor)
                                .clickable {
                                    when (option) {
                                        FilterOption.Latest -> {
                                            transactionViewModel.updateFilter(FilterOption.Latest)
                                            showFilterPopup = false // Close popup
                                            transactionViewModel.onDateSelected("")
                                        }
                                        FilterOption.Income -> {
                                            transactionViewModel.updateFilter(FilterOption.Income)
                                            showFilterPopup = false // Close popup
                                            transactionViewModel.onDateSelected("")
                                        }
                                        FilterOption.Expense -> {
                                            transactionViewModel.updateFilter(FilterOption.Expense)
                                            showFilterPopup = false // Close popup
                                            transactionViewModel.onDateSelected("")
                                        }
                                        FilterOption.Date -> {
                                            openDatePickerDialog(context) { selectedDate ->
                                                selectedDate?.let {
                                                    transactionViewModel.updateFilter(FilterOption.Date)
                                                    transactionViewModel.onDateSelected(it)
                                                    showFilterPopup = false // Close popup
                                                }
                                            }
                                        }
                                    }

                                    // Reset selected date if needed
                                    if (option != FilterOption.Date) {
                                        onDateSelected("") // Reset selected date
                                    }
                                }
                                .padding(vertical = 8.dp, horizontal = 16.dp),
                            color = textColor
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showFilterPopup = false }) {
                    Text("Close")
                }
            }
        )
    }
}
fun openDatePickerDialog(context: Context, onDateSelected: (String?) -> Unit) {
    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            // Calendar month is zero-based, so month + 1 is used to match standard month numbering (1-12).
            val selectedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .format(calendar.apply { set(year, month, dayOfMonth) }.time)
            onDateSelected(selectedDate)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )
    datePickerDialog.show()
}


//TransactionItem
@Composable
fun TransactionItem(transaction: UiTransaction) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(transaction.type.name, fontWeight = FontWeight.Bold)
            Text(transaction.date, color = Color.Gray, fontSize = 14.sp)
        }
        Text(
            text = if (transaction.type == TransactionType.Expense) "-₹${transaction.amount}" else "+₹${transaction.amount}",
            color = if (transaction.type == TransactionType.Expense) Color.Red else Color.Green,
            fontWeight = FontWeight.Bold
        )
    }
}

//AddTransactionCards
@Composable
fun AddTransactionCards(onAddIncome: () -> Unit, onAddExpense: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        // Add Income Card
        Card(
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD)) // Replace with your desired color
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(16.dp)
                    .clickable(onClick = onAddIncome)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Add Income",
                        modifier = Modifier.size(30.dp),
                        tint = Color(0xFF4CAF50) // Replace with your desired icon color
                    )
                    Text(
                        text = "Add Income",
                        fontSize = 18.sp,
                        modifier = Modifier.padding(10.dp),
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF4CAF50) // Replace with your desired text color
                    )
                }
            }
        }

        // Add Expense Card
        Card(
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE)) // Replace with your desired color
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(16.dp)
                    .clickable(onClick = onAddExpense)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Filled.AddCircle,
                        contentDescription = "Add Expense",
                        modifier = Modifier.size(30.dp),
                        tint = Color(0xFFF44336) // Replace with your desired icon color
                    )
                    Text(
                        text = "Add Expense",
                        fontSize = 18.sp,
                        modifier = Modifier.padding(10.dp),
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFFF44336) // Replace with your desired text color
                    )
                }
            }
        }
    }
}

