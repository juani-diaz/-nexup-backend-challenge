package com.challenge.repositories

import com.challenge.entities.Venta
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface VentaRepository : JpaRepository<Venta, Long>
