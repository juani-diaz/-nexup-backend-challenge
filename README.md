# Nexup Backend Challenge – Kotlin + Spring Boot

Este repositorio contiene la solución al **Nexup Backend Challenge**, implementada en **Kotlin** utilizando **Spring Boot**, **JPA/Hibernate** y una arquitectura simple orientada a servicios.

El objetivo del proyecto es modelar una **cadena de supermercados**, permitiendo registrar ventas, manejar stock y obtener métricas agregadas tanto por supermercado como a nivel cadena.

---

## Requisitos

* Java 17
* Gradle
* Curl (o Postman)

---

## Cómo ejecutar el proyecto

Desde la raíz del proyecto:

```bash
./gradlew clean bootRun
```

La aplicación quedará disponible en:

```
http://localhost:8083
```

---

## Funcionalidades implementadas

### Por supermercado

* Registrar una venta de un producto
* Obtener cantidad vendida de un producto
* Obtener ingresos por producto
* Obtener ingresos totales

### Por cadena de supermercados

* Obtener los 5 productos más vendidos
* Obtener ingresos totales de la cadena
* Obtener el supermercado con mayores ingresos

### Objetivo opcional

* Manejo de horario de apertura/cierre y días de apertura
* Obtener supermercados abiertos dado un día y horario

---

## Ejemplos de uso (curl)

### Crear productos

```bash
curl -X POST http://localhost:8083/productos \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Leche","precio":120.5}'
```

```bash
curl -X POST http://localhost:8083/productos \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Pan","precio":80.0}'
```

---

### Crear supermercados

```bash
curl -X POST http://localhost:8083/supermercados \
  -H "Content-Type: application/json" \
  -d '{
    "nombre":"Super Norte",
    "horarioApertura":"08:00",
    "horarioCierre":"22:00",
    "diasAbierto":["MONDAY","TUESDAY","WEDNESDAY","THURSDAY","FRIDAY"]
  }'
```

---

### Cargar stock

```bash
curl -X POST http://localhost:8083/stock \
  -H "Content-Type: application/json" \
  -d '{
    "supermercadoId":1,
    "productoId":1,
    "cantidadDisponible":100
  }'
```

---

### Registrar ventas

```bash
curl -X POST "http://localhost:8083/supermercados/1/ventas?productoId=1&cantidad=5"
```

La respuesta corresponde al **total de la venta**.

---

### Métricas por supermercado

**Cantidad vendida de un producto**

```bash
curl http://localhost:8083/supermercados/1/productos/1/cantidad-vendida
```

**Ingresos por producto**

```bash
curl http://localhost:8083/supermercados/1/productos/1/ingresos
```

**Ingresos totales del supermercado**

```bash
curl http://localhost:8083/supermercados/1/ingresos
```

---

### Métricas de la cadena

**Top 5 productos más vendidos**

```bash
curl http://localhost:8083/cadena/productos/top
```

Formato de respuesta:

```
<nombre_producto>: cantidad - <nombre_producto>: cantidad
```

**Ingresos totales de la cadena**

```bash
curl http://localhost:8083/cadena/ingresos
```

**Supermercado con mayores ingresos**

```bash
curl http://localhost:8083/cadena/supermercado/top
```

Formato:

```
<nombre_supermercado> (<id>). Ingresos totales: <monto>
```

---

### Supermercados abiertos (objetivo opcional)

```bash
curl "http://localhost:8083/cadena/supermercados-abiertos?fechaHora=2026-02-10T10:30"
```

Respuesta:

```
Super Norte (1), Super Sur (2)
```

---

## Notas

* Se utiliza **JPA/Hibernate** con relaciones bidireccionales
* Se evita serialización cíclica usando `@JsonIgnore`
* Los DTOs separan la API del modelo de dominio
* El proyecto incluye manejo de errores básicos y validaciones
* Se utilizan annotaciones JPA aunque se maneja todo en memoria para simplicidad
* 
