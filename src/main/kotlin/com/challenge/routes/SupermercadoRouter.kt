package com.challenge.routes

import com.challenge.services.SupermercadoService
import com.challenge.DTOs.SupermercadoDTO
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/supermercados")
class SupermercadoRouter(
    private val supermercadoService: SupermercadoService
) {

    @PostMapping("/{supermercadoId}/ventas")
    fun registrarVenta(
        @PathVariable supermercadoId: Long,
        @RequestParam productoId: Long,
        @RequestParam cantidad: Int
    ): Float =
        supermercadoService.registrarVenta(supermercadoId, productoId, cantidad)

    @PostMapping
    fun crear(@RequestBody dto: SupermercadoDTO) =
        supermercadoService.crear(dto)

    @PutMapping("/{id}")
    fun actualizar(@PathVariable id: Long, @RequestBody dto: SupermercadoDTO) =
        supermercadoService.actualizar(id, dto)

    @DeleteMapping("/{id}")
    fun eliminar(@PathVariable id: Long) =
        supermercadoService.eliminar(id)

    @GetMapping
    fun listar() =
        supermercadoService.listar()


    @GetMapping("/{supermercadoId}/productos/{productoId}/cantidad-vendida")
    fun obtenerCantidadVendida(
        @PathVariable supermercadoId: Long,
        @PathVariable productoId: Long
    ): Int =
        supermercadoService.obtenerCantidadVendida(supermercadoId, productoId)

    @GetMapping("/{supermercadoId}/productos/{productoId}/ingresos")
    fun obtenerIngresosPorProducto(
        @PathVariable supermercadoId: Long,
        @PathVariable productoId: Long
    ): Float =
        supermercadoService.obtenerIngresosPorProducto(supermercadoId, productoId)

    @GetMapping("/{supermercadoId}/ingresos")
    fun obtenerIngresosTotales(
        @PathVariable supermercadoId: Long
    ): Float =
        supermercadoService.obtenerIngresosTotales(supermercadoId)
}
