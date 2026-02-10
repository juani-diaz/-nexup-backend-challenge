package com.challenge.services

import com.challenge.DTOs.SupermercadoDTO
import com.challenge.entities.Supermercado
import com.challenge.repositories.ProductoRepository
import com.challenge.repositories.SupermercadoRepository
import com.challenge.repositories.VentaRepository
import org.springframework.stereotype.Service
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException


@Service
class SupermercadoService(
    private val supermercadoRepository: SupermercadoRepository,
    private val productoRepository: ProductoRepository,
    private val ventaRepository: VentaRepository
) {

    fun registrarVenta(
        supermercadoId: Long,
        productoId: Long,
        cantidad: Int
    ): Float {

        val supermercado = supermercadoRepository.findById(supermercadoId)
            .orElseThrow { NotFoundException() }

        val producto = productoRepository.findById(productoId)
            .orElseThrow { NotFoundException() }

        if (!supermercado.estaAbierto()) {
            throw IllegalStateException("El supermercado está cerrado")
        }

        val venta = supermercado.registrarVenta(producto, cantidad)
        ventaRepository.save(venta)

        return venta.total
    }


        fun crear(dto: SupermercadoDTO): Supermercado =
            supermercadoRepository.save(
                Supermercado(
                    nombre = dto.nombre!!,
                    horarioApertura = dto.horarioApertura!!,
                    horarioCierre = dto.horarioCierre!!,
                    diasAbierto = dto.diasAbierto!!
                )
            )

        fun actualizar(id: Long, dto: SupermercadoDTO): Supermercado {
            val supermercado = supermercadoRepository.findById(id)
                .orElseThrow { NotFoundException() }

            dto.nombre?.let { supermercado.nombre = it }
            dto.horarioApertura?.let { supermercado.horarioApertura = it }
            dto.horarioCierre?.let { supermercado.horarioCierre = it }
            dto.diasAbierto?.let { supermercado.diasAbierto = it }

            return supermercadoRepository.save(supermercado)
        }

        fun eliminar(id: Long) =
            supermercadoRepository.deleteById(id)

        fun listar(): List<Supermercado> =
            supermercadoRepository.findAll()



    fun obtenerCantidadVendida(
        supermercadoId: Long,
        productoId: Long
    ): Int =
        ventaRepository.findAll()
            .filter {
                it.supermercado.id == supermercadoId &&
                        it.producto.id == productoId
            }
            .sumOf { it.cantidad }

    fun obtenerIngresosPorProducto(
        supermercadoId: Long,
        productoId: Long
    ): Float =
        ventaRepository.findAll()
            .filter {
                it.supermercado.id == supermercadoId &&
                        it.producto.id == productoId
            }
            .map { it.total }.sum()

    fun obtenerIngresosTotales(supermercadoId: Long): Float =
        ventaRepository.findAll()
            .filter { it.supermercado.id == supermercadoId }
            .map { it.total }.sum()
}
