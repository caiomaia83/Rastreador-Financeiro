package com.app.rastreadorfinanceiro.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.app.rastreadorfinanceiro.ui.theme.*

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
                                onConfirm(name, selectedColor, limit)
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
