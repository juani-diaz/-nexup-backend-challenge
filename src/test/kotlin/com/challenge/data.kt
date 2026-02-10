package com.challenge

import com.challenge.entities.Producto
import com.challenge.entities.Supermercado
import java.time.DayOfWeek
import java.time.LocalTime

fun supermercado(
    nombre: String,
    apertura: LocalTime = LocalTime.of(8, 0),
    cierre: LocalTime = LocalTime.of(22, 0),
    dias: MutableSet<DayOfWeek> = DayOfWeek.values().toMutableSet()
): Supermercado =
    Supermercado(
        nombre = nombre,
        horarioApertura = apertura,
        horarioCierre = cierre,
        diasAbierto = dias
    )

fun producto(
    nombre: String,
    precio: Float
): Producto =
    Producto(
        nombre = nombre,
        precio = precio
    )
