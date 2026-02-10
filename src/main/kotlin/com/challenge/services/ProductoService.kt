package com.challenge.services

import com.challenge.DTOs.ProductoDTO
import com.challenge.entities.Producto
import com.challenge.repositories.ProductoRepository
import org.springframework.stereotype.Service
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException

@Service
class ProductoService(
    private val productoRepository: ProductoRepository
) {

    fun crear(dto: ProductoDTO): Producto =
        productoRepository.save(
            Producto(
                nombre = dto.nombre!!,
                precio = dto.precio!!
            )
        )

    fun actualizar(id: Long, dto: ProductoDTO): Producto {
        val producto = productoRepository.findById(id)
            .orElseThrow { NotFoundException() }

        dto.nombre?.let { producto.nombre = it }
        dto.precio?.let { producto.precio = it }

        return productoRepository.save(producto)
    }

    fun eliminar(id: Long) =
        productoRepository.deleteById(id)

    fun listar(): List<Producto> =
        productoRepository.findAll()
}
