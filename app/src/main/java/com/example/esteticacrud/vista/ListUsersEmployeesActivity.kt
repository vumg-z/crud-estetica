package com.example.esteticacrud.vista

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.esteticacrud.R
import com.example.esteticacrud.utilidades.SessionManager

class ListUsersEmployeesActivity : AppCompatActivity() {

    private lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_users_employees)

        listView = findViewById(R.id.listViewUsersEmployees)
        setupListView()
    }
    private fun setupListView() {
        val usersAndEmployees = mutableListOf<String>()

        // Populate the list with user and employee names
        SessionManager.instance.usuarios.forEach { user ->
            usersAndEmployees.add("User: ${user.nombre}")
        }
        SessionManager.instance.empleados.forEach { employee ->
            usersAndEmployees.add("Employee: ${employee.nombre}")
        }

        val adapter = object : ArrayAdapter<String>(this, R.layout.list_item_user_employee, usersAndEmployees) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_item_user_employee, parent, false)
                val textViewName = view.findViewById<TextView>(R.id.textViewName)
                val imageViewDelete = view.findViewById<ImageView>(R.id.imageViewDelete)

                textViewName.text = getItem(position) // Set the name
                imageViewDelete.setOnClickListener {
                    val selectedItem = getItem(position)
                    if (selectedItem != null) {
                        if (selectedItem.startsWith("User: ")) {
                            val userName = selectedItem?.removePrefix("User: ")
                            if (userName != null) {
                                SessionManager.instance.eliminarUsuario(userName)
                            }
                        } else if (selectedItem.startsWith("Employee: ")) {
                            val employeeName = selectedItem.removePrefix("Employee: ")
                            SessionManager.instance.eliminarEmpleado(employeeName)
                        }
                    }
                    remove(selectedItem)
                    notifyDataSetChanged()
                }

                return view
            }
        }

        listView.adapter = adapter
    }

}
