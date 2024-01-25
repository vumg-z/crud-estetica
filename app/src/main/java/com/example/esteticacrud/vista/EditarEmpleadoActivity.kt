package com.example.esteticacrud.vista

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.esteticacrud.R
import com.example.esteticacrud.utilidades.SessionManager

class EditarEmpleadoActivity : AppCompatActivity() {

    private lateinit var editTextEmployeeName: EditText
    private lateinit var buttonSave: Button
    private lateinit var buttonCancel: Button
    private var oldEmployeeName: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_empleado)

        editTextEmployeeName = findViewById(R.id.editTextEmployeeName)
        buttonSave = findViewById(R.id.buttonSave)
        buttonCancel = findViewById(R.id.buttonCancel)

        val employeeName = intent.getStringExtra("EMPLOYEE_NAME")
        editTextEmployeeName.setText(employeeName)

        buttonSave.setOnClickListener {
            guardarCambios()
        }

        buttonCancel.setOnClickListener {
            finish() // Cierra la actividad y regresa a la anterior
        }

        oldEmployeeName = intent.getStringExtra("OLD_EMPLOYEE_NAME")
        editTextEmployeeName.setText(oldEmployeeName)
    }

    private fun guardarCambios() {
        val updatedName = editTextEmployeeName.text.toString()

        if (updatedName.isEmpty()) {
            Toast.makeText(this, "El nombre no puede estar vacío", Toast.LENGTH_SHORT).show()
            return
        }

        oldEmployeeName?.let { oldName ->
            val sessionManager = SessionManager.instance
            sessionManager.actualizarEmpleadoNombre(oldName, updatedName)
            Toast.makeText(this, "Empleado actualizado con éxito", Toast.LENGTH_SHORT).show()
            setResult(Activity.RESULT_OK) // Establece el resultado para `ListUsersEmployeesActivity`
            finish() // Cierra la actividad y regresa a la anterior
        } ?: run {
            Toast.makeText(this, "Error: nombre antiguo no disponible", Toast.LENGTH_SHORT).show()
        }

        setResult(Activity.RESULT_OK) // Establece el resultado para `ListUsersEmployeesActivity`
        finish() // Cierra la actividad y regresa a la anterior
    }


}
