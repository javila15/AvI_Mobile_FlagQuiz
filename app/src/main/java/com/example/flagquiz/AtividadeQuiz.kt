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

class QuizActivity : AppCompatActivity() {

    private data class Bandeira(
        val nome: String,
        val imagem: Int
    )

    private val bandeiras = listOf(
        Bandeira("Alemanha", R.drawable.alemanha),
        Bandeira("Argentina", R.drawable.argentina),
        Bandeira("Austrália", R.drawable.australia),
        Bandeira("Bolívia", R.drawable.bolivia),
        Bandeira("Brasil", R.drawable.brasil),
        Bandeira("Canadá", R.drawable.canada),
        Bandeira("China", R.drawable.china),
        Bandeira("Espanha", R.drawable.espanha),
        Bandeira("Estados Unidos", R.drawable.estadosunidos),
        Bandeira("França", R.drawable.franca),
        Bandeira("Gana", R.drawable.gana),
        Bandeira("Índia", R.drawable.india),
        Bandeira("Itália", R.drawable.italia),
        Bandeira("Japão", R.drawable.japao),
        Bandeira("Coreia do Sul", R.drawable.koreasul),
        Bandeira("México", R.drawable.mexico),
        Bandeira("Portugal", R.drawable.portugal),
        Bandeira("Uruguai", R.drawable.uruguai),
        Bandeira("Vaticano", R.drawable.vaticano),
        Bandeira("Venezuela", R.drawable.venezuela)
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
