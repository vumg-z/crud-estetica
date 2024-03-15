package com.example.esteticacrud.vista

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.esteticacrud.R
import com.example.esteticacrud.controlador.CitasController
import com.example.esteticacrud.modelo.Cita
import com.example.esteticacrud.utilidades.SessionManager

class VerCitasActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CitasAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ver_citas)

        recyclerView = findViewById(R.id.citasRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        setupRecyclerView()
    }

    @SuppressLint("MissingPermission")
    private fun enviarNotificacionCitaEliminada(citaId: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelID = "CITA_ELIMINACION_CHANNEL"
            val name = getString(R.string.channel_name_eliminacion) // Debes agregar esto en strings.xml
            val descriptionText = getString(R.string.channel_description_eliminacion) // Debes agregar esto en strings.xml
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(this, "CITA_ELIMINACION_CHANNEL")
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Asegúrate de tener este recurso gráfico
            .setContentTitle(getString(R.string.notification_title_eliminacion)) // Agrega esto en strings.xml
            .setContentText(getString(R.string.notification_content_eliminacion)) // Agrega esto en strings.xml
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)) {
            notify(citaId, builder.build())
        }
    }


    private fun obtenerCitas(): List<Cita> {
        return SessionManager.instance.obtenerCitas()
    }

    private fun setupRecyclerView() {
        val listaDeCitas = SessionManager.instance.obtenerCitas().toMutableList()

        // Crea el adaptador con la lista y una función lambda para manejar la cancelación
        adapter = CitasAdapter(listaDeCitas, onCitaCancel = { citaId ->
            CitasController().cancelarCita(citaId)
            Log.d("VerCitasActivity", "Request to cancel Cita ID: $citaId")

            // Enviar notificación de cita cancelada
            enviarNotificacionCitaEliminada(citaId)

            // Remover la cita cancelada del conjunto de datos del adaptador
            val citaIndex = listaDeCitas.indexOfFirst { it.id == citaId }
            if (citaIndex != -1) {
                Log.d("VerCitasActivity", "Removing Cita at index: $citaIndex")

                listaDeCitas.removeAt(citaIndex)
                adapter.notifyItemRemoved(citaIndex)
                adapter.notifyItemRangeChanged(citaIndex, listaDeCitas.size)
            }
        }, onCitaEdit = { cita ->
            // Lógica para editar la cita, como abrir una nueva actividad
            // Por ejemplo:
            val intent = Intent(this, EditarCitaActivity::class.java).apply {
                putExtra("CITA_ID", cita.id)
            }
            startActivity(intent)
        })

        recyclerView.adapter = adapter  // Establecer el adaptador en el RecyclerView
    }


}

