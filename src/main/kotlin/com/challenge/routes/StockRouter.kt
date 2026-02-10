package com.challenge.routes

import com.challenge.DTOs.StockDTO
import com.challenge.services.StockService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/stock")
class StockRouter(
    private val service: StockService
) {

    @PostMapping
    fun cargar(@RequestBody dto: StockDTO) =
        service.cargarStock(dto)

    @DeleteMapping("/{id}")
    fun eliminar(@PathVariable id: Long) =
        service.eliminarStock(id)

    @GetMapping
    fun listar() =
        service.listar()
}

