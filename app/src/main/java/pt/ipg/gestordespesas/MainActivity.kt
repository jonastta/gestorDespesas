package pt.ipg.gestordespesas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import pt.ipg.gestordespesas.model.Despesa
import androidx.compose.material.icons.filled.Edit


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GestorDeDespesasApp()
        }
    }
}

@Composable
fun GestorDeDespesasApp() {
    val listaDespesas = remember { mutableStateListOf<Despesa>() }
    var descricao by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }
    var valor by remember { mutableStateOf("") }
    var despesaEmEdicao by remember { mutableStateOf<Despesa?>(null) }

    val total = listaDespesas.sumOf { it.valor }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Adicionar Despesa", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = descricao,
            onValueChange = { descricao = it },
            label = { Text("Descrição") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = categoria,
            onValueChange = { categoria = it },
            label = { Text("Categoria") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = valor,
            onValueChange = { valor = it },
            label = { Text("Valor (€)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        Button(
            onClick = {
                val valorDouble = valor.toDoubleOrNull()
                if (valorDouble != null && descricao.isNotBlank()) {
                    val novaDespesa = Despesa(
                        descricao = descricao,
                        categoria = categoria,
                        valor = valorDouble
                    )

                    if (despesaEmEdicao != null) {
                        val index = listaDespesas.indexOf(despesaEmEdicao)
                        if (index != -1) {
                            listaDespesas[index] = novaDespesa
                        }
                        despesaEmEdicao = null
                    } else {
                        listaDespesas.add(novaDespesa)
                    }

                    descricao = ""
                    categoria = ""
                    valor = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (despesaEmEdicao != null) "Guardar alterações" else "Adicionar")
        }

        Spacer(Modifier.height(16.dp))
        Text("Total gasto: € %.2f".format(total), style = MaterialTheme.typography.bodyLarge)
        Spacer(Modifier.height(16.dp))

        LazyColumn {
            items(listaDespesas) { despesa ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Descrição: ${despesa.descricao}")
                            Text("Categoria: ${despesa.categoria}")
                            Text("Valor: € %.2f".format(despesa.valor))
                        }
                        Row {
                            IconButton(onClick = {
                                descricao = despesa.descricao
                                categoria = despesa.categoria
                                valor = despesa.valor.toString()
                                despesaEmEdicao = despesa
                            }) {
                                Icon(Icons.Default.Edit, contentDescription = "Editar despesa")
                            }
                            IconButton(onClick = {
                                listaDespesas.remove(despesa)
                            }) {
                                Icon(Icons.Default.Delete, contentDescription = "Apagar despesa")
                            }
                        }
                    }

                }
            }
        }
    }
}

