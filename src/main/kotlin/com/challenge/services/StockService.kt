package com.challenge.services

import com.challenge.DTOs.StockDTO
import com.challenge.entities.Stock
import com.challenge.repositories.ProductoRepository
import com.challenge.repositories.StockRepository
import com.challenge.repositories.SupermercadoRepository
import org.springframework.stereotype.Service
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException

@Service
class StockService(
    private val supermercadoRepository: SupermercadoRepository,
    private val productoRepository: ProductoRepository,
    private val stockRepository: StockRepository
) {

    fun cargarStock(dto: StockDTO): Stock {
        val supermercado = supermercadoRepository.findById(dto.supermercadoId)
            .orElseThrow { NotFoundException() }

        val producto = productoRepository.findById(dto.productoId)
            .orElseThrow { NotFoundException() }
        val cantidad = dto.cantidadDisponible
            ?: throw NotFoundException()

        supermercado.modificarStock(producto, cantidad)
        supermercadoRepository.save(supermercado)

        return supermercado.stock.first {
            it.producto.id == producto.id
        }
    }

    fun eliminarStock(id: Long) =
        stockRepository.deleteById(id)

    fun listar(): List<Stock> =
        stockRepository.findAll()
}
