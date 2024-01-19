package com.example.esteticacrud.vista

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
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


    private fun obtenerCitas(): List<Cita> {
        return SessionManager.instance.obtenerCitas()
    }

    private fun setupRecyclerView() {
        val listaDeCitas = SessionManager.instance.obtenerCitas().toMutableList()

        // Create the adapter with the list and a lambda function to handle cancellation
        adapter = CitasAdapter(listaDeCitas, onCitaCancel = { citaId ->
            CitasController().cancelarCita(citaId)
            Log.d("VerCitasActivity", "Request to cancel Cita ID: $citaId")


            // Remove the canceled cita from the adapter's data set
            val citaIndex = listaDeCitas.indexOfFirst { it.id == citaId }
            if (citaIndex != -1) {
                Log.d("VerCitasActivity", "Removing Cita at index: $citaIndex")

                listaDeCitas.removeAt(citaIndex)
                adapter.notifyItemRemoved(citaIndex)
                adapter.notifyItemRangeChanged(citaIndex, listaDeCitas.size)
            }
        })

        recyclerView.adapter = adapter  // Set the adapter on the RecyclerView
    }

}

