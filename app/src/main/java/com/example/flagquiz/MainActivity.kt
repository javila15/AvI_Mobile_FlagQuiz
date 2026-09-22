package com.example.flagquiz

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tela_inicial)

        val editNome = findViewById<EditText>(R.id.editTextInput)
        val btnComecar = findViewById<Button>(R.id.button)

        btnComecar.setOnClickListener {
            val nome = editNome.text.toString().trim()

            if (nome.isEmpty()) {
                editNome.error = "Digite seu nome para continuar!"
                return@setOnClickListener
            }

            val intent = Intent(this, AtividadeQuiz::class.java).apply {
                putExtra("nomeJogador", nome)
            }
            startActivity(intent)
        }
    }
}
