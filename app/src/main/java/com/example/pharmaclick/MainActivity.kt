package com.example.pharmaclick

import android.widget.Button
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var medicamentoAdapter: MedicamentoAdapter
    private lateinit var medicamentosList: MutableList<Medicamento>
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Configurar la Toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Ajuste de ventanas para una experiencia completa
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Configurar RecyclerView
        recyclerView = findViewById(R.id.recyclerViewMedicamentos)
        recyclerView.layoutManager = LinearLayoutManager(this)
        medicamentosList = mutableListOf()
        medicamentoAdapter = MedicamentoAdapter(medicamentosList)
        recyclerView.adapter = medicamentoAdapter

        // Inicializa Firebase Database
        database = FirebaseDatabase.getInstance().getReference("medicamentos")

        // Escucha cambios en la base de datos
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                medicamentosList.clear()
                for (medicamentoSnapshot in snapshot.children) {
                    val medicamento = medicamentoSnapshot.getValue(Medicamento::class.java)
                    medicamento?.let { medicamentosList.add(it) }
                }
                medicamentoAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, "Error al cargar los datos", Toast.LENGTH_SHORT).show()
            }
        })

        // Botón para ver resumen de la orden
        val btnVerResumen: Button = findViewById(R.id.btnVerResumen)

        btnVerResumen.setOnClickListener {
            // Obtener medicamentos seleccionados
            val selectedMedicamentos = medicamentoAdapter.getSelectedMedicamentos()

            // Verificar que haya medicamentos seleccionados
            if (selectedMedicamentos.isEmpty()) {
                Toast.makeText(this, "No has seleccionado ningún medicamento", Toast.LENGTH_SHORT).show()
            } else {
                // Enviar medicamentos seleccionados a la actividad de resumen
                val intent = Intent(this, OrderSummaryActivity::class.java)
                intent.putParcelableArrayListExtra("selectedMedicamentos", ArrayList(selectedMedicamentos))
                startActivity(intent)
            }
        }
    }

    // Crea el menú de opciones
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // Manejador de selección del menú
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_sign_out -> {
                FirebaseAuth.getInstance().signOut().also {
                    Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, RegisterActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
