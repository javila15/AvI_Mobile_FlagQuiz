class AtividadeQuiz : AppCompatActivity (){
    private val bandeiras = listOf(
        Bandeira("Alemanha",R.drawable.alemanha),
        Bandeira("Argentina",R.drawable.argentina),
        Bandeira("Australia",R.drawable.australia),
        Bandeira("Bolivia",R.drawable.bolivia),
        Bandeira("Brasil",R.drawable.brasil),
        Bandeira("Canada",R.drawable.canada),
        Bandeira("China",R.drawable.china),
        Bandeira("Espanha",R.drawable.espanha),
        Bandeira("Estados Unidos",R.drawable.estadosunidos),
        Bandeira("França",R.drawable.franca),
        Bandeira("Gana",R.drawable.gana),
        Bandeira("India",R.drawable.india),
        Bandeira("Italia",R.drawable.italia),
        Bandeira("Japão",R.drawable.japao),
        Bandeira("Coreia do Sul",R.drawable.koreasul),
        Bandeira("Mexico",R.drawable.mexico),
        Bandeira("Portugal",R.drawable.portugal),
        Bandeira("Uruguai",R.drawable.uruguai),
        Bandeira("Vaticano",R.drawable.vaticano),
        Bandeira("Venezuela",R.drawable.venezuela)

    )
    private lateinit var perguntas: List<Bandeira>
    private var indiceAtual = 0
    private var textPontuacaoFinal = 0
    private var textNomeUsuario = ""

    private lateinit var progresso: TextView
    private lateinit var imgbandeira: ImageView
    private lateinit var resposta: EditText
    private lateinit var btnResposta: Button
    private lateinit var feedback: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        progresso = findViewById(R.id.tvProgresso)
        imgbandeira = findViewById(R.id.ivBandeira)
        resposta = findViewById(R.id.etResposta)
        btnResposta = findViewById(R.id.btnResponder)
        feedback = findViewById(R.id.tvFeedback)

        textNomeUsuario = intent.getStringExtra("nomeJogador") ?: ""

        // Sorteia 5 bandeiras diferentes entre as 15
        perguntas = bandeiras.shuffled().take(5)

        btnResposta.setOnClickListener { verResposta() }
        mostPergunta()
    }

    private fun mostPergunta() {
        val bandeira = perguntas[indiceAtual]
        progresso.text = "${indiceAtual + 1} de ${perguntas.size}"
        imgbandeira.setImageResource(bandeira.imagem)
        resposta.text.clear()
        feedback.text = ""
        btnResposta.isEnabled = true
    }

    private fun verResposta() {
        val rresposta = normalizar(resposta.text.toString())
        val correta = normalizar(perguntas[indiceAtual].nome)
        btnResposta.isEnabled = false

        if (rresposta == correta) {
            textPontuacaoFinal += 20
            feedback.text = "Correto!"
            feedback.setTextColor(Color.parseColor("#2E7D32"))
        } else {
            feedback.text = "Incorreto!"
            feedback.setTextColor(Color.RED)
        }

        Handler(Looper.getMainLooper()).postDelayed({
            indiceAtual++
            if (indiceAtual < perguntas.size) {
                mostPergunta()
            } else {
                ResultadoFinal()
            }
        }, 1500)
    }

    private fun normalizar(texto: String): String {
        val decomposto = Normalizer.normalize(texto.trim().lowercase(), Normalizer.Form.NFD)
        return decomposto.replace(Regex("\\p{InCombiningDiacriticalMarks}+"), "")
    }

    private fun ResultadoFinal() {
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra("nomeJogador", textNomeUsuario)
        intent.putExtra("pontuacao", textPontuacaoFinal)
        startActivity(intent)
        finish()
    }
}
