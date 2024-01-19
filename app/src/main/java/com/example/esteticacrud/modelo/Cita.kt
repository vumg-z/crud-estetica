package com.example.esteticacrud.modelo

import java.time.LocalDateTime

data class Cita(
    val id: Int,
    val cliente: Usuario,
    val servicio: Servicio,
    var fechaHora: LocalDateTime,
    val empleadoAsignado: Empleado
) {
    //  métodos
}
