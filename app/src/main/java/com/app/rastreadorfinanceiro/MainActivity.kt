package com.app.rastreadorfinanceiro

import DashboardScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
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

        // 1. Inicializa o Banco de Dados (Singleton)
        val db = AppDatabase.getDatabase(applicationContext)

        // 2. Cria os Repositórios (Injeção de Dependência Manual)
        val transactionRepo = TransactionRepository(db.transactionDao(), db.categoryDao())
        val categoryRepo = CategoryRepository(db.categoryDao())
        val goalsRepo = GoalsRepository(db.goalDao())

        // 3. Cria a Fábrica de ViewModels
        val viewModelFactory = RastreadorViewModelFactory(
            transactionRepo,
            categoryRepo,
            goalsRepo
        )

        setContent {
            RastreadorFinanceiroTheme {
                // Passa a fábrica para o App
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
            // Container principal
            Box(modifier = Modifier.padding(innerPadding)) {

                // Switch de Navegação
                when (currentDestination) {
                    AppDestinations.HOME -> {
                        // Instancia os ViewModels necessários usando a Factory
                        val tViewModel: TransactionViewModel = viewModel(factory = viewModelFactory)
                        val cViewModel: CategoryViewModel = viewModel(factory = viewModelFactory)

                        // Chama a Tela Home
                        HomeScreen(
                            transactionViewModel = tViewModel,
                            categoryViewModel = cViewModel
                        )
                    }

                    AppDestinations.EXTRATO -> {
                        val tViewModel: TransactionViewModel = viewModel(factory = viewModelFactory)

                        // Chama a Tela Extrato
                        ExtratoScreen(viewModel = tViewModel)
                    }

                    AppDestinations.GRAFICOS -> {
                        val dViewModel: DashboardViewModel = viewModel(factory = viewModelFactory)
                        DashboardScreen(dViewModel)

                    }

// ... (outros destinations) ...

                    AppDestinations.GESTAO -> {
                        val cViewModel: CategoryViewModel = viewModel(factory = viewModelFactory)
                        // AQUI: Removemos o placeholder e chamamos a tela real
                        com.app.rastreadorfinanceiro.ui.screens.GestaoScreen(
                            categoryViewModel = cViewModel
                        )
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
    HOME("Home", Icons.Default.Add),
    EXTRATO("Extrato", Icons.Default.DateRange),
    GRAFICOS("Gráficos", Icons.Default.Info),
    GESTAO("Gestão", Icons.Default.AccountBox),
}