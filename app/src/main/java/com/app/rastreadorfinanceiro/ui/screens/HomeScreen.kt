package com.app.rastreadorfinanceiro.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.foundation.shape.RoundedCornerShape
import com.app.rastreadorfinanceiro.model.CategoryModel
import com.app.rastreadorfinanceiro.model.ExpenseModel
import com.app.rastreadorfinanceiro.model.IncomeModel
import com.app.rastreadorfinanceiro.model.TransactionModel
import com.app.rastreadorfinanceiro.ui.components.CategoryFormDialog
import com.app.rastreadorfinanceiro.ui.theme.*
import com.app.rastreadorfinanceiro.viewmodel.CategoryViewModel
import com.app.rastreadorfinanceiro.viewmodel.TransactionViewModel
import java.time.format.DateTimeFormatter

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
    var selectedCategory by remember { mutableStateOf<CategoryModel?>(null) }
    var showAddCategoryDialog by remember { mutableStateOf(false) }


    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        // Header with value input section
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Semi-transparent container for value input
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = Color.Black.copy(alpha = 0.3f),
                            shape = RoundedCornerShape(14.dp)
                        )
                        .border(
                            width = 1.dp,
                            color = Color.White.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(14.dp)
                        )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Saldo Atual",
                            style = MaterialTheme.typography.labelMedium,
                            color = TextSecondary.copy(alpha = 0.7f),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Normal
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "R$ ${String.format("%.2f", balance)}",
                            style = MaterialTheme.typography.displaySmall,
                            fontWeight = FontWeight.Bold,
                            fontSize = 42.sp,
                            color = if (balance >= 0) SuccessGreenLight else ErrorRedLight
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Income and Expense summary in semi-transparent boxes
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .background(
                                        Color.Black.copy(alpha = 0.2f),
                                        RoundedCornerShape(10.dp)
                                    )
                                    .border(
                                        0.5.dp,
                                        Color.White.copy(alpha = 0.08f),
                                        RoundedCornerShape(10.dp)
                                    )
                                    .padding(12.dp)
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        "Receitas",
                                        color = TextSecondary.copy(alpha = 0.6f),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Normal
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        "R$ ${String.format("%.2f", totalIncome)}",
                                        color = SuccessGreenLight,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 16.sp
                                    )
                                }
                            }
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .background(
                                        Color.Black.copy(alpha = 0.2f),
                                        RoundedCornerShape(10.dp)
                                    )
                                    .border(
                                        0.5.dp,
                                        Color.White.copy(alpha = 0.08f),
                                        RoundedCornerShape(10.dp)
                                    )
                                    .padding(12.dp)
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        "Despesas",
                                        color = TextSecondary.copy(alpha = 0.6f),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Normal
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        "R$ ${String.format("%.2f", totalExpense)}",
                                        color = ErrorRedLight,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 16.sp
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Quick action buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { showIncomeDialog = true },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = SuccessGreen.copy(alpha = 0.9f)
                                ),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(44.dp),
                                elevation = ButtonDefaults.buttonElevation(0.dp)
                            ) {
                                Icon(
                                    Icons.Default.KeyboardArrowUp,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(Modifier.width(6.dp))
                                Text("Receita", fontSize = 13.sp, fontWeight = FontWeight.Medium)
                            }

                            Button(
                                onClick = { showExpenseDialog = true },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = ErrorRed.copy(alpha = 0.9f)
                                ),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(44.dp),
                                elevation = ButtonDefaults.buttonElevation(0.dp)
                            ) {
                                Icon(
                                    Icons.Default.KeyboardArrowDown,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(Modifier.width(6.dp))
                                Text("Despesa", fontSize = 13.sp, fontWeight = FontWeight.Medium)
                            }
                        }
                    }
                }
            }
        }

        // Categories section
        item {
            Text(
                text = "Categorias",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary.copy(alpha = 0.9f),
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 12.dp)
            )
        }

        item {
            val totalItems = categories.size + 1 // +1 for AddCategory card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(((totalItems + 2) / 3 * 55).dp + 60.dp)
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(categories) { category ->
                        CategoryCard(
                            category = category,
                            isSelected = selectedCategory == category,
                            onClick = {
                                selectedCategory = if (selectedCategory == category) null else category
                                showExpenseDialog = true
                            }
                        )
                    }

                    // AddCategory card
                    item {
                        AddCategoryCard(
                            onClick = { showAddCategoryDialog = true }
                        )
                    }
                }
                // Gradient overlay at bottom
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .height(60.dp)
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    DarkBackground
                                )
                            )
                        )
                )
            }
        }

            // Add spacing at bottom
        item {
            Spacer(modifier = Modifier.height(16.dp))
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
                onDismissRequest = {
                    showExpenseDialog = false
                    selectedCategory = null
                },
                confirmButton = {
                    TextButton(onClick = {
                        categoryViewModel.addCategory("Geral", Color.Gray)
                        showExpenseDialog = false
                        selectedCategory = null
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
                initialCategory = selectedCategory,
                onDismiss = {
                    showExpenseDialog = false
                    selectedCategory = null
                },
                onConfirm = { amount, desc, cat ->
                    if (cat != null) {
                        transactionViewModel.addExpense(amount, desc, cat)
                        showExpenseDialog = false
                        selectedCategory = null
                    }
                }
            )
        }
    }

    // Add Category Dialog
    if (showAddCategoryDialog) {
        CategoryFormDialog(
            title = "Nova Categoria",
            onDismiss = { showAddCategoryDialog = false },
            onConfirm = { name, color, limit ->
                categoryViewModel.addCategory(name, color, limit)
                showAddCategoryDialog = false
            }
        )
    }
}

@Composable
fun AddTransactionDialog(
    title: String,
    categories: List<CategoryModel> = emptyList(),
    isExpense: Boolean = false,
    initialCategory: CategoryModel? = null,
    onDismiss: () -> Unit,
    onConfirm: (Double, String, CategoryModel?) -> Unit
) {
    var amountText by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    // Inicializa com a categoria selecionada se houver
    var selectedCategory by remember { mutableStateOf(initialCategory) }
    var expanded by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = DarkCard)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    title,
                    style = MaterialTheme.typography.titleLarge,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(20.dp))

                OutlinedTextField(
                    value = amountText,
                    onValueChange = { amountText = it },
                    label = { Text("Valor (R$)", color = TextSecondary) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryLight,
                        unfocusedBorderColor = TextTertiary,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Descrição", color = TextSecondary) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryLight,
                        unfocusedBorderColor = TextTertiary,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
                Spacer(Modifier.height(12.dp))

                if (isExpense && categories.isNotEmpty()) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        OutlinedButton(
                            onClick = { expanded = true },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = if (selectedCategory == null) ErrorRed else TextPrimary
                            )
                        ) {
                            Text(
                                text = selectedCategory?.name ?: "Selecione a Categoria"
                            )
                        }
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier.background(DarkCard)
                        ) {
                            categories.forEach { category ->
                                DropdownMenuItem(
                                    text = { Text(category.name, color = TextPrimary) },
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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancelar", color = TextSecondary)
                    }
                    Spacer(Modifier.width(8.dp))

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
                        enabled = isValid,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PrimaryLight,
                            disabledContainerColor = TextTertiary
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Salvar", color = TextPrimary)
                    }
                }
            }
        }
    }
}

// Helper function to get icon for category
fun getCategoryIcon(categoryName: String): ImageVector {
    return when {
        categoryName.contains("comida", ignoreCase = true) ||
        categoryName.contains("alimentação", ignoreCase = true) ||
        categoryName.contains("food", ignoreCase = true) -> Icons.Default.Place

        categoryName.contains("transporte", ignoreCase = true) ||
        categoryName.contains("transport", ignoreCase = true) -> Icons.Default.Phone

        categoryName.contains("saúde", ignoreCase = true) ||
        categoryName.contains("health", ignoreCase = true) -> Icons.Default.Favorite

        categoryName.contains("educação", ignoreCase = true) ||
        categoryName.contains("education", ignoreCase = true) -> Icons.Default.Info

        categoryName.contains("lazer", ignoreCase = true) ||
        categoryName.contains("entretenimento", ignoreCase = true) ||
        categoryName.contains("entertainment", ignoreCase = true) -> Icons.Default.Star

        categoryName.contains("casa", ignoreCase = true) ||
        categoryName.contains("home", ignoreCase = true) -> Icons.Default.Home

        categoryName.contains("compras", ignoreCase = true) ||
        categoryName.contains("shopping", ignoreCase = true) -> Icons.Default.ShoppingCart

        else -> Icons.Default.Add
    }
}

@Composable
fun CategoryCard(
    category: CategoryModel,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val borderWidth = if (isSelected) 2.5.dp else 1.5.dp
    val iconColor = if (isSelected) category.color else category.color.copy(alpha = 0.8f)
    val textAlpha = if (isSelected) 1f else 0.75f
    val textWeight = if (isSelected) FontWeight.W600 else FontWeight.W500

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp)
            .clickable(onClick = onClick)
            .background(
                color = if (isSelected)
                    category.color.copy(alpha = 0.15f)
                else
                    Color.Black.copy(alpha = 0.3f),
                shape = RoundedCornerShape(12.dp)
            )
            .border(
                width = borderWidth,
                color = if (isSelected)
                    category.color.copy(alpha = 0.8f)
                else
                    TextSecondary.copy(alpha = 0.12f),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(vertical = 2.dp, horizontal = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(
                imageVector = getCategoryIcon(category.name),
                contentDescription = category.name,
                tint = iconColor,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier.height(33.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = category.name,
                    fontSize = 14.sp,
                    color = TextPrimary.copy(alpha = textAlpha),
                    fontWeight = textWeight,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 16.sp
                )
            }
        }
    }
}

@Composable
fun RecentTransactionItem(
    transaction: TransactionModel,
    modifier: Modifier = Modifier
) {
    val isExpense = transaction is ExpenseModel
    val color = if (isExpense) ErrorRedLight else SuccessGreenLight
    val amountPrefix = if (isExpense) "-" else "+"
    val formatter = DateTimeFormatter.ofPattern("dd/MM")

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = Color.Black.copy(alpha = 0.25f),
                shape = RoundedCornerShape(12.dp)
            )
            .border(
                width = 0.5.dp,
                color = Color.White.copy(alpha = 0.08f),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(
                            if (isExpense)
                                (transaction as ExpenseModel).category.color.copy(alpha = 0.9f)
                            else SuccessGreen.copy(alpha = 0.9f)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isExpense) {
                            getCategoryIcon((transaction as ExpenseModel).category.name)
                        } else {
                            Icons.Default.KeyboardArrowUp
                        },
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = transaction.description,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextPrimary.copy(alpha = 0.95f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = transaction.date.format(formatter),
                        fontSize = 12.sp,
                        color = TextSecondary.copy(alpha = 0.6f)
                    )
                }
            }
            Text(
                text = "$amountPrefix R$ ${String.format("%.2f", transaction.amount)}",
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = color
            )
        }
    }
}

@Composable
fun AddCategoryCard(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp)
            .clickable(onClick = onClick)
            .background(
                color = Color.Transparent,
                shape = RoundedCornerShape(12.dp)
            )
            .border(
                width = 1.5.dp,
                color = TextSecondary.copy(alpha = 0.3f),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(vertical = 2.dp, horizontal = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Adicionar Categoria",
                tint = TextSecondary.copy(alpha = 0.6f),
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier.height(33.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Adicionar",
                    fontSize = 14.sp,
                    color = TextSecondary.copy(alpha = 0.6f),
                    fontWeight = FontWeight.W500,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 16.sp
                )
            }
        }
    }
}

