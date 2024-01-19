package com.example.esteticacrud

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.esteticacrud.utilidades.SessionManager
import com.example.esteticacrud.vista.VistaUsuarioActivity
import com.example.esteticacrud.vista.VistaEmpleadoActivity


class MainActivity : AppCompatActivity() {

    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button

    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializar vistas
        usernameEditText = findViewById(R.id.usernameEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        loginButton = findViewById(R.id.loginButton)

        // Configurar listener para el botón de inicio de sesión
        loginButton.setOnClickListener {
            performLogin()
        }
    }

    private fun performLogin() {

        val username = usernameEditText.text.toString()
        val password = passwordEditText.text.toString()

        if (isValidCredentials(username, password)) {
            // Intentar encontrar un usuario o empleado
            val sessionManager = SessionManager.instance
            val usuario = sessionManager.usuarios.find { it.nombre == username && it.contraseña == password }
            val empleado = sessionManager.empleados.find { it.nombre == username && it.contraseña == password }

            when {
                usuario != null -> {
                    sessionManager.iniciarSesion(usuario)
                    Toast.makeText(this, "Inicio de sesión exitoso como Usuario", Toast.LENGTH_LONG).show()
                    // Redirigir a VistaUsuarioActivity
                    val intent = Intent(this, VistaUsuarioActivity::class.java)
                    intent.putExtra("NOMBRE_USUARIO", usuario.nombre)
                    startActivity(intent)
                }
                empleado != null -> {
                    sessionManager.iniciarSesion(empleado)
                    Toast.makeText(this, "Inicio de sesión exitoso como Empleado", Toast.LENGTH_LONG).show()
                    // Redirigir a VistaEmpleadoActivity
                    val intent = Intent(this, VistaEmpleadoActivity::class.java)
                    intent.putExtra("NOMBRE_EMPLEADO", empleado.nombre)
                    startActivity(intent)
                }
                else -> {
                    // Usuario no encontrado o contraseña incorrecta
                    Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_LONG).show()
                }
            }
        } else {
            // Mostrar mensaje de error: Credenciales inválidas
            Toast.makeText(this, "Por favor, ingrese credenciales válidas", Toast.LENGTH_LONG).show()
        }
    }




    private fun isValidCredentials(username: String, password: String): Boolean {
        return username.isNotBlank() && password.isNotBlank()
    }
}