package com.challenge

import com.challenge.repositories.ProductoRepository
import com.challenge.repositories.SupermercadoRepository
import com.challenge.repositories.VentaRepository
import com.challenge.services.SupermercadoService
import jakarta.transaction.Transactional
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.crossstore.ChangeSetPersister
import org.springframework.test.context.ActiveProfiles
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.LocalTime

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class SupermercadoServiceTest {

    @Autowired
    lateinit var supermercadoService: SupermercadoService

    @Autowired
    lateinit var supermercadoRepository: SupermercadoRepository

    @Autowired
    lateinit var productoRepository: ProductoRepository

    @Autowired
    lateinit var ventaRepository: VentaRepository



    @Test
    fun registrarVenta() {
        val supermercado = supermercadoRepository.save(supermercado("Disco"))
        val producto = productoRepository.save(producto("Leche", 100f))

        supermercado.modificarStock(producto, 10)

        val total = supermercadoService.registrarVenta(
            supermercado.id,
            producto.id,
            2
        )

        assertEquals(200f, total)
        assertEquals(1, ventaRepository.count())
    }


    @Test
    fun noVenderProductoInexistente() {
        val supermercado = supermercadoRepository.save(supermercado("Disco"))

        assertThrows<ChangeSetPersister.NotFoundException> {
            supermercadoService.registrarVenta(
                supermercado.id,
                productoId = 999,
                cantidad = 1
            )
        }
    }

    @Test
    fun noVenderSinStock() {
        val supermercado = supermercadoRepository.save(supermercado("Coto"))
        val producto = productoRepository.save(producto("Pan", 50f))

        assertThrows<IllegalStateException> {
            supermercadoService.registrarVenta(supermercado.id, producto.id, 1)
        }
    }

    @Test
    fun cantidadVendidaXProducto() {
        val supermercado = supermercadoRepository.save(supermercado("Dia"))
        val producto = productoRepository.save(producto("Arroz", 120f))

        supermercado.modificarStock(producto, 10)

        supermercadoService.registrarVenta(supermercado.id, producto.id, 3)
        supermercadoService.registrarVenta(supermercado.id, producto.id, 2)

        val cantidadVendida =
            supermercadoService.obtenerCantidadVendida(supermercado.id, producto.id)

        assertEquals(5, cantidadVendida)
    }

    @Test
    fun ingresosXProducto() {
        val supermercado = supermercadoRepository.save(supermercado("Carrefour"))
        val producto = productoRepository.save(producto("Aceite", 200f))

        supermercado.modificarStock(producto, 5)
        supermercadoService.registrarVenta(supermercado.id, producto.id, 2)

        val ingresos =
            supermercadoService.obtenerIngresosPorProducto(supermercado.id, producto.id)

        assertEquals(400f, ingresos)
    }

    @Test
    fun ingresosPorSupermercado() {
        val supermercado = supermercadoRepository.save(supermercado("Jumbo"))
        val p1 = productoRepository.save(producto("Queso", 300f))
        val p2 = productoRepository.save(producto("Jamon", 400f))

        supermercado.modificarStock(p1, 5)
        supermercado.modificarStock(p2, 5)

        supermercadoService.registrarVenta(supermercado.id, p1.id, 1)
        supermercadoService.registrarVenta(supermercado.id, p2.id, 2)

        val total = supermercadoService.obtenerIngresosTotales(supermercado.id)

        assertEquals(1100f, total)
    }
    @Test
    fun noVenderSupermercadoCerrado() {
        val supermercado = supermercadoRepository.save(
            supermercado(
                nombre = "Cerrado",
                apertura = LocalTime.of(9, 0),
                cierre = LocalTime.of(10, 0),
                dias = mutableSetOf(DayOfWeek.MONDAY) //o poner un dia que no sea el actual
            )
        )
        val producto = productoRepository.save(producto("Leche", 100f))

        supermercado.modificarStock(producto, 5)


        assertThrows<IllegalStateException> {
            supermercadoService.registrarVenta(supermercado.id, producto.id, 1)
        }
    }

    @Test
    fun noVenderCantidadInvalida() {
        val supermercado = supermercadoRepository.save(supermercado("Dia"))
        val producto = productoRepository.save(producto("Pan", 50f))

        supermercado.modificarStock(producto, 10)

        assertThrows<IllegalArgumentException> {
            supermercadoService.registrarVenta(supermercado.id, producto.id, 0)
        }
    }



}
