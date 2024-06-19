package com.example.myexpensetracker.ui.pages

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.myexpensetracker.R
import com.example.myexpensetracker.ui.viewModel.TransactionViewModel
import java.text.SimpleDateFormat
import java.util.*


@Composable
fun AddIncomeScreen(navController: NavController, viewModel: TransactionViewModel = viewModel()) {
    val calendar = remember { Calendar.getInstance() }
    val dateFormat = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) }
    val dateState = remember { mutableStateOf("") }
    val amountState = remember { mutableStateOf("") }
    val descriptionState = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        ScreenTitle("Add Income")

        Spacer(modifier = Modifier.height(16.dp))

        DatePickerField(
            label = "Date",
            viewModel = viewModel
        )

        Spacer(modifier = Modifier.height(8.dp))

        CurrencyTextField(
            label = "Amount",
            valueState = amountState,
            viewModel = viewModel
        )

        Spacer(modifier = Modifier.height(8.dp))

        DescriptionTextField(
            label = "Description",
            valueState = descriptionState,
            viewModel = viewModel
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val newTransaction = Transaction(
                    id = UUID.randomUUID().toString(),
                    type = TransactionType.Income,
                    date = dateState.value,
                    amount = amountState.value.toDoubleOrNull() ?: 0.0,
                    description = descriptionState.value,
                    isExpense = false,
                    category = "",
                    paymentMode = ""

                )
                viewModel.addTransaction(newTransaction)
                navController.popBackStack()
            },
            colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.blue))
        ) {
            Text("Add Income")
        }
    }
}

@Composable
fun AddExpenseScreen(navController: NavController, viewModel: TransactionViewModel = viewModel()) {
    val calendar = remember { Calendar.getInstance() }
    val dateFormat = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }
    val dateState = remember { mutableStateOf("") }
    val amountState = remember { mutableStateOf("") }
    val descriptionState = remember { mutableStateOf("") }
    val selectedCategory = remember { mutableStateOf("") }
    val selectedPaymentMode = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        ScreenTitle("Add Expense")

        Spacer(modifier = Modifier.height(16.dp))

        DatePickerField(
            label = "Date",
            viewModel = viewModel
        )

        Spacer(modifier = Modifier.height(8.dp))

        CurrencyTextField(
            label = "Amount",
            valueState = amountState,
            viewModel = viewModel
        )

        Spacer(modifier = Modifier.height(8.dp))

        DescriptionTextField(
            label = "Description",
            valueState = descriptionState,
            viewModel = viewModel
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Dropdown menu for category
        DropdownMenuWithDialog(
            label = "Category",
            items = viewModel.categories.collectAsState().value.map { it.name },
            selectedItem = selectedCategory,
            viewModel = viewModel,
            addCategory = { category -> viewModel.addCategory(category) } // Add new category
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Dropdown menu for payment mode
        DropdownMenuWithDialog(
            label = "Payment Mode",
            items = viewModel.paymentModes.collectAsState().value.map { it.name },
            selectedItem = selectedPaymentMode,
            viewModel = viewModel,
            addPaymentMode = { paymentMode -> viewModel.addPaymentMode(paymentMode) } // Add new payment mode
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val newTransaction = Transaction(
                    id = UUID.randomUUID().toString(),
                    type = TransactionType.Expense,
                    date = dateState.value,
                    amount = amountState.value.toDoubleOrNull() ?: 0.0,
                    description = descriptionState.value,
                    isExpense = true,
                    category = selectedCategory.value,
                    paymentMode = selectedPaymentMode.value
                )
                viewModel.addTransaction(newTransaction)
                navController.popBackStack()
            },
            colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.blue))
        ) {
            Text("Add Expense")
        }
    }
}

@Composable
fun ScreenTitle(title: String) {
    Text(text = title, style = MaterialTheme.typography.titleLarge)
}

@Composable
fun CurrencyTextField(label: String, valueState: MutableState<String>, viewModel: TransactionViewModel? = null) {
    TextField(
        value = valueState.value,
        onValueChange = {
            valueState.value = it
            //viewModel?.updateCurrencyValue(it) // Optional: Update ViewModel if needed
        },
        label = { Text(label) },
        leadingIcon = { Text(text = "₹", style = TextStyle(fontSize = 20.sp)) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        colors = TextFieldDefaults.colors(
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black,
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White
        ),
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun DescriptionTextField(label: String, valueState: MutableState<String>, viewModel: TransactionViewModel? = null) {
    TextField(
        value = valueState.value,
        onValueChange = {
            valueState.value = it
            //viewModel?.updateDescriptionValue(it) // Optional: Update ViewModel if needed
        },
        label = { Text(label) },
        colors = TextFieldDefaults.colors(
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black,
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White
        ),
        modifier = Modifier.fillMaxWidth()
    )
}

//@Composable
//fun AddButton(text: String, navController: NavController, viewModel: TransactionViewModel) {
//    val buttonColor = colorResource(id = R.color.blue)
//
//    Button(
//        onClick = {
//            // Perform ViewModel operation or navigation action
//            viewModel.addTransaction(/* pass transaction data if needed */)
//            navController.popBackStack()
//        },
//        colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
//    ) {
//        Text(text)
//    }
//}

@Composable
fun DatePickerField(
    label: String,
    viewModel: TransactionViewModel
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val selectedDate = remember { mutableStateOf("") }

    val datePickerDialog = remember {
        DatePickerDialog(
            context,
            { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                calendar.set(year, month, dayOfMonth)
                val formattedDate = dateFormat.format(calendar.time)
                selectedDate.value = formattedDate
                viewModel.onDateSelected(formattedDate) // Update ViewModel with selected date
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    Box(modifier = Modifier
        .fillMaxWidth()
        .clickable { datePickerDialog.show() }) {
        TextField(
            value = selectedDate.value,
            onValueChange = {},
            label = { Text(label) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Select Date",
                    modifier = Modifier.clickable { datePickerDialog.show() }
                )
            },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownMenuWithDialog(
    label: String,
    items: List<String>,
    selectedItem: MutableState<String>,
    viewModel: TransactionViewModel,
    addCategory: ((String) -> Unit)? = null, // Nullable lambda for adding a new category
    addPaymentMode: ((String) -> Unit)? = null // Nullable lambda for adding a new payment mode
) {
    var expanded by remember { mutableStateOf(false) }
    val showDialog = remember { mutableStateOf(false) }
    val newItemState = remember { mutableStateOf("") }

    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 4.dp)) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            TextField(
                value = selectedItem.value,
                onValueChange = {},
                label = { Text(label) },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                readOnly = true,
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(10.dp)
                    .border(1.dp, Color.LightGray, RoundedCornerShape(4.dp))
            ) {
                items.forEach { item ->
                    val isSelected = item == selectedItem.value
                    DropdownMenuItem(
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                }
                                Text(item)
                            }
                        },
                        onClick = {
                            selectedItem.value = item
                            expanded = false
                            // Update ViewModel with selected item based on label
                            when (label) {
                                "Category" -> viewModel.updateFilter(FilterOption.valueOf(item))
                                "Payment Mode" -> viewModel.updatePaymentMode(item)
                            }
                        },
                        modifier = Modifier
                            .background(
                                if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.12f) else Color.White
                            )
                    )
                }
                DropdownMenuItem(
                    text = { Text("Add New $label") },
                    onClick = {
                        showDialog.value = true
                        expanded = false
                    },
                    modifier = Modifier
                        .background(Color.White)
                )
            }
        }
    }

    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { Text(text = "Add New $label") },
            text = {
                Column {
                    Text(text = "$label Name")
                    TextField(
                        value = newItemState.value,
                        onValueChange = { newItemState.value = it },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    if (newItemState.value.isNotBlank()) {
                        // Add new item to ViewModel or handle as needed
                        when (label) {
                            "Category" -> {
                                addCategory?.invoke(newItemState.value)
                                selectedItem.value = newItemState.value
                            }
                            "Payment Mode" -> {
                                addPaymentMode?.invoke(newItemState.value)
                                selectedItem.value = newItemState.value
                            }
                        }
                        newItemState.value = ""
                        showDialog.value = false
                    }
                }) {
                    Text("Save")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog.value = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}


@Composable
fun TransactionsScreen(navController: NavController) {
    // Your TransactionsScreen content
}

@Composable
fun ProfileScreen(navController: NavController) {
    // Your ProfileScreen content
}
