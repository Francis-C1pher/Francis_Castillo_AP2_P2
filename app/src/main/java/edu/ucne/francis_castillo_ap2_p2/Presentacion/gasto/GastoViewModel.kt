package edu.ucne.francis_castillo_ap2_p2.Presentacion.gasto



import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.francis_castillo_ap2_p2.data.remote.Resource
import edu.ucne.francis_castillo_ap2_p2.data.remote.dto.GastoDto
import edu.ucne.francis_castillo_ap2_p2.data.Repository.GastoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

data class GastoUiState(
    val gastos: List<GastoDto> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedGasto: GastoDto? = null,
    val showBottomSheet: Boolean = false
)

@HiltViewModel
class GastoViewModel @Inject constructor(
    private val repository: GastoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(GastoUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadGastos()
    }

    fun loadGastos() {
        repository.getGastos().onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    _uiState.value = _uiState.value.copy(isLoading = true)
                }
                is Resource.Success -> {
                    _uiState.value = _uiState.value.copy(
                        gastos = result.data ?: emptyList(),
                        isLoading = false,
                        error = null
                    )
                }
                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(
                        error = result.message,
                        isLoading = false
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    fun openBottomSheet(gasto: GastoDto? = null) {
        _uiState.value = _uiState.value.copy(
            showBottomSheet = true,
            selectedGasto = gasto
        )
    }

    fun closeBottomSheet() {
        _uiState.value = _uiState.value.copy(
            showBottomSheet = false,
            selectedGasto = null
        )
    }

    fun saveGasto(gasto: GastoDto) {
        viewModelScope.launch {
            if (gasto.idGasto == 0) {
                repository.createGasto(gasto)
            } else {
                repository.updateGasto(gasto.idGasto, gasto)
            }
            loadGastos()
            closeBottomSheet()
        }
    }

    fun deleteGasto(id: Int) {
        viewModelScope.launch {
            repository.deleteGasto(id)
            loadGastos()
        }
    }
}