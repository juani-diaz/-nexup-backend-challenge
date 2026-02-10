package com.challenge.controller

import com.challenge.services.CadenaService
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@RestController
@RequestMapping("/cadena")
class CadenaRouter(
    private val chainService: CadenaService
) {

    @GetMapping("/productos/top")
    fun top5Productos(): String =
        chainService.obtenerTop5ProductosMasVendidos()

    @GetMapping("/ingresos")
    fun ingresosTotales(): Float =
        chainService.obtenerIngresosTotalesCadena()

    @GetMapping("/supermercado/top")
    fun supermercadoConMasIngresos(): String =
        chainService.obtenerSupermercadoConMasIngresos()

    @GetMapping("/supermercados-abiertos")
    fun supermercadosAbiertos(
        @RequestParam fechaHora: String
    ): String =
        chainService.obtenerSupermercadosAbiertos(
            LocalDateTime.parse(fechaHora)
        )
}
