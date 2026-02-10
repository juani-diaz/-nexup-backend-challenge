package com.challenge.DTOs



data class StockDTO(
    val id: Long?=null,

    var supermercadoId: Long?=null,

    var productoId: Long?=null,

    var cantidadDisponible: Int?=null
)
