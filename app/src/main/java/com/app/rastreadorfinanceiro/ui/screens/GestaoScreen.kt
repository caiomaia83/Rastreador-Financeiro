package com.app.rastreadorfinanceiro.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.rastreadorfinanceiro.model.CategoryModel
import com.app.rastreadorfinanceiro.ui.components.CategoryFormDialog
import com.app.rastreadorfinanceiro.ui.theme.*
import com.app.rastreadorfinanceiro.viewmodel.CategoryViewModel

@Composable
fun GestaoScreen(categoryViewModel: CategoryViewModel) {
    val categories = categoryViewModel.categories


    var showAddDialog by remember { mutableStateOf(false) }


    var categoryToEdit by remember { mutableStateOf<CategoryModel?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Minhas Categorias",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    fontSize = 28.sp
                )

                FilledIconButton(
                    onClick = { showAddDialog = true },
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = PrimaryLight
                    ),
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Nova Categoria",
                        tint = TextPrimary
                    )
                }
            }

            if (categories.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        "Nenhuma categoria cadastrada.",
                        color = TextSecondary,
                        fontSize = 16.sp
                    )
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(categories) { category ->
                        CategoryItem(
                            category = category,
                            onEdit = { categoryToEdit = category },
                            onDelete = { categoryViewModel.removeCategory(category) }
                        )
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        CategoryFormDialog(
            title = "Nova Categoria",
            onDismiss = { showAddDialog = false },
            onConfirm = { name, color, limit ->
                categoryViewModel.addCategory(name, color, limit)
                showAddDialog = false
            }
        )
    }


    if (categoryToEdit != null) {
        CategoryFormDialog(
            title = "Editar Categoria",
            initialName = categoryToEdit!!.name,
            initialColor = categoryToEdit!!.color,
            initialLimit = categoryToEdit!!.budgetLimit,
            onDismiss = { categoryToEdit = null },
            onConfirm = { name, color, limit ->
                val updatedCategory = categoryToEdit!!.copy(
                    name = name,
                    color = color,
                    budgetLimit = limit
                )
                categoryViewModel.updateCategory(updatedCategory)
                categoryToEdit = null
            }
        )
    }
}


@Composable
fun CategoryItem(
    category: CategoryModel,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = DarkCard),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(category.color)
                )
                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = category.name,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        fontSize = 18.sp
                    )
                    if (category.budgetLimit != null && category.budgetLimit > 0) {
                        Text(
                            text = "Teto: R$ ${String.format("%.2f", category.budgetLimit)}",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary,
                            fontSize = 14.sp
                        )
                    }
                }
            }

            Row {
                IconButton(onClick = onEdit) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "Editar",
                        tint = PrimaryLight
                    )
                }
                IconButton(onClick = onDelete) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Remover",
                        tint = TextTertiary
                    )
                }
            }
        }
    }
}

