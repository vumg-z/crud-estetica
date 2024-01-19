package com.example.esteticacrud.modelo

enum class Servicio(val duracion: Int, val descripcion: String) {
    CORTE(1, "Corte de cabello - Duración aproximada: 1 hora"),
    UNAS(2, "Manicura o pedicura - Duración aproximada: 2 horas"),
    PESTANAS(2, "Aplicación de pestañas - Duración aproximada: 2 horas"),
    TRATAMIENTOS(2, "Tratamientos faciales o corporales - Duración aproximada: 2 horas");
}
