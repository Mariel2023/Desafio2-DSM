package com.example.pharmaclick

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class MedicamentoAdapter(private val medicamentos: List<Medicamento>) :
    RecyclerView.Adapter<MedicamentoAdapter.MedicamentoViewHolder>() {

    // Lista para almacenar los medicamentos seleccionados
    private val selectedMedicamentos = mutableSetOf<Medicamento>()

    class MedicamentoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombre: TextView = itemView.findViewById(R.id.textViewNombreMedicamento)
        val precio: TextView = itemView.findViewById(R.id.textViewPrecioMedicamento)
        val imagen: ImageView = itemView.findViewById(R.id.imageViewMedicamento)
        val checkBox: CheckBox = itemView.findViewById(R.id.checkBoxMedicamento)  // CheckBox agregado
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicamentoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_medicamento, parent, false)
        return MedicamentoViewHolder(view)
    }

    override fun onBindViewHolder(holder: MedicamentoViewHolder, position: Int) {
        val medicamento = medicamentos[position]
        holder.nombre.text = medicamento.nombre
        holder.precio.text = "$${medicamento.precio}"
        Picasso.get().load(medicamento.imagen).into(holder.imagen)

        // Limpiar el listener anterior para evitar comportamientos inesperados
        holder.checkBox.setOnCheckedChangeListener(null)
        holder.checkBox.isChecked = selectedMedicamentos.contains(medicamento)

        // Listener para gestionar la selección del medicamento
        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedMedicamentos.add(medicamento)  // Agregar a la lista de seleccionados
            } else {
                selectedMedicamentos.remove(medicamento)  // Remover de la lista de seleccionados
            }
        }
    }

    override fun getItemCount(): Int = medicamentos.size

    // Método para obtener la lista de medicamentos seleccionados
    fun getSelectedMedicamentos(): List<Medicamento> {
        return selectedMedicamentos.toList()
    }
}
