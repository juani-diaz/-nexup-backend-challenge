package com.challenge

import com.challenge.repositories.ProductoRepository
import com.challenge.repositories.SupermercadoRepository
import com.challenge.services.CadenaService
import com.challenge.services.SupermercadoService
import jakarta.transaction.Transactional
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.time.LocalTime
import java.time.LocalDateTime
import java.time.DayOfWeek


@SpringBootTest
@ActiveProfiles("test")
@Transactional
class CadenaServiceTest {

    @Autowired
    lateinit var chainService: CadenaService

    @Autowired
    lateinit var supermercadoService: SupermercadoService

    @Autowired
    lateinit var supermercadoRepository: SupermercadoRepository

    @Autowired
    lateinit var productoRepository: ProductoRepository



    @Test
    fun top5ProductosVendidos() {
        val s1 = supermercadoRepository.save(supermercado("Disco"))
        val s2 = supermercadoRepository.save(supermercado("Coto"))

        val p1 = productoRepository.save(producto("Leche", 100f))
        val p2 = productoRepository.save(producto("Pan", 50f))
        val p3 = productoRepository.save(producto("Arroz", 120f))

        s1.modificarStock(p1, 10)
        s1.modificarStock(p2, 10)
        s2.modificarStock(p3, 10)

        supermercadoService.registrarVenta(s1.id, p1.id, 5)
        supermercadoService.registrarVenta(s1.id, p2.id, 2)
        supermercadoService.registrarVenta(s2.id, p3.id, 7)

        val top = chainService.obtenerTop5ProductosMasVendidos()

        assertTrue(top.contains("Arroz: 7"))
        assertTrue(top.contains("Leche: 5"))
        assertTrue(top.contains("Pan: 2"))
    }

    @Test
    fun top5Limite() {
        val s = supermercadoRepository.save(supermercado("Disco"))

        val productos = (1..6).map {
            productoRepository.save(producto("P$it", 10f))
        }

        productos.forEachIndexed { i, p ->
            s.modificarStock(p, 10)
            supermercadoService.registrarVenta(s.id, p.id, i + 1)
        }

        val top = chainService.obtenerTop5ProductosMasVendidos()

        assertTrue(top.split("-").size == 5)
    }

    @Test
    fun supermercadosAbiertosCadena() {
        supermercadoRepository.save(
            supermercado(
                nombre = "A",
                apertura = LocalTime.of(8, 0),
                cierre = LocalTime.of(18, 0),
                dias = mutableSetOf(DayOfWeek.MONDAY)
            )
        )

        supermercadoRepository.save(
            supermercado(
                nombre = "B",
                apertura = LocalTime.of(20, 0),
                cierre = LocalTime.of(23, 0),
                dias = mutableSetOf(DayOfWeek.MONDAY)
            )
        )

        val lunes10 = LocalDateTime.of(2026, 2, 9, 10, 0)

        val abiertos = chainService.obtenerSupermercadosAbiertos(lunes10)

        assertTrue(abiertos.contains("A"))
        assertTrue(!abiertos.contains("B"))
    }


    @Test
    fun ingresosTotales() {
        val s = supermercadoRepository.save(supermercado("Dia"))
        val p = productoRepository.save(producto("Yerba", 500f))

        s.modificarStock(p, 5)
        supermercadoService.registrarVenta(s.id, p.id, 2)

        val total = chainService.obtenerIngresosTotalesCadena()

        assertEquals(1000f, total)
    }

    @Test
    fun supermercadoMasIngresos() {
        val s1 = supermercadoRepository.save(supermercado("Disco"))
        val s2 = supermercadoRepository.save(supermercado("Coto"))

        val p = productoRepository.save(producto("Cafe", 800f))

        s1.modificarStock(p, 5)
        s2.modificarStock(p, 5)

        supermercadoService.registrarVenta(s1.id, p.id, 1)
        supermercadoService.registrarVenta(s2.id, p.id, 3)

        val result = chainService.obtenerSupermercadoConMasIngresos()

        assertTrue(result.contains("Coto"))
    }

    @Test
    fun abiertoPorHorario() {
        val supermercado = supermercadoRepository.save(
            supermercado(
                nombre = "Express",
                apertura = LocalTime.of(9, 0),
                cierre = LocalTime.of(18, 0),
                dias = mutableSetOf(DayOfWeek.MONDAY)
            )
        )

        val lunes10am = LocalDateTime.of(2026, 2, 9, 10, 0)

        assertTrue(supermercado.estaAbierto(lunes10am))
    }
}
