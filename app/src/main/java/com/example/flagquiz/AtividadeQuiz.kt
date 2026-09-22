package com.example.flagquiz

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.Normalizer
import java.util.Locale

class AtividadeQuiz : AppCompatActivity() {

    private data class Bandeira(
        val nome: String,
        val imagem: Int
    )

    private val bandeiras = listOf(
        Bandeira("Argentina", R.drawable.flag_ar),
        Bandeira("Australia", R.drawable.flag_au),
        Bandeira("Brasil", R.drawable.flag_br),
        Bandeira("Chile", R.drawable.flag_cl),
        Bandeira("Peru", R.drawable.flag_pe),
        Bandeira("Mexico", R.drawable.flag_mx),
        Bandeira("Estados Unidos", R.drawable.flag_us),
        Bandeira("Canada", R.drawable.flag_ca),
        Bandeira("França", R.drawable.flag_fr),
        Bandeira("Alemanha", R.drawable.flag_de),
        Bandeira("Italia", R.drawable.flag_it),
        Bandeira("Espanha", R.drawable.flag_es),
        Bandeira("Portugal", R.drawable.flag_pt),
        Bandeira("Japão", R.drawable.flag_jp),
        Bandeira("Colombia", R.drawable.flag_co)
    )

    private lateinit var perguntas: List<Bandeira>
    private var indiceAtual = 0
    private var pontuacao = 0
    private var nomeJogador = ""
    private var perguntaRespondida = false

    private lateinit var progresso: TextView
    private lateinit var imagemBandeira: ImageView
    private lateinit var campoResposta: EditText
    private lateinit var botaoResponder: Button
    private lateinit var botaoProxima: Button
    private lateinit var feedback: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        progresso = findViewById(R.id.textProgress)
        imagemBandeira = findViewById(R.id.imageFlag)
        campoResposta = findViewById(R.id.editAnswer)
        botaoResponder = findViewById(R.id.buttonCheck)
        botaoProxima = findViewById(R.id.buttonNext)
        feedback = findViewById(R.id.textFeedback)

        nomeJogador = intent.getStringExtra("nomeJogador")
            ?: intent.getStringExtra("PLAYER_NAME")
            ?: ""

        // Sorteia exatamente 5 bandeiras diferentes entre as disponíveis.
        perguntas = bandeiras.shuffled().take(5)

        botaoResponder.setOnClickListener {
            verificarResposta()
        }

        botaoProxima.setOnClickListener {
            mostrarProximaPergunta()
        }

        mostrarPergunta()
    }

    private fun mostrarPergunta() {
        val bandeira = perguntas[indiceAtual]

        progresso.text = "Pergunta ${indiceAtual + 1} de ${perguntas.size}"
        imagemBandeira.setImageResource(bandeira.imagem)
        imagemBandeira.contentDescription =
            "Bandeira da pergunta ${indiceAtual + 1}"

        campoResposta.setText("")
        campoResposta.isEnabled = true

        feedback.text = ""
        botaoResponder.isEnabled = true
        botaoProxima.isEnabled = false
        perguntaRespondida = false
    }

    private fun verificarResposta() {
        if (perguntaRespondida) {
            return
        }

        val respostaDigitada = campoResposta.text.toString().trim()

        if (respostaDigitada.isEmpty()) {
            campoResposta.error = "Digite uma resposta"
            return
        }

        val bandeiraAtual = perguntas[indiceAtual]
        val respostaCorreta = normalizar(bandeiraAtual.nome)
        val respostaDoUsuario = normalizar(respostaDigitada)

        if (respostaDoUsuario == respostaCorreta) {
            pontuacao += 20
            feedback.text = "Correto! +20 pontos"
            feedback.setTextColor(Color.rgb(46, 125, 50))
        } else {
            feedback.text =
                "Incorreto! A resposta correta é: ${bandeiraAtual.nome}"
            feedback.setTextColor(Color.RED)
        }

        perguntaRespondida = true
        campoResposta.isEnabled = false
        botaoResponder.isEnabled = false
        botaoProxima.isEnabled = true
    }

    private fun mostrarProximaPergunta() {
        if (!perguntaRespondida) {
            return
        }

        if (indiceAtual < perguntas.lastIndex) {
            indiceAtual++
            mostrarPergunta()
        } else {
            abrirResultado()
        }
    }

    private fun normalizar(texto: String): String {
        val decomposto = Normalizer.normalize(
            texto.trim().lowercase(Locale.ROOT),
            Normalizer.Form.NFD
        )

        return decomposto.replace(
            Regex("\\p{InCombiningDiacriticalMarks}+"),
            ""
        )
    }

    private fun abrirResultado() {
        val resultadoIntent = Intent(this, ResultActivity::class.java)
        resultadoIntent.putExtra("nomeJogador", nomeJogador)
        resultadoIntent.putExtra("pontuacao", pontuacao)
        startActivity(resultadoIntent)
        finish()
    }
}
