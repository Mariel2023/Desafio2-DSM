package com.example.pharmaclick

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class OrderSummaryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_summary)

        // Obtener la lista de medicamentos seleccionados desde el intent
        val selectedMedicamentos = intent.getParcelableArrayListExtra<Medicamento>("selectedMedicamentos")

        // Referencias a las vistas
        val listViewMedicamentos = findViewById<ListView>(R.id.listViewMedicamentosSeleccionados)
        val textViewTotal = findViewById<TextView>(R.id.textViewTotal)

        // Crear un adaptador para mostrar los medicamentos seleccionados
        val medicamentosAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, selectedMedicamentos?.map {
            "${it.nombre} - $${it.precio}"
        } ?: emptyList())

        listViewMedicamentos.adapter = medicamentosAdapter

        // Calcular el total
        val total = selectedMedicamentos?.sumOf { it.precio } ?: 0.0
        textViewTotal.text = "Total: $$total"
    }
}