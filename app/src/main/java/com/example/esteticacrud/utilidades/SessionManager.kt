package com.example.esteticacrud.utilidades

import com.example.esteticacrud.modelo.Cita
import com.example.esteticacrud.modelo.Empleado
import com.example.esteticacrud.modelo.Usuario

class SessionManager private constructor()  {

    val usuarios: MutableList<Usuario> = mutableListOf()
    val empleados: MutableList<Empleado> = mutableListOf()
    val citas: MutableList<Cita> = mutableListOf()

    init {
        crearDatosDePrueba()
    }

    fun agregarCita(cita: Cita) {
        citas.add(cita)
    }

    fun obtenerCitas(): List<Cita> {
        return citas
    }


    private fun crearDatosDePrueba() {
        // Crear empleados de prueba

        empleados.add(Empleado(1, "Pepe", "12345", "nomina1"))
        empleados.add(Empleado(2, "Angel", "12345", "nomina2"))
        empleados.add(Empleado(3, "Maria", "12345", "nomina3"))
        empleados.add(Empleado(4, "Cris", "12345", "nomina4"))
        empleados.add(Empleado(5, "Michael", "12345", "nomina5"))

        // Crear un usuario de prueba
        usuarios.add(Usuario(6, "Uriel", "12345"))
    }

    companion object {
        val instance: SessionManager by lazy { SessionManager() }
    }

    var usuarioActual: Usuario? = null
        private set

    fun iniciarSesion(usuario: Usuario) {
        usuarioActual = usuario
    }

    fun cerrarSesion() {
        usuarioActual = null
    }

    val estaAutenticado: Boolean
        get() = usuarioActual != null

    fun eliminarUsuario(nombre: String) {
        usuarios.removeIf { it.nombre == nombre }
    }

    fun eliminarEmpleado(nombre: String) {
        empleados.removeIf { it.nombre == nombre }
    }
}
