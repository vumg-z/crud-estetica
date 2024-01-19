package com.example.esteticacrud.vista

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.esteticacrud.R
import com.example.esteticacrud.modelo.Cita
import org.w3c.dom.Text

class CitasAdapter(
    private val citas: MutableList<Cita>,
    private val onCitaCancel: (Int) -> Unit
) : RecyclerView.Adapter<CitasAdapter.CitaViewHolder>() {

    class CitaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewCliente: TextView = view.findViewById(R.id.textViewCliente)
        val textViewFecha: TextView = view.findViewById(R.id.textViewFecha)
        val textViewHora: TextView = view.findViewById(R.id.textViewHora)
        val textViewServicio: TextView = view.findViewById(R.id.textViewServicio)
        val textViewEmpleado: TextView = view.findViewById(R.id.textViewEmpleado)
        val buttonCancelarCita: Button = view.findViewById(R.id.buttonCancelarCita)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CitaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cita, parent, false)
        return CitaViewHolder(view)
    }

    override fun onBindViewHolder(holder: CitaViewHolder, position: Int) {
        val cita = citas[position]

        Log.d("CitasAdapter", "Binding view holder for position: $position with Cita ID: ${cita.id}")


        holder.textViewCliente.text = "Cliente: ${cita.cliente.nombre}"
        holder.textViewFecha.text = "Fecha: ${cita.fechaHora.toLocalDate()}"
        holder.textViewHora.text = "Hora: ${cita.fechaHora.toLocalTime()}"
        holder.textViewServicio.text = "Servicio: ${cita.servicio.name}"
        holder.textViewEmpleado.text = "Empleado: ${cita.empleadoAsignado.nombre}"

        // Corrected the cancel button callback name
        holder.buttonCancelarCita.setOnClickListener {
            // First, remove the cita from the adapter's data set.
            val citaToRemove = citas[position]
            citas.removeAt(position)

            // Then, notify the adapter that an item has been removed.
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, itemCount)

            Log.d("CitasAdapter", "Cancel button clicked for Cita ID: ${cita.id}")

            // Finally, remove the cita from the main data set in CitasController or SessionManager.
            onCitaCancel(citaToRemove.id)
        }


    }





    override fun getItemCount() = citas.size
}
