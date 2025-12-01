package com.app.rastreadorfinanceiro.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.app.rastreadorfinanceiro.ui.theme.*

// Data class para ícones de categoria
data class CategoryIcon(val icon: ImageVector, val label: String)

// Lista de ícones disponíveis para categorias
val categoryIcons = listOf(
    CategoryIcon(Icons.Default.Home, "Casa"),
    CategoryIcon(Icons.Default.ShoppingCart, "Compras"),
    CategoryIcon(Icons.Default.Place, "Alimentação"),
    CategoryIcon(Icons.Default.Phone, "Transporte"),
    CategoryIcon(Icons.Default.Call, "Telefone"),
    CategoryIcon(Icons.Default.Favorite, "Saúde"),
    CategoryIcon(Icons.Default.FavoriteBorder, "Lazer"),
    CategoryIcon(Icons.Default.Star, "Favorito"),
    CategoryIcon(Icons.Default.Settings, "Outros"),
    CategoryIcon(Icons.Default.Build, "Ferramentas"),
    CategoryIcon(Icons.Default.Person, "Pessoal"),
    CategoryIcon(Icons.Default.AccountCircle, "Conta"),
    CategoryIcon(Icons.Default.AccountBox, "Mercado"),
    CategoryIcon(Icons.Default.DateRange, "Calendário"),
    CategoryIcon(Icons.Default.Info, "Info"),
    CategoryIcon(Icons.Default.MoreVert, "Mais"),
    CategoryIcon(Icons.Default.Email, "Email"),
    CategoryIcon(Icons.Default.Notifications, "Notificações"),
    CategoryIcon(Icons.Default.Lock, "Segurança"),
    CategoryIcon(Icons.Default.Add, "Adicionar"),
    CategoryIcon(Icons.Default.Edit, "Editar"),
    CategoryIcon(Icons.Default.Delete, "Deletar"),
    CategoryIcon(Icons.Default.CheckCircle, "Concluído"),
    CategoryIcon(Icons.Default.Check, "Verificar")
)

@Composable
fun CategoryFormDialog(
    title: String,
    initialName: String = "",
    initialColor: Color = Color(0xFFEF5350),
    initialLimit: Double? = null,
    onDismiss: () -> Unit,
    onConfirm: (String, Color, Double?, ImageVector) -> Unit
) {
    var name by remember { mutableStateOf(initialName) }
    var limitText by remember { mutableStateOf(initialLimit?.toString() ?: "") }
    var selectedColor by remember { mutableStateOf(initialColor) }
    var selectedIcon by remember { mutableStateOf(categoryIcons[0].icon) }
    var showIconPicker by remember { mutableStateOf(false) }

    val colors = listOf(
        Color(0xFFEF5350), Color(0xFF42A5F5), Color(0xFFFFCA28), Color(0xFF66BB6A),
        Color(0xFFAB47BC), Color(0xFF8D6E63), Color(0xFFFFA726), Color(0xFF78909C)
    )

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = DarkCard)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nome da Categoria", color = TextSecondary) },
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

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = limitText,
                    onValueChange = { limitText = it },
                    label = { Text("Teto de Gastos (Opcional)", color = TextSecondary) },
                    placeholder = { Text("Ex: 500.00", color = TextTertiary) },
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

                Spacer(modifier = Modifier.height(16.dp))

                // Icon Selector
                Text(
                    "Escolha um ícone:",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { showIconPicker = true }
                        .background(Color.Black.copy(alpha = 0.3f))
                        .border(
                            1.dp,
                            selectedColor.copy(alpha = 0.5f),
                            RoundedCornerShape(12.dp)
                        )
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = selectedIcon,
                            contentDescription = "Ícone selecionado",
                            tint = selectedColor,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            "Toque para escolher",
                            color = TextSecondary,
                            fontSize = 14.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "Escolha uma cor:",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextPrimary
                )
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
                    TextButton(onClick = onDismiss) {
                        Text("Cancelar", color = TextSecondary)
                    }
                    Spacer(Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (name.isNotEmpty()) {
                                val limit = limitText.toDoubleOrNull()
                                onConfirm(name, selectedColor, limit, selectedIcon)
                            }
                        },
                        enabled = name.isNotEmpty(),
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

    // Icon Picker Dialog
    if (showIconPicker) {
        Dialog(onDismissRequest = { showIconPicker = false }) {
            Card(
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(500.dp),
                colors = CardDefaults.cardColors(containerColor = DarkCard)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        "Escolha um Ícone",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(4),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        items(categoryIcons) { iconData ->
                            IconOption(
                                icon = iconData.icon,
                                label = iconData.label,
                                color = selectedColor,
                                isSelected = iconData.icon == selectedIcon,
                                onClick = {
                                    selectedIcon = iconData.icon
                                    showIconPicker = false
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    TextButton(
                        onClick = { showIconPicker = false },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Cancelar", color = TextSecondary)
                    }
                }
            }
        }
    }
}

@Composable
fun IconOption(
    icon: ImageVector,
    label: String,
    color: Color,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .background(
                if (isSelected)
                    color.copy(alpha = 0.2f)
                else
                    Color.Black.copy(alpha = 0.3f)
            )
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) color else TextSecondary.copy(alpha = 0.2f),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (isSelected) color else TextSecondary,
            modifier = Modifier.size(28.dp)
        )
    }
}

@Composable
fun ColorCircle(color: Color, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(color)
            .clickable(onClick = onClick)
            .then(
                if (isSelected) Modifier.border(3.dp, TextPrimary, CircleShape)
                else Modifier.border(1.dp, TextTertiary.copy(alpha = 0.3f), CircleShape)
            )
    )
}
