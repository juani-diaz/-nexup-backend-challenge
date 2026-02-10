package com.challenge.entities

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*

@Entity
class Stock(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(optional = false)
    @JsonIgnore
    var supermercado: Supermercado,

    @ManyToOne(optional = false)
    @JsonIgnore
    var producto: Producto,

    var cantidadDisponible: Int
)
