package com.app.rastreadorfinanceiro.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.foundation.shape.RoundedCornerShape
import com.app.rastreadorfinanceiro.model.CategoryModel
import com.app.rastreadorfinanceiro.model.ExpenseModel
import com.app.rastreadorfinanceiro.model.IncomeModel
import com.app.rastreadorfinanceiro.viewmodel.CategoryViewModel
import com.app.rastreadorfinanceiro.viewmodel.TransactionViewModel

@Composable
fun HomeScreen(
    transactionViewModel: TransactionViewModel,
    categoryViewModel: CategoryViewModel
) {
    val transactions = transactionViewModel.transactions
    val categories = categoryViewModel.categories

    val totalIncome = transactions.filterIsInstance<IncomeModel>().sumOf { it.amount }
    val totalExpense = transactions.filterIsInstance<ExpenseModel>().sumOf { it.amount }
    val balance = totalIncome - totalExpense

    var showIncomeDialog by remember { mutableStateOf(false) }
    var showExpenseDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Saldo Atual", style = MaterialTheme.typography.labelLarge)
                Text(
                    text = "R$ ${String.format("%.2f", balance)}",
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (balance >= 0) Color(0xFF2E7D32) else Color(0xFFC62828)
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { showIncomeDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
            ) {
                Icon(Icons.Default.KeyboardArrowUp, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Nova Receita")
            }

            Button(
                onClick = { showExpenseDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC62828))
            ) {
                Icon(Icons.Default.KeyboardArrowDown, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Nova Despesa")
            }
        }
    }

    if (showIncomeDialog) {
        AddTransactionDialog(
            title = "Adicionar Receita",
            onDismiss = { showIncomeDialog = false },
            onConfirm = { amount, desc, _ ->
                transactionViewModel.addIncome(amount, desc)
                showIncomeDialog = false
            }
        )
    }

    if (showExpenseDialog) {
        if (categories.isEmpty()) {
            AlertDialog(
                onDismissRequest = { showExpenseDialog = false },
                confirmButton = {
                    TextButton(onClick = {
                        categoryViewModel.addCategory("Geral", Color.Gray)
                        showExpenseDialog = false
                    }) { Text("Criar Categoria 'Geral'") }
                },
                title = { Text("Sem Categorias") },
                text = { Text("Crie uma categoria antes de adicionar despesas.") }
            )
        } else {
            AddTransactionDialog(
                title = "Adicionar Despesa",
                categories = categories,
                isExpense = true,
                onDismiss = { showExpenseDialog = false },
                onConfirm = { amount, desc, cat ->
                    if (cat != null) {
                        transactionViewModel.addExpense(amount, desc, cat)
                        showExpenseDialog = false
                    }
                }
            )
        }
    }
}

@Composable
fun AddTransactionDialog(
    title: String,
    categories: List<CategoryModel> = emptyList(),
    isExpense: Boolean = false,
    onDismiss: () -> Unit,
    onConfirm: (Double, String, CategoryModel?) -> Unit
) {
    var amountText by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    // Inicializa como null para forçar o usuário a escolher se for Despesa
    var selectedCategory by remember { mutableStateOf<CategoryModel?>(null) }
    var expanded by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(title, style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(16.dp))

                OutlinedTextField(
                    value = amountText,
                    onValueChange = { amountText = it },
                    label = { Text("Valor (R$)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Descrição") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))

                if (isExpense && categories.isNotEmpty()) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        OutlinedButton(
                            onClick = { expanded = true },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = selectedCategory?.name ?: "Selecione a Categoria",
                                color = if (selectedCategory == null) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
                            )
                        }
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            categories.forEach { category ->
                                DropdownMenuItem(
                                    text = { Text(category.name) },
                                    onClick = {
                                        selectedCategory = category
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                Spacer(Modifier.height(24.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismiss) { Text("Cancelar") }


                    val isValid = amountText.toDoubleOrNull() != null &&
                            description.isNotEmpty() &&
                            (!isExpense || selectedCategory != null)

                    Button(
                        onClick = {
                            val amount = amountText.toDoubleOrNull()
                            if (amount != null) {
                                onConfirm(amount, description, selectedCategory)
                            }
                        },
                        enabled = isValid
                    ) { Text("Salvar") }
                }
            }
        }
    }
}