package com.app.rastreadorfinanceiro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.rastreadorfinanceiro.data.AppDatabase
import com.app.rastreadorfinanceiro.repository.CategoryRepository
import com.app.rastreadorfinanceiro.repository.GoalsRepository
import com.app.rastreadorfinanceiro.repository.TransactionRepository
import com.app.rastreadorfinanceiro.ui.screens.ExtratoScreen
import com.app.rastreadorfinanceiro.ui.screens.HomeScreen
import com.app.rastreadorfinanceiro.ui.theme.RastreadorFinanceiroTheme
import com.app.rastreadorfinanceiro.viewmodel.CategoryViewModel
import com.app.rastreadorfinanceiro.viewmodel.DashboardViewModel
import com.app.rastreadorfinanceiro.viewmodel.GoalsViewModel
import com.app.rastreadorfinanceiro.viewmodel.RastreadorViewModelFactory
import com.app.rastreadorfinanceiro.viewmodel.TransactionViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val db = AppDatabase.getDatabase(applicationContext)


        val transactionRepo = TransactionRepository(db.transactionDao(), db.categoryDao())
        val categoryRepo = CategoryRepository(db.categoryDao())
        val goalsRepo = GoalsRepository(db.goalDao())


        val viewModelFactory = RastreadorViewModelFactory(
            transactionRepo,
            categoryRepo,
            goalsRepo
        )

        setContent {
            RastreadorFinanceiroTheme {

                RastreadorFinanceiroApp(viewModelFactory)
            }
        }
    }
}

@Composable
fun RastreadorFinanceiroApp(viewModelFactory: RastreadorViewModelFactory) {
    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.HOME) }

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            AppDestinations.entries.forEach {
                item(
                    icon = {
                        Icon(
                            it.icon,
                            contentDescription = it.label
                        )
                    },
                    label = { Text(it.label) },
                    selected = it == currentDestination,
                    onClick = { currentDestination = it }
                )
            }
        }
    ) {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

            Box(modifier = Modifier.padding(innerPadding)) {


                when (currentDestination) {
                    AppDestinations.HOME -> {

                        val tViewModel: TransactionViewModel = viewModel(factory = viewModelFactory)
                        val cViewModel: CategoryViewModel = viewModel(factory = viewModelFactory)


                        com.app.rastreadorfinanceiro.ui.screens.AddTransactionScreen(
                            transactionViewModel = tViewModel,
                            categoryViewModel = cViewModel
                        )
                    }

                    AppDestinations.GRAFICOS -> {
                        val dViewModel: DashboardViewModel = viewModel(factory = viewModelFactory)


                        com.app.rastreadorfinanceiro.ui.screens.GraficosScreen(viewModel = dViewModel)
                    }

                    AppDestinations.EXTRATO -> {
                        val tViewModel: TransactionViewModel = viewModel(factory = viewModelFactory)


                        ExtratoScreen(viewModel = tViewModel)
                    }
                }
            }
        }
    }
}

enum class AppDestinations(
    val label: String,
    val icon: ImageVector,
) {
    HOME("Adicionar", Icons.Default.Add),
    GRAFICOS("Gráficos", Icons.Default.Info),
    EXTRATO("Extrato", Icons.Default.DateRange),
}