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
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
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

    @SuppressLint("MissingPermission")
    private fun enviarNotificacionCitaCreada(citaId: Int) {
        // Crear el canal de notificaciones (o asegurarse de que se ha creado)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelID = "CITA_CREACION_CHANNEL" // Identificador del canal de notificaciones
            val name = getString(R.string.channel_name) // Nombre del canal (visible para el usuario)
            val descriptionText = getString(R.string.channel_description) // Descripción del canal (visible para el usuario)
            val importance = NotificationManager.IMPORTANCE_HIGH // Establece la importancia para mostrar interrupciones
            val channel = NotificationChannel(channelID, name, importance).apply {
                description = descriptionText
                enableLights(true) // Habilita luces para notificaciones en este canal, si el dispositivo lo soporta
                enableVibration(true) // Habilita vibración para notificaciones en este canal
            }
            // Registrar el canal en el sistema
            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        // Construir la notificación
        val builder = NotificationCompat.Builder(this, "CITA_CREACION_CHANNEL")
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Asegúrate de tener este recurso gráfico
            .setContentTitle(getString(R.string.notification_title)) // Título de la notificación
            .setContentText(getString(R.string.notification_content)) // Contenido de la notificación
            .setPriority(NotificationCompat.PRIORITY_HIGH) // Establece la prioridad para las interrupciones
            .setVibrate(longArrayOf(1000, 500, 1000, 500)) // Patrón de vibración: espera, vibra, espera, vibra

        // Mostrar la notificación
        with(NotificationManagerCompat.from(this)) {
            notify(citaId, builder.build()) // Usar citaId asegura un identificador único para cada notificación
        }
    }

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
            // Enviar notificación de cita creada
            enviarNotificacionCitaCreada(cita.id)
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
