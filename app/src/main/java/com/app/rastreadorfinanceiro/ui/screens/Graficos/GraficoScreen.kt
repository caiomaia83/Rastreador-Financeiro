import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.remember

import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.rastreadorfinanceiro.ui.screens.Graficos.DashboardPieChart
import java.text.NumberFormat
import com.app.rastreadorfinanceiro.viewmodel.DashboardViewModel

import java.util.*

@Composable
fun DashboardScreen(viewModel: DashboardViewModel = viewModel()) {

    val expenses = viewModel.transactions
    val formatter = remember { NumberFormat.getCurrencyInstance(Locale("pt", "BR")) }

    val total = expenses.sumOf { it.amount }
    val count = expenses.size
    val average = if (count > 0) total / count else 0.0

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF000000
            ))
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {


        // Total
        Text(
            text = "Total Gasto: ${formatter.format(total)}",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFE94560)
        )

        Spacer(Modifier.height(16.dp))

        // Card do gráfico
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF7A7A7A)),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                DashboardPieChart(expenses)
            }
        }

        Spacer(Modifier.height(16.dp))

        // Card do resumo
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF7A7A7A)),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {

                Text(
                    text = "Resumo",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(Modifier.height(16.dp))

                SummaryRow("Quantidade:", count.toString())

                Spacer(Modifier.height(8.dp))

                SummaryRow("Média:", formatter.format(average))
            }
        }
    }
}

@Composable
fun SummaryRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = Color.White, fontSize = 18.sp)
        Text(text = value, color = Color(0xFFE94560), fontSize = 18.sp)
    }
}