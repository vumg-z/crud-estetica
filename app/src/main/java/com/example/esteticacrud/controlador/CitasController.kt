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
        val finCita = fechaHora.plusHours(servicio.duracion.toLong())

        val superposicionEmpleado = sessionManager.obtenerCitas().any { citaExistente ->
            val finCitaExistente = citaExistente.fechaHora.plusHours(citaExistente.servicio.duracion.toLong())
            citaExistente.empleadoAsignado.nombre == empleadoAsignado.nombre &&
                    citaExistente.fechaHora.isBefore(finCita) && finCitaExistente.isAfter(fechaHora)
        }

        val superposicionCliente = sessionManager.obtenerCitas().any { citaExistente ->
            val finCitaExistente = citaExistente.fechaHora.plusHours(citaExistente.servicio.duracion.toLong())
            citaExistente.cliente.nombre == cliente.nombre &&
                    citaExistente.fechaHora.isBefore(finCita) && finCitaExistente.isAfter(fechaHora)
        }

        if (superposicionEmpleado) {
            throw Exception("El empleado ya tiene una cita en este horario.")
        }

        if (superposicionCliente) {
            throw Exception("El cliente ya tiene una cita en este horario.")
        }

        val nuevaCita = Cita(nextId++, cliente, servicio, fechaHora, empleadoAsignado)
        sessionManager.agregarCita(nuevaCita)
        return nuevaCita
    }


    fun eliminarCita(id: Int) {
        citas.removeIf { it.id == id }
    }

    fun actualizarCita(id: Int, nuevoCliente: Usuario, nuevoServicio: Servicio, nuevaFechaHora: LocalDateTime, nuevoEmpleado: Empleado) {
        val finNuevaCita = nuevaFechaHora.plusHours(nuevoServicio.duracion.toLong())

        val superposicionEmpleado = sessionManager.obtenerCitas().any { citaExistente ->
            val finCitaExistente = citaExistente.fechaHora.plusHours(citaExistente.servicio.duracion.toLong())
            citaExistente.id != id &&
                    citaExistente.empleadoAsignado.nombre == nuevoEmpleado.nombre &&
                    citaExistente.fechaHora.isBefore(finNuevaCita) &&
                    finCitaExistente.isAfter(nuevaFechaHora)
        }

        val superposicionCliente = sessionManager.obtenerCitas().any { citaExistente ->
            val finCitaExistente = citaExistente.fechaHora.plusHours(citaExistente.servicio.duracion.toLong())
            citaExistente.id != id &&
                    citaExistente.cliente.nombre == nuevoCliente.nombre &&
                    citaExistente.fechaHora.isBefore(finNuevaCita) &&
                    finCitaExistente.isAfter(nuevaFechaHora)
        }

        if (superposicionEmpleado || superposicionCliente) {
            throw Exception("La cita se superpone con otra cita existente.")
        }

        sessionManager.actualizarCita(id, nuevoCliente, nuevoServicio, nuevaFechaHora, nuevoEmpleado)
    }





    fun obtenerCitaPorId(id: Int): Cita? {
        val usuarioActual = SessionManager.instance.usuarioActual
        return SessionManager.instance.obtenerCitas().find { cita ->
            cita.id == id && cita.cliente.nombre == usuarioActual?.nombre
        }
    }

    fun cancelarCita(citaId: Int) {
        // Ensure this method updates the list in SessionManager correctly
        SessionManager.instance.citas.removeIf { it.id == citaId }
    }

    // Otros métodos según sea necesario
}
