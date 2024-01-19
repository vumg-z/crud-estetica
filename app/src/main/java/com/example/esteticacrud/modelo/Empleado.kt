package com.example.esteticacrud.modelo

class Empleado(
    id: Int,
    nombre: String,
    contraseña: String,
    val nomina: String
) : Usuario(id, nombre, contraseña) {
    // Métodos específicos para el empleado
}
