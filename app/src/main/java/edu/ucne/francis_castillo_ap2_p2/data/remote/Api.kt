package edu.ucne.francis_castillo_ap2_p2.data.remote

import edu.ucne.francis_castillo_ap2_p2.data.remote.dto.GastoDto
import retrofit2.http.*

interface GastoApi {
    @GET("api/Gastos")
    suspend fun getGastos(): List<GastoDto>

    @GET("api/Gastos/{id}")
    suspend fun getGasto(@Path("id") id: Int): GastoDto

    @POST("api/Gastos")
    suspend fun createGasto(@Body gasto: GastoDto): GastoDto

    @PUT("api/Gastos/{id}")
    suspend fun updateGasto(@Path("id") id: Int, @Body gasto: GastoDto): GastoDto

    @DELETE("api/Gastos/{id}")
    suspend fun deleteGasto(@Path("id") id: Int)
}