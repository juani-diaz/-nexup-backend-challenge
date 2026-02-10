package com.challenge.entities

import jakarta.persistence.*
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.LocalTime

@Entity
class Supermercado(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    var nombre: String,

    @OneToMany(mappedBy = "supermercado", cascade = [CascadeType.ALL], orphanRemoval = true)
    val stock: MutableList<Stock> = mutableListOf(),

    var horarioApertura: LocalTime,
    var horarioCierre: LocalTime,

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
        name = "supermercado_dias_abierto",
        joinColumns = [JoinColumn(name = "supermercado_id")]
    )
    @Column(name = "dia")
    @Enumerated(EnumType.STRING)
    @JvmSuppressWildcards
    var diasAbierto: MutableSet<DayOfWeek>


) {


    fun modificarStock(producto: Producto, monto: Int) {
        val stockProducto = stock.find { it.producto.id == producto.id }

        if (stockProducto != null) {
            val nuevaCantidad = stockProducto.cantidadDisponible + monto
            if (nuevaCantidad < 0) {
                throw IllegalStateException("Stock insuficiente para el producto ${producto.nombre}")
            }
            stockProducto.cantidadDisponible = nuevaCantidad
        } else {
            stock.add(
                Stock(
                    supermercado = this,
                    producto = producto,
                    cantidadDisponible = monto
                )
            )
        }
    }



    fun registrarVenta(productoComprado: Producto, cantidad: Int): Venta {
        if (cantidad <= 0) {
            throw IllegalArgumentException("La cantidad debe ser mayor a cero")
        }

        val stockActual =  stock.find { it.producto.id == productoComprado.id }

        if (stockActual==null || stockActual.cantidadDisponible < cantidad) {
            throw IllegalStateException("Stock insuficiente")
        }

        modificarStock(productoComprado, -cantidad)

        return Venta(
            supermercado = this,
            producto = productoComprado,
            cantidad = cantidad
        )
    }


    fun estaAbierto(fechaHoraActual: LocalDateTime = LocalDateTime.now()): Boolean {
        val dia = fechaHoraActual.dayOfWeek
        val hora = fechaHoraActual.toLocalTime()

        val esDiaValido = diasAbierto.contains(dia)
        val esHorarioValido = !hora.isBefore(horarioApertura) &&
                !hora.isAfter(horarioCierre)

        return esDiaValido && esHorarioValido
    }
}
