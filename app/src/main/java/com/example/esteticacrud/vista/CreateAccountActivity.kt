package com.example.esteticacrud.vista

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.esteticacrud.R
import com.example.esteticacrud.modelo.Empleado
import com.example.esteticacrud.modelo.Usuario
import com.example.esteticacrud.utilidades.SessionManager

class CreateAccountActivity : AppCompatActivity() {

    private lateinit var editTextName: EditText
    private lateinit var editTextPassword: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.crear_usuario_empleado)

        editTextName = findViewById(R.id.editTextName)
        editTextPassword = findViewById(R.id.editTextPassword)
        // Initialize other views

        val accountType = intent.getStringExtra("ACCOUNT_TYPE")

        val submitButton = findViewById<Button>(R.id.buttonSubmit)
        submitButton.setOnClickListener {
            val name = editTextName.text.toString()
            val password = editTextPassword.text.toString()

            when (accountType) {
                "user" -> createUser(name, password)
                "employee" -> createEmployee(name, password)
                else -> Toast.makeText(this, "Invalid account type", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun createUser(name: String, password: String) {
        val sessionManager = SessionManager.instance

        // Verificar si ya existe un usuario con el mismo nombre
        val existingUser = sessionManager.usuarios.find { it.nombre == name }

        if (existingUser == null) {
            // No hay un usuario con el mismo nombre, crear uno nuevo
            val newUser = Usuario(sessionManager.usuarios.size + 1, name, password)
            sessionManager.usuarios.add(newUser)
            Toast.makeText(this, "Usuario $name creado exitosamente", Toast.LENGTH_SHORT).show()
        } else {
            // Ya existe un usuario con el mismo nombre, mostrar un mensaje de error
            Toast.makeText(this, "Ya existe un usuario con el nombre $name", Toast.LENGTH_SHORT).show()
        }
    }

    private fun createEmployee(name: String, password: String) {
        val newEmployee = Empleado(SessionManager.instance.empleados.size + 1, name, password, "nominaX")
        SessionManager.instance.empleados.add(newEmployee)
        Toast.makeText(this, "Empleado $name creado exitosamente", Toast.LENGTH_SHORT).show()
    }
}
