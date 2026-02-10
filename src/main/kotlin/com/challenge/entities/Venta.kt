package com.challenge.entities

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class Venta(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(optional = false)
    var supermercado: Supermercado,

    @ManyToOne(optional = false)
    var producto: Producto,

    var cantidad: Int,

    var total: Float = 0f,

    var fechaVenta: LocalDateTime = LocalDateTime.now()
) {
    init {
        if (cantidad <= 0) {
            throw IllegalArgumentException("La cantidad debe ser mayor a cero")
        }
        total = producto.precio * cantidad
    }
}
