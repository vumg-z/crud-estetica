package com.example.esteticacrud.controlador

import com.example.esteticacrud.modelo.Cita
import com.example.esteticacrud.modelo.Empleado
import com.example.esteticacrud.modelo.Servicio
import com.example.esteticacrud.modelo.Usuario
import com.example.esteticacrud.utilidades.SessionManager
import java.time.LocalDateTime

class CitasController {

    private val citas: MutableList<Cita> = mutableListOf()
    private var nextId = 1
    private val sessionManager = SessionManager.instance

    fun agregarCita(cita: Cita) {
        sessionManager.agregarCita(cita)
    }

    fun obtenerCitas(): List<Cita> {
        return sessionManager.obtenerCitas()
    }

    fun agregarCita(cliente: Usuario, servicio: Servicio, fechaHora: LocalDateTime, empleadoAsignado: Empleado): Cita {
        val nuevaCita = Cita(nextId++, cliente, servicio, fechaHora, empleadoAsignado)
        sessionManager.agregarCita(nuevaCita)
        return nuevaCita
    }

    fun eliminarCita(id: Int) {
        citas.removeIf { it.id == id }
    }

    fun actualizarCita(id: Int, nuevaFechaHora: LocalDateTime) {
        citas.find { it.id == id }?.let {
            it.fechaHora = nuevaFechaHora
            // Otras actualizaciones necesarias
        }
    }

    fun obtenerCitaPorId(id: Int): Cita? {
        return citas.find { it.id == id }
    }

    fun cancelarCita(citaId: Int) {
        // Ensure this method updates the list in SessionManager correctly
        SessionManager.instance.citas.removeIf { it.id == citaId }
    }

    // Otros métodos según sea necesario
}
