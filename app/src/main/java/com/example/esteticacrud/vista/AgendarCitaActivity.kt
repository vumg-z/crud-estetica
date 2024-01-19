package com.example.esteticacrud.vista

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.esteticacrud.R
import com.example.esteticacrud.controlador.CitasController
import com.example.esteticacrud.modelo.Empleado
import com.example.esteticacrud.modelo.Servicio
import com.example.esteticacrud.modelo.Usuario
import com.example.esteticacrud.utilidades.SessionManager
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.MonthDay
import java.time.Year
import java.time.format.DateTimeFormatter

class AgendarCitaActivity : AppCompatActivity() {

    private lateinit var spinnerServicio: Spinner

    private lateinit var buttonFecha: Button
    private lateinit var buttonHora: Button
    private var fechaCita: LocalDate? = null
    private var horaCita: LocalTime? = null

    private lateinit var spinnerEmpleado: Spinner
    private lateinit var buttonAgendar: Button
    private val citasController = CitasController()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.agendar_citas)

        val sessionManager = SessionManager.instance

        val usuarioActual = SessionManager.instance.usuarioActual

        val textViewNombreCliente = findViewById<TextView>(R.id.editTextCliente)
        textViewNombreCliente.text = "Nombre del cliente: ${usuarioActual?.nombre ?: "Desconocido"}"

        // Inicialización de vistas
        // editTextCliente = findViewById(R.id.editTextCliente)
        spinnerServicio = findViewById(R.id.spinnerServicio)

        buttonFecha = findViewById(R.id.buttonFecha)
        buttonHora = findViewById(R.id.buttonHora)

        spinnerEmpleado = findViewById(R.id.spinnerEmpleado)
        buttonAgendar = findViewById(R.id.buttonAgendar)

        // Poblar Spinners
        poblarSpinnerServicios()
        poblarSpinnerEmpleados()

        // Configurar OnClickListener para el botón
        buttonAgendar.setOnClickListener {
            agregarCita()
        }

        buttonFecha.setOnClickListener {
            mostrarDatePicker()
        }

        buttonHora.setOnClickListener {
            mostrarTimePicker()
        }
    }

    private fun agregarCita() {
        try {
            val fechaSeleccionada: LocalDate = fechaCita ?: throw Exception("Fecha no seleccionada")
            val horaSeleccionada: LocalTime = horaCita ?: throw Exception("Hora no seleccionada")

            if (fechaSeleccionada == null || horaSeleccionada == null) {
                throw Exception("Fecha y hora deben ser seleccionadas")
            }

            val dateTime = LocalDateTime.of(fechaSeleccionada, horaSeleccionada)

            // val nombreCliente = editTextCliente.text.toString()
            val nombreServicio = spinnerServicio.selectedItem.toString()
            val nombreEmpleado = spinnerEmpleado.selectedItem.toString()
            val usuarioActual = SessionManager.instance.usuarioActual
            val cliente = Usuario(0, usuarioActual?.nombre ?: "Desconocido", "password") // Ejemplo, ajustar según sea necesario
            val servicio = Servicio.valueOf(nombreServicio)
            val empleado = SessionManager.instance.empleados.find { it.nombre == nombreEmpleado } ?: throw Exception("Empleado no encontrado")


            val cita = citasController.agregarCita(cliente, servicio, dateTime, empleado)
            Toast.makeText(this, "Cita Agendada: ${cita.id}", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(this, "Error al agendar la cita: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun poblarSpinnerServicios() {
        val servicios = Servicio.values().map { it.name }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, servicios)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerServicio.adapter = adapter
    }

    private fun poblarSpinnerEmpleados() {
        val nombresEmpleados = SessionManager.instance.empleados.map { it.nombre }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, nombresEmpleados)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerEmpleado.adapter = adapter
    }

    private fun mostrarDatePicker() {
        val datePickerDialog = DatePickerDialog(this, { _, year, month, dayOfMonth ->
            val fechaSeleccionada = LocalDate.of(year, month + 1, dayOfMonth)
            fechaCita = fechaSeleccionada
            buttonFecha.text = fechaSeleccionada.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        }, Year.now().value, MonthDay.now().monthValue - 1, MonthDay.now().dayOfMonth)

        datePickerDialog.show()
    }

    private fun mostrarTimePicker() {
        val timePickerDialog = TimePickerDialog(this, { _, hourOfDay, minute ->
            val horaSeleccionada = LocalTime.of(hourOfDay, minute)
            horaCita = horaSeleccionada
            buttonHora.text = horaSeleccionada.format(DateTimeFormatter.ofPattern("HH:mm"))
        }, LocalTime.now().hour, LocalTime.now().minute, true)

        timePickerDialog.show()
    }


}
