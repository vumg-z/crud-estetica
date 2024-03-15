package com.example.esteticacrud.vista

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.TimePickerDialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.esteticacrud.R
import com.example.esteticacrud.controlador.CitasController
import com.example.esteticacrud.modelo.Cita
import com.example.esteticacrud.modelo.Servicio
import com.example.esteticacrud.utilidades.SessionManager
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class EditarCitaActivity : AppCompatActivity() {

    private var fechaCita: LocalDate? = null
    private var horaCita: LocalTime? = null

    private lateinit var textViewCliente: TextView
    private lateinit var spinnerServicio: Spinner
    private lateinit var buttonFecha: Button
    private lateinit var buttonHora: Button
    private lateinit var spinnerEmpleado: Spinner
    private lateinit var buttonAgendar: Button

    private val citasController = CitasController()
    private var citaId: Int = -1
    private var cita: Cita? = null




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_cita)

        citaId = intent.getIntExtra("CITA_ID", -1)
        Log.d("EditarCitaActivity", "Cita ID: $citaId")

        textViewCliente = findViewById(R.id.editTextCliente)
        spinnerServicio = findViewById(R.id.spinnerServicio)
        buttonFecha = findViewById(R.id.buttonFecha)
        buttonHora = findViewById(R.id.buttonHora)
        spinnerEmpleado = findViewById(R.id.spinnerEmpleado)
        buttonAgendar = findViewById(R.id.buttonAgendar)

        // Asegúrate de inicializar las vistas antes de usarlas
        poblarSpinnerServicios()
        poblarSpinnerEmpleados()

        // Configurar los OnClickListener
        buttonFecha.setOnClickListener { mostrarDatePicker() }
        buttonHora.setOnClickListener { mostrarTimePicker() }
        buttonAgendar.setOnClickListener { actualizarCita() }

        if (citaId != -1) {
            cita = citasController.obtenerCitaPorId(citaId)
            Log.d("EditarCitaActivity", "Cita cargada: ${cita?.id}, Cliente: ${cita?.cliente?.nombre}")

            cita?.let {
                // Configurar las vistas con los datos de la cita
                textViewCliente.text = "Nombre del cliente: ${it.cliente.nombre}"
                fechaCita = it.fechaHora.toLocalDate()
                horaCita = it.fechaHora.toLocalTime()
                buttonFecha.text = fechaCita?.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                buttonHora.text = horaCita?.format(DateTimeFormatter.ofPattern("HH:mm"))

                // Asegúrate de seleccionar los valores correctos en los Spinners
                seleccionarValorSpinner(spinnerServicio, it.servicio.name)
                seleccionarValorSpinner(spinnerEmpleado, it.empleadoAsignado.nombre)
            }
        }
    }


    private fun actualizarCita() {
        try {
            val clienteActual = cita?.cliente ?: throw Exception("Cliente no definido")
            Log.d("EditarCitaActivity", "Actualizando cita para el cliente: ${clienteActual.nombre}")


            val fechaSeleccionada = fechaCita ?: throw Exception("Fecha no seleccionada")
            val horaSeleccionada = horaCita ?: throw Exception("Hora no seleccionada")
            val dateTime = LocalDateTime.of(fechaSeleccionada, horaSeleccionada)

            val nombreServicio = spinnerServicio.selectedItem.toString()
            val nombreEmpleado = spinnerEmpleado.selectedItem.toString()
            val servicio = Servicio.valueOf(nombreServicio)
            val empleado = SessionManager.instance.empleados.find { it.nombre == nombreEmpleado }
                ?: throw Exception("Empleado no encontrado")

            citasController.actualizarCita(citaId, clienteActual, servicio, dateTime, empleado)

            Toast.makeText(this, "Cita Actualizada: $citaId", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Log.e("EditarCitaActivity", "Error al actualizar la cita: ${e.message}", e)

            Toast.makeText(this, "Error al actualizar la cita: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun seleccionarValorSpinner(spinner: Spinner, valor: String) {
        val adapter = spinner.adapter
        for (i in 0 until adapter.count) {
            if (adapter.getItem(i).toString() == valor) {
                spinner.setSelection(i)
                break
            }
        }
    }
    // Métodos para mostrar DatePickerDialog y TimePickerDialog
    // ...

    private fun poblarSpinnerServicios() {
        val servicios = Servicio.values().map { it.name }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, servicios)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerServicio.adapter = adapter

        // Si tienes una cita existente, selecciona el servicio actual
        cita?.let {
            val servicioIndex = servicios.indexOf(it.servicio.name)
            if (servicioIndex != -1) {
                spinnerServicio.setSelection(servicioIndex)
            }
        }
    }

    private fun poblarSpinnerEmpleados() {
        val nombresEmpleados = SessionManager.instance.empleados.map { it.nombre }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, nombresEmpleados)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerEmpleado.adapter = adapter

        // Si tienes una cita existente, selecciona el empleado actual
        cita?.let {
            val empleadoIndex = nombresEmpleados.indexOf(it.empleadoAsignado.nombre)
            if (empleadoIndex != -1) {
                spinnerEmpleado.setSelection(empleadoIndex)
            }
        }
    }

    private fun mostrarDatePicker() {
        val listener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            val fechaSeleccionada = LocalDate.of(year, month + 1, dayOfMonth)
            fechaCita = fechaSeleccionada
            buttonFecha.text = fechaSeleccionada.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        }

        // Usa la fecha de la cita existente o la fecha actual
        val fechaInicial = fechaCita ?: LocalDate.now()
        val datePickerDialog = DatePickerDialog(
            this, listener, fechaInicial.year, fechaInicial.monthValue - 1, fechaInicial.dayOfMonth
        )

        datePickerDialog.show()
    }

    private fun mostrarTimePicker() {
        val listener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            val horaSeleccionada = LocalTime.of(hourOfDay, minute)
            horaCita = horaSeleccionada
            buttonHora.text = horaSeleccionada.format(DateTimeFormatter.ofPattern("HH:mm"))
        }

        // Usa la hora de la cita existente o la hora actual
        val horaInicial = horaCita ?: LocalTime.now()
        val timePickerDialog = TimePickerDialog(
            this, listener, horaInicial.hour, horaInicial.minute, true
        )

        timePickerDialog.show()
    }

}
