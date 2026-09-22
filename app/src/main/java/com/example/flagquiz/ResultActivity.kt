package com.example.flagquiz

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tela_placar)

        val nomeJogador = intent.getStringExtra("nomeJogador") ?: "Jogador"
        val pontuacao = intent.getIntExtra("pontuacao", 0)

        val tvNomeUsuario = findViewById<TextView>(R.id.textNomeUsuario)
        val tvPontuacaoFinal = findViewById<TextView>(R.id.textPontuacaoFinal)
        val btnRecomecar = findViewById<Button>(R.id.btnRecomecar)

        tvNomeUsuario.text = "Jogador: $nomeJogador"
        tvPontuacaoFinal.text = "Pontuação: $pontuacao"

        btnRecomecar.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
}
