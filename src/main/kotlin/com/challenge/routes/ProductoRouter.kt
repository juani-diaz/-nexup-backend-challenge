package com.challenge.routes

import com.challenge.DTOs.ProductoDTO
import com.challenge.services.ProductoService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/productos")
class ProductoRouter(
    private val service: ProductoService
) {

    @PostMapping
    fun crear(@RequestBody dto: ProductoDTO) =
        service.crear(dto)

    @PutMapping("/{id}")
    fun actualizar(@PathVariable id: Long, @RequestBody dto: ProductoDTO) =
        service.actualizar(id, dto)

    @DeleteMapping("/{id}")
    fun eliminar(@PathVariable id: Long) =
        service.eliminar(id)

    @GetMapping
    fun listar() =
        service.listar()
}
