package com.challenge.services

import com.challenge.entities.Supermercado
import com.challenge.repositories.SupermercadoRepository
import com.challenge.repositories.VentaRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime


@Service
class CadenaService(
    private val supermercadoRepository: SupermercadoRepository,
    private val ventaRepository: VentaRepository
) {


    fun obtenerTop5ProductosMasVendidos(): String {
        val ventas = ventaRepository.findAll()

        return ventas
            .groupBy { it.producto }
            .mapValues { entry -> entry.value.sumOf { it.cantidad } }
            .entries
            .sortedByDescending { it.value }
            .take(5)
            .joinToString(" - ") {
                "${it.key.nombre}: ${it.value}"
            }
    }


    fun obtenerIngresosTotalesCadena(): Float =
        ventaRepository.findAll()
            .map { it.total }
            .sum()

    fun obtenerSupermercadoConMasIngresos(): String {
        val ventasPorSupermercado = ventaRepository.findAll()
            .groupBy { it.supermercado }
            .mapValues { entry ->
                entry.value.map { it.total }.sum()
            }

        val (supermercado, ingresos) = ventasPorSupermercado
            .maxByOrNull { it.value }
            ?: throw IllegalStateException("No hay ventas registradas")

        return "${supermercado.nombre} (${supermercado.id}). Ingresos totales: $ingresos"
    }



    fun obtenerSupermercadosAbiertos(
        fechaHora: LocalDateTime
    ): String {
        return supermercadoRepository.findAll()
            .filter { it.estaAbierto(fechaHora) }
            .joinToString(", ") {
                "${it.nombre} (${it.id})"
            }
    }


}
