package com.example.esteticacrud.modelo

import java.time.LocalDateTime

data class Cita(
    var id: Int,
    var cliente: Usuario,
    var servicio: Servicio,
    var fechaHora: LocalDateTime,
    var empleadoAsignado: Empleado
) {
    //  métodos
}
