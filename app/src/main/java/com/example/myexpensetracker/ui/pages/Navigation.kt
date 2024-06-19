package com.example.myexpensetracker.ui.pages

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.myexpensetracker.ui.viewModel.TransactionViewModel


sealed class AllScreens(val route: String) {
    object Home : AllScreens("home")
    object Transactions : AllScreens("transactions")
    object Profile : AllScreens("profile")
    object AddIncome : AllScreens("addIncome")
    object AddExpense: AllScreens("addExpenses")
}


@Composable
fun AppNavigation(navController: NavController, modifier: Modifier = Modifier) {
    NavHost(navController = navController as NavHostController, startDestination = AllScreens.Home.route, modifier = modifier) {
        composable(route = AllScreens.Home.route) {
            val transactionViewModel = viewModel<TransactionViewModel>()
            ExpenseTrackerHome(transactionViewModel = transactionViewModel)
        }
        composable(route = AllScreens.Transactions.route) {
            TransactionsScreen(navController)
        }
        composable(route = AllScreens.Profile.route) {
            ProfileScreen(navController)
        }
        composable(route = "addTransaction") {
            AddTransactionScreen(navController)
        }
        composable(route = AllScreens.AddIncome.route) {
            AddIncomeScreen(navController)
        }
        composable(route = AllScreens.AddExpense.route) {
            AddExpenseScreen(navController)
        }
    }
}