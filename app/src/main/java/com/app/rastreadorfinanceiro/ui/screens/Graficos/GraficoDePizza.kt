package com.app.rastreadorfinanceiro.ui.screens.Graficos

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.rastreadorfinanceiro.model.ExpenseModel
import com.app.rastreadorfinanceiro.model.PieChartDataItem
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun DashboardPieChart(expenses: List<ExpenseModel>) {

    val items = expenses
        .groupBy { it.category }
        .map { (category, list) ->
            PieChartDataItem(
                label = category.name,
                value = list.sumOf { it.amount },
                color = category.color.hashCode()
            )
        }
        .sortedByDescending { it.value }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {

        if (items.isEmpty()) {
            PlaceholderChart()
        } else {
            PieChart(items)
            Spacer(modifier = Modifier.height(20.dp))
            PieLegend(items)
        }
    }
}

@Composable
private fun PieChart(items: List<PieChartDataItem>) {

    val total = items.sumOf { it.value }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(350.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {

            var startAngle = -90f
            val radius = size.minDimension / 2.5f
            val center = Offset(size.width / 2, size.height / 2)

            items.forEach { item ->

                val sweep = ((item.value / total) * 360f).toFloat()

                // Desenha fatia
                drawArc(
                    color = Color(item.color),
                    startAngle = startAngle,
                    sweepAngle = sweep,
                    useCenter = true,
                    topLeft = Offset(center.x - radius, center.y - radius),
                    size = Size(radius * 2, radius * 2)
                )

                // Porcentagem (desenho usando canvas nativa para centralizar texto)
                val angle = startAngle + sweep / 2
                val rad = Math.toRadians(angle.toDouble())

                val tx = center.x + (radius * 0.75f) * cos(rad).toFloat()
                val ty = center.y + (radius * 0.75f) * sin(rad).toFloat()

                drawIntoCanvas { canvas ->
                    val percent = String.format("%.1f%%", (item.value / total) * 100)
                    val paint = android.graphics.Paint().apply {
                        color = android.graphics.Color.WHITE
                        textSize = 32f
                        textAlign = android.graphics.Paint.Align.CENTER
                        isFakeBoldText = true
                    }
                    canvas.nativeCanvas.drawText(percent, tx, ty, paint)
                }

                startAngle += sweep
            }

            // Buraco central
            drawCircle(
                color = Color.White,
                radius = radius * 0.5f,
                center = center
            )
        }
    }
}

@Composable
private fun PieLegend(items: List<PieChartDataItem>) {

    val total = items.sumOf { it.value }

    Column(modifier = Modifier.fillMaxWidth()) {
        items.forEach { item ->

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                horizontalArrangement = Arrangement.Start
            ) {

                Box(
                    modifier = Modifier
                        .size(18.dp)
                        .background(Color(item.color), CircleShape)
                )

                Spacer(modifier = Modifier.width(12.dp))

                val percent = String.format("%.1f%%", (item.value / total) * 100)

                Text(
                    text = "${item.label} — $percent",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
private fun PlaceholderChart() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(40.dp),
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Sem dados",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Adicione gastos para visualizar o gráfico",
            fontSize = 16.sp,
            color = Color.LightGray
        )
    }
}