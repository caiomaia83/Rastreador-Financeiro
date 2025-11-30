package com.app.rastreadorfinanceiro.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.app.rastreadorfinanceiro.model.ExpenseModel
import com.app.rastreadorfinanceiro.model.IncomeModel
import com.app.rastreadorfinanceiro.model.TransactionModel
import com.app.rastreadorfinanceiro.viewmodel.TransactionViewModel
import java.time.format.DateTimeFormatter

@Composable
fun ExtratoScreen(viewModel: TransactionViewModel) {
    val transactions = viewModel.transactions


    val groupedTransactions = remember(transactions) {
        transactions.groupBy { transaction ->
            when (transaction) {

                is ExpenseModel -> transaction.category

                is IncomeModel -> null
                else -> null
            }
        }
    }

    if (transactions.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Nenhuma movimentação registrada.")
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            groupedTransactions.forEach { (category, transactionList) ->

                item {
                    val categoryName = category?.name ?: "Receitas"


                    Column(modifier = Modifier.fillMaxWidth().padding(top = 16.dp, bottom = 8.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = categoryName,
                                style = MaterialTheme.typography.titleMedium,
                                color = category?.color ?: MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )


                            if (category?.budgetLimit != null && category.budgetLimit > 0) {
                                val totalExpenses = transactionList.sumOf { it.amount }
                                val limit = category.budgetLimit
                                val isOverBudget = totalExpenses > limit
                                val statusColor = if (isOverBudget) Color.Red else Color(0xFF2E7D32)

                                Text(
                                    text = "${String.format("R$ %.0f", totalExpenses)} / R$ ${String.format("%.0f", limit)}",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = statusColor,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        // BARRA DE PROGRESSO DO ORÇAMENTO
                        if (category?.budgetLimit != null && category.budgetLimit > 0) {
                            val totalExpenses = transactionList.sumOf { it.amount }
                            val limit = category.budgetLimit
                            val progress = (totalExpenses / limit).toFloat().coerceIn(0f, 1f)
                            val isOverBudget = totalExpenses > limit

                            Spacer(modifier = Modifier.height(4.dp))
                            LinearProgressIndicator(
                                progress = { progress },
                                modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                                color = if (isOverBudget) Color.Red else category.color,
                                trackColor = Color.LightGray.copy(alpha = 0.3f),
                            )
                            if (isOverBudget) {
                                Text(
                                    text = "Limite excedido!",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.Red,
                                    modifier = Modifier.align(Alignment.End)
                                )
                            }
                        }
                    }
                }

                items(transactionList) { transaction ->
                    TransactionItem(
                        transaction = transaction,
                        onDelete = { viewModel.removeTransaction(transaction) }
                    )
                }
            }
        }
    }
}


@Composable
fun TransactionItem(transaction: TransactionModel, onDelete: () -> Unit) {

    val isExpense = transaction is ExpenseModel
    val color = if (isExpense) Color(0xFFC62828) else Color(0xFF2E7D32)
    val amountPrefix = if (isExpense) "-" else "+"
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = transaction.description, style = MaterialTheme.typography.bodyLarge)
                Text(text = transaction.date.format(formatter), style = MaterialTheme.typography.bodySmall)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "$amountPrefix R$ ${String.format("%.2f", transaction.amount)}",
                    color = color,
                    style = MaterialTheme.typography.titleMedium
                )
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Deletar", tint = Color.Gray)
                }
            }
        }
    }
}