package edu.ucne.francis_castillo_ap2_p2.data.Repository


import edu.ucne.francis_castillo_ap2_p2.data.remote.Resource
import edu.ucne.francis_castillo_ap2_p2.data.remote.GastoApi
import edu.ucne.francis_castillo_ap2_p2.data.remote.dto.GastoDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GastoRepository @Inject constructor(
    private val gastoApi: GastoApi
) {
    fun getGastos(): Flow<Resource<List<GastoDto>>> = flow {
        try {
            emit(Resource.Loading())
            val gastos = gastoApi.getGastos()
            emit(Resource.Success(gastos))
        } catch (e: HttpException) {
            emit(Resource.Error(e.message ?: "Error HTTP"))
        } catch (e: IOException) {
            emit(Resource.Error("Verifica tu conexión a internet"))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Error desconocido"))
        }
    }

    fun getGasto(id: Int): Flow<Resource<GastoDto>> = flow {
        try {
            emit(Resource.Loading())
            val gasto = gastoApi.getGasto(id)
            emit(Resource.Success(gasto))
        } catch (e: HttpException) {
            emit(Resource.Error(e.message ?: "Error HTTP"))
        } catch (e: IOException) {
            emit(Resource.Error("Verifica tu conexión a internet"))
        }
    }

    suspend fun createGasto(gasto: GastoDto): Resource<GastoDto> {
        return try {
            val result = gastoApi.createGasto(gasto)
            Resource.Success(result)
        } catch (e: HttpException) {
            Resource.Error(e.message ?: "Error HTTP")
        } catch (e: IOException) {
            Resource.Error("Verifica tu conexión a internet")
        }
    }

    suspend fun updateGasto(id: Int, gasto: GastoDto): Resource<GastoDto> {
        return try {
            val result = gastoApi.updateGasto(id, gasto)
            Resource.Success(result)
        } catch (e: HttpException) {
            Resource.Error(e.message ?: "Error HTTP")
        } catch (e: IOException) {
            Resource.Error("Verifica tu conexión a internet")
        }
    }

    suspend fun deleteGasto(id: Int): Resource<Unit> {
        return try {
            gastoApi.deleteGasto(id)
            Resource.Success(Unit)
        } catch (e: HttpException) {
            Resource.Error(e.message ?: "Error HTTP")
        } catch (e: IOException) {
            Resource.Error("Verifica tu conexión a internet")
        }
    }
}