package com.app.rastreadorfinanceiro.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.rastreadorfinanceiro.model.CategoryModel
import com.app.rastreadorfinanceiro.ui.theme.*
import com.app.rastreadorfinanceiro.viewmodel.CategoryViewModel
import com.app.rastreadorfinanceiro.viewmodel.TransactionViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AddTransactionScreen(
    transactionViewModel: TransactionViewModel,
    categoryViewModel: CategoryViewModel,
    onTransactionAdded: () -> Unit = {}
) {
    var amountText by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedCategoryIndex by remember { mutableStateOf(0) }
    var selectedDate by remember { mutableStateOf(LocalDateTime.now()) }
    var showToast by remember { mutableStateOf(false) }
    var toastData by remember { mutableStateOf<ToastData?>(null) }
    var isExpense by remember { mutableStateOf(true) } // true = Despesa, false = Receita
    var showAddCategoryDialog by remember { mutableStateOf(false) }

    val categories = categoryViewModel.categories

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(DarkBackground)
        ) {
            // Header with gradient background
            HeaderWithGradient(
                amountText = amountText,
                onAmountChange = { amountText = it },
                description = description,
                onDescriptionChange = { description = it },
                selectedDate = selectedDate,
                onDateClick = { },
                isExpense = isExpense,
                onToggleType = { isExpense = !isExpense }
            )

            // Category grid (only for expenses)
            if (isExpense) {
                CategoryGridSection(
                    categories = categories,
                    selectedIndex = selectedCategoryIndex,
                    onCategorySelected = { selectedCategoryIndex = it },
                    onAddCategory = { showAddCategoryDialog = true }
                )
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }

            // Insert button
            InsertExpenseButton(
                onClick = {
                    val amount = amountText.replace(",", ".").toDoubleOrNull()
                    if (amount != null && amount > 0) {
                        if (isExpense) {
                            if (categories.isNotEmpty()) {
                                val selectedCategory = categories[selectedCategoryIndex]
                                transactionViewModel.addExpense(
                                    amount = amount,
                                    description = description.ifEmpty { "Sem descrição" },
                                    category = selectedCategory
                                )

                                // Show toast
                                toastData = ToastData(
                                    amount = amount,
                                    description = description.ifEmpty { "Sem descrição" },
                                    categoryName = selectedCategory.name,
                                    categoryColor = selectedCategory.color,
                                    isExpense = true
                                )
                                showToast = true
                            }
                        } else {
                            // Add income
                            transactionViewModel.addIncome(
                                amount = amount,
                                description = description.ifEmpty { "Sem descrição" }
                            )

                            // Show toast
                            toastData = ToastData(
                                amount = amount,
                                description = description.ifEmpty { "Sem descrição" },
                                categoryName = "Receita",
                                categoryColor = SuccessGreenLight,
                                isExpense = false
                            )
                            showToast = true
                        }

                        // Reset fields
                        amountText = ""
                        description = ""

                        // Hide toast after delay
                        GlobalScope.launch {
                            delay(2500)
                            showToast = false
                        }

                        onTransactionAdded()
                    }
                },
                enabled = amountText.replace(",", ".").toDoubleOrNull() != null &&
                          (!isExpense || categories.isNotEmpty()),
                isExpense = isExpense
            )

            Spacer(modifier = Modifier.height(16.dp))
        }

        // Add Category Dialog
        if (showAddCategoryDialog) {
            com.app.rastreadorfinanceiro.ui.components.CategoryFormDialog(
                title = "Nova Categoria",
                onDismiss = { showAddCategoryDialog = false },
                onConfirm = { name, color, limit, icon ->
                    categoryViewModel.addCategory(name, color, limit, icon)
                    showAddCategoryDialog = false
                }
            )
        }

        // Toast overlay
        AnimatedVisibility(
            visible = showToast,
            enter = slideInVertically(
                initialOffsetY = { -it },
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            ) + fadeIn(),
            exit = slideOutVertically(
                targetOffsetY = { -it },
                animationSpec = tween(300)
            ) + fadeOut(),
            modifier = Modifier.align(Alignment.TopCenter)
        ) {
            toastData?.let {
                AddedExpenseToast(it)
            }
        }
    }
}

data class ToastData(
    val amount: Double,
    val description: String,
    val categoryName: String,
    val categoryColor: Color,
    val isExpense: Boolean = true
)

@Composable
fun AddedExpenseToast(data: ToastData) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(20.dp, RoundedCornerShape(18.dp))
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF2A2A2A),
                            Color(0xFF1F1F1F)
                        )
                    ),
                    shape = RoundedCornerShape(18.dp)
                )
                .border(
                    width = 1.2.dp,
                    color = Color.White.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(18.dp)
                )
                .padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Icon
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(data.categoryColor.copy(alpha = 0.2f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = SuccessGreenLight,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Content
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        if (data.isExpense) "Despesa Adicionada" else "Receita Adicionada",
                        style = TextStyle(
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.W700,
                            letterSpacing = 0.3.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Box(
                        modifier = Modifier
                            .background(
                                Color.White.copy(alpha = 0.1f),
                                RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            "R$ %.2f".format(data.amount),
                            style = TextStyle(
                                color = SuccessGreenLight,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.W700
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            data.description,
                            style = TextStyle(
                                color = Color.White.copy(alpha = 0.7f),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.W500
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f, fill = false)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .size(4.dp)
                                .background(
                                    Color.White.copy(alpha = 0.54f),
                                    RoundedCornerShape(2.dp)
                                )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            data.categoryName,
                            style = TextStyle(
                                color = Color.White.copy(alpha = 0.7f),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.W500
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HeaderWithGradient(
    amountText: String,
    onAmountChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
    selectedDate: LocalDateTime,
    onDateClick: () -> Unit,
    isExpense: Boolean,
    onToggleType: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(15.dp, RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        DarkBackgroundGradient1,
                        DarkBackgroundGradient2
                    )
                ),
                shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp)
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Toggle Receita/Despesa
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .clickable { if (!isExpense) onToggleType() }
                        .background(if (isExpense) ButtonSelected else Color.Transparent)
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Despesa",
                        style = TextStyle(
                            color = if (isExpense) TextPrimary else TextSecondary.copy(alpha = 0.6f),
                            fontSize = 14.sp,
                            fontWeight = if (isExpense) FontWeight.W600 else FontWeight.W500
                        )
                    )
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .clickable { if (isExpense) onToggleType() }
                        .background(if (!isExpense) ButtonSelected else Color.Transparent)
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Receita",
                        style = TextStyle(
                            color = if (!isExpense) TextPrimary else TextSecondary.copy(alpha = 0.6f),
                            fontSize = 14.sp,
                            fontWeight = if (!isExpense) FontWeight.W600 else FontWeight.W500
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Header bar with date
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    if (isExpense) "Registrar Despesa" else "Registrar Receita",
                    style = TextStyle(
                        color = TextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.W600
                    )
                )

                // Date selector
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .clickable(onClick = onDateClick)
                        .border(
                            1.dp,
                            Color.White.copy(alpha = 0.2f),
                            RoundedCornerShape(10.dp)
                        )
                        .background(Color.Black.copy(alpha = 0.2f))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    val formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM")
                    Text(
                        selectedDate.format(formatter),
                        style = TextStyle(
                            color = TextSecondary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.W500
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Value input section
            ValueInputSection(
                amountText = amountText,
                onAmountChange = onAmountChange
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Description input
            DescriptionInput(
                description = description,
                onDescriptionChange = onDescriptionChange
            )

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun ValueInputSection(
    amountText: String,
    onAmountChange: (String) -> Unit
) {
    val quickValues = listOf(
        listOf(5, 10, 20),
        listOf(50, 100, 200)
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Color.Black.copy(alpha = 0.3f),
                RoundedCornerShape(14.dp)
            )
            .border(
                1.dp,
                Color.White.copy(alpha = 0.1f),
                RoundedCornerShape(14.dp)
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp)
        ) {
            // Amount input
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "R$ ",
                    style = TextStyle(
                        color = TextPrimary,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
                BasicTextField(
                    value = amountText,
                    onValueChange = { newValue ->
                        if (newValue.all { it.isDigit() || it == ',' || it == '.' }) {
                            onAmountChange(newValue)
                        }
                    },
                    textStyle = TextStyle(
                        color = TextPrimary,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Start
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    decorationBox = { innerTextField ->
                        Box(
                            contentAlignment = Alignment.CenterStart
                        ) {
                            if (amountText.isEmpty()) {
                                Text(
                                    "0,00",
                                    style = TextStyle(
                                        color = TextSecondary.copy(alpha = 0.5f),
                                        fontSize = 32.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                            innerTextField()
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Quick value buttons in 2 rows
            quickValues.forEach { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    row.forEach { value ->
                        QuickValueButton(
                            value = value,
                            onClick = {
                                val current = amountText.replace(",", ".").toDoubleOrNull() ?: 0.0
                                val newAmount = current + value
                                onAmountChange(newAmount.toString().replace(".", ","))
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                if (row != quickValues.last()) {
                    Spacer(modifier = Modifier.height(6.dp))
                }
            }
        }
    }
}

@Composable
fun QuickValueButton(
    value: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isPressed by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .height(44.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable {
                isPressed = true
                onClick()
                GlobalScope.launch {
                    delay(100)
                    isPressed = false
                }
            }
            .background(
                if (isPressed)
                    Color(0xFF2563EB) // Azul mais escuro quando pressionado
                else
                    Color(0xFF3B82F6) // Azul vibrante
            )
            .border(
                width = 1.dp,
                color = Color(0xFF60A5FA).copy(alpha = 0.3f),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = value.toString(),
            style = TextStyle(
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.W600
            )
        )
    }
}

@Composable
fun DescriptionInput(
    description: String,
    onDescriptionChange: (String) -> Unit
) {
    BasicTextField(
        value = description,
        onValueChange = onDescriptionChange,
        textStyle = TextStyle(
            color = TextPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal
        ),
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Color.Black.copy(alpha = 0.3f),
                RoundedCornerShape(12.dp)
            )
            .border(
                1.dp,
                Color.White.copy(alpha = 0.1f),
                RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 14.dp, vertical = 12.dp),
        decorationBox = { innerTextField ->
            if (description.isEmpty()) {
                Text(
                    "Descrição",
                    style = TextStyle(
                        color = TextSecondary.copy(alpha = 0.5f),
                        fontSize = 14.sp
                    )
                )
            }
            innerTextField()
        }
    )
}

@Composable
fun ColumnScope.CategoryGridSection(
    categories: List<CategoryModel>,
    selectedIndex: Int,
    onCategorySelected: (Int) -> Unit,
    onAddCategory: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(bottom = 70.dp)
        ) {
            items(categories) { category ->
                val index = categories.indexOf(category)
                CategoryCircleCard(
                    category = category,
                    isSelected = index == selectedIndex,
                    onClick = { onCategorySelected(index) }
                )
            }

            // Add category button
            item {
                AddCategoryCard(onClick = onAddCategory)
            }
        }

        // Gradient fade at bottom
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(70.dp)
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

@Composable
fun CategoryCircleCard(
    category: CategoryModel,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val borderWidth = if (isSelected) 2.5.dp else 1.5.dp
    val iconAlpha = if (isSelected) 1f else 0.8f
    val textAlpha = if (isSelected) 1f else 0.75f
    val textWeight = if (isSelected) FontWeight.W600 else FontWeight.W500

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .background(
                color = if (isSelected)
                    category.color.copy(alpha = 0.15f)
                else
                    Color.Black.copy(alpha = 0.3f)
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
                imageVector = category.icon,
                contentDescription = category.name,
                tint = category.color.copy(alpha = iconAlpha),
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
fun InsertExpenseButton(
    onClick: () -> Unit,
    enabled: Boolean,
    isExpense: Boolean = true
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isExpense) ErrorRed else SuccessGreen,
            disabledContainerColor = ButtonColor.copy(alpha = 0.5f)
        ),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .padding(horizontal = 16.dp),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.dp
        )
    ) {
        Text(
            if (isExpense) "Inserir Despesa" else "Inserir Receita",
            style = TextStyle(
                color = if (enabled) Color.White else TextSecondary.copy(alpha = 0.5f),
                fontSize = 16.sp,
                fontWeight = FontWeight.W600
            )
        )
    }
}
