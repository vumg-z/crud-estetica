package com.example.esteticacrud.vista

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.esteticacrud.R

class VistaUsuarioActivity : AppCompatActivity() {

    private lateinit var tvLoggedInUser : TextView;
    private lateinit var agendarBtn : Button;
    private lateinit var verCitasBtn : Button;




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_main)

        tvLoggedInUser = findViewById(R.id.tvLoggedInUser)
        agendarBtn = findViewById(R.id.btnSchedule)
        verCitasBtn = findViewById(R.id.btnViewAppointments)

        val nombreUsuario = intent.getStringExtra("NOMBRE_USUARIO")
        tvLoggedInUser.text = "Usuario logeado: $nombreUsuario"

        // Configurar el OnClickListener
        agendarBtn.setOnClickListener {
            abrirAgendarCitaActivity()
        }

        verCitasBtn.setOnClickListener {
            abrirVerCitas();
        }
    }

    private fun abrirVerCitas() {
        val intent = Intent(this, VerCitasActivity::class.java)
        startActivity(intent)
    }

    private fun abrirAgendarCitaActivity() {
        val intent = Intent(this, AgendarCitaActivity::class.java)
        startActivity(intent)
    }

}