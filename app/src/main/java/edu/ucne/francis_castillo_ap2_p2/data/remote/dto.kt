package edu.ucne.francis_castillo_ap2_p2.data.remote.dto

import com.squareup.moshi.Json

data class GastoDto(
    @Json(name = "idGasto")
    val idGasto: Int = 0,
    @Json(name = "idSuplidor")
    val idSuplidor: Int? = null,
    @Json(name = "ncf")
    val ncf: String? = null,
    @Json(name = "fecha")
    val fecha: String = "",
    @Json(name = "concepto")
    val concepto: String? = null,
    @Json(name = "descuento")
    val descuento: Double? = null,
    @Json(name = "itbis")
    val itbis: Double? = null,
    @Json(name = "monto")
    val monto: Double = 0.0
)