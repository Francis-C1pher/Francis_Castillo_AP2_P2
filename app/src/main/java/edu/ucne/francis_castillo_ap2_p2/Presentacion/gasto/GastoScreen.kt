package edu.ucne.francis_castillo_ap2_p2.Presentacion.gasto


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import edu.ucne.francis_castillo_ap2_p2.data.remote.dto.GastoDto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GastoScreen(
    viewModel: GastoViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val sheetState = rememberModalBottomSheetState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gastos") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.openBottomSheet() }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                uiState.error != null -> {
                    Text(
                        text = uiState.error ?: "",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(uiState.gastos) { gasto ->
                            GastoItem(
                                gasto = gasto,
                                onEdit = { viewModel.openBottomSheet(gasto) },
                                onDelete = { viewModel.deleteGasto(gasto.idGasto) }
                            )
                        }
                    }
                }
            }
        }

        if (uiState.showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { viewModel.closeBottomSheet() },
                sheetState = sheetState
            ) {
                GastoBottomSheet(
                    gasto = uiState.selectedGasto,
                    onSave = { viewModel.saveGasto(it) },
                    onDismiss = { viewModel.closeBottomSheet() }
                )
            }
        }
    }
}

@Composable
fun GastoItem(
    gasto: GastoDto,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = gasto.concepto ?: "Sin concepto",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Monto: $${gasto.monto}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Fecha: ${gasto.fecha}",
                    style = MaterialTheme.typography.bodySmall
                )
                gasto.ncf?.let {
                    Text(
                        text = "NCF: $it",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
            Row {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, contentDescription = "Editar")
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                }
            }
        }
    }
}

@Composable
fun GastoBottomSheet(
    gasto: GastoDto?,
    onSave: (GastoDto) -> Unit,
    onDismiss: () -> Unit
) {
    var idSuplidor by remember { mutableStateOf(gasto?.idSuplidor?.toString() ?: "") }
    var ncf by remember { mutableStateOf(gasto?.ncf ?: "") }
    var fecha by remember { mutableStateOf(gasto?.fecha ?: "") }
    var concepto by remember { mutableStateOf(gasto?.concepto ?: "") }
    var descuento by remember { mutableStateOf(gasto?.descuento?.toString() ?: "") }
    var itbis by remember { mutableStateOf(gasto?.itbis?.toString() ?: "") }
    var monto by remember { mutableStateOf(gasto?.monto?.toString() ?: "") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = if (gasto == null) "Nuevo Gasto" else "Editar Gasto",
            style = MaterialTheme.typography.titleLarge
        )

        OutlinedTextField(
            value = idSuplidor,
            onValueChange = { idSuplidor = it },
            label = { Text("ID Suplidor") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = ncf,
            onValueChange = { ncf = it },
            label = { Text("NCF") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = fecha,
            onValueChange = { fecha = it },
            label = { Text("Fecha (YYYY-MM-DD)") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = concepto,
            onValueChange = { concepto = it },
            label = { Text("Concepto") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = descuento,
            onValueChange = { descuento = it },
            label = { Text("Descuento") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = itbis,
            onValueChange = { itbis = it },
            label = { Text("ITBIS") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = monto,
            onValueChange = { monto = it },
            label = { Text("Monto") },
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = onDismiss,
                modifier = Modifier.weight(1f)
            ) {
                Text("Cancelar")
            }
            Button(
                onClick = {
                    val nuevoGasto = GastoDto(
                        idGasto = gasto?.idGasto ?: 0,
                        idSuplidor = idSuplidor.toIntOrNull(),
                        ncf = ncf.ifEmpty { null },
                        fecha = fecha,
                        concepto = concepto.ifEmpty { null },
                        descuento = descuento.toDoubleOrNull(),
                        itbis = itbis.toDoubleOrNull(),
                        monto = monto.toDoubleOrNull() ?: 0.0
                    )
                    onSave(nuevoGasto)
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Guardar")
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}