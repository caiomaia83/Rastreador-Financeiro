package com.app.rastreadorfinanceiro.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.app.rastreadorfinanceiro.model.CategoryModel
import com.app.rastreadorfinanceiro.viewmodel.CategoryViewModel

@Composable
fun GestaoScreen(categoryViewModel: CategoryViewModel) {
    val categories = categoryViewModel.categories


    var showAddDialog by remember { mutableStateOf(false) }


    var categoryToEdit by remember { mutableStateOf<CategoryModel?>(null) }

    Scaffold(

    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Minhas Categorias",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )

                // Botão de Adicionar no Canto Superior Direito
                FilledIconButton(
                    onClick = { showAddDialog = true },
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Nova Categoria")
                }
            }
            // -----------------------------------

            if (categories.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Nenhuma categoria cadastrada.")
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
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
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(category.color)
                )
                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = category.name,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )
                    if (category.budgetLimit != null && category.budgetLimit > 0) {
                        Text(
                            text = "Teto: R$ ${String.format("%.2f", category.budgetLimit)}",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.Gray
                        )
                    }
                }
            }

            Row {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, contentDescription = "Editar", tint = MaterialTheme.colorScheme.primary)
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Remover", tint = Color.Gray)
                }
            }
        }
    }
}

@Composable
fun CategoryFormDialog(
    title: String,
    initialName: String = "",
    initialColor: Color = Color(0xFFEF5350),
    initialLimit: Double? = null,
    onDismiss: () -> Unit,
    onConfirm: (String, Color, Double?) -> Unit
) {
    var name by remember { mutableStateOf(initialName) }
    var limitText by remember { mutableStateOf(initialLimit?.toString() ?: "") }
    var selectedColor by remember { mutableStateOf(initialColor) }

    val colors = listOf(
        Color(0xFFEF5350), Color(0xFF42A5F5), Color(0xFFFFCA28), Color(0xFF66BB6A),
        Color(0xFFAB47BC), Color(0xFF8D6E63), Color(0xFFFFA726), Color(0xFF78909C)
    )

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(title, style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nome da Categoria") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = limitText,
                    onValueChange = { limitText = it },
                    label = { Text("Teto de Gastos (Opcional)") },
                    placeholder = { Text("Ex: 500.00") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))
                Text("Escolha uma cor:", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    colors.take(4).forEach { color ->
                        ColorCircle(color = color, isSelected = color == selectedColor) { selectedColor = color }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    colors.takeLast(4).forEach { color ->
                        ColorCircle(color = color, isSelected = color == selectedColor) { selectedColor = color }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) { Text("Cancelar") }
                    Button(
                        onClick = {
                            if (name.isNotEmpty()) {
                                val limit = limitText.toDoubleOrNull()
                                onConfirm(name, selectedColor, limit)
                            }
                        },
                        enabled = name.isNotEmpty()
                    ) { Text("Salvar") }
                }
            }
        }
    }
}

@Composable
fun ColorCircle(color: Color, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(color)
            .clickable(onClick = onClick)
            .then(
                if (isSelected) Modifier.border(2.dp, MaterialTheme.colorScheme.onSurface, CircleShape)
                else Modifier
            )
    )
}