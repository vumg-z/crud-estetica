package com.example.esteticacrud.vista

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.esteticacrud.R


class VistaEmpleadoActivity : AppCompatActivity() {

    private lateinit var tvLoggedInUser: TextView
    private lateinit var btnCreateUserAccount: Button
    private lateinit var btnCreateEmployeeAccount: Button
    private lateinit var btnViewUsersEmployees: Button  // Declare the button here

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.empleado_main)

        tvLoggedInUser = findViewById(R.id.tvLoggedInUser)
        btnCreateUserAccount = findViewById(R.id.btnCreateUserAccount)
        btnCreateEmployeeAccount = findViewById(R.id.btnCreateEmployeeAccount)
        btnViewUsersEmployees = findViewById(R.id.btnAddEmployee) // Initialize the button here

        val nombreUsuario = intent.getStringExtra("NOMBRE_EMPLEADO")
        tvLoggedInUser.text = "Usuario logeado: $nombreUsuario"

        btnCreateUserAccount.setOnClickListener {
            startCreateAccountActivity("user")
        }

        btnCreateEmployeeAccount.setOnClickListener {
            startCreateAccountActivity("employee")
        }

        btnViewUsersEmployees.setOnClickListener {
            val intent = Intent(this, ListUsersEmployeesActivity::class.java)
            startActivity(intent)
        }
    }

    private fun startCreateAccountActivity(accountType: String) {
        val intent = Intent(this, CreateAccountActivity::class.java)
        intent.putExtra("ACCOUNT_TYPE", accountType)
        startActivity(intent)
    }
}




