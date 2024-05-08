package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.ComponentActivity
import java.lang.Integer.parseInt

class SecondActivity : ComponentActivity() {

    private lateinit var spinner: Spinner
    private lateinit var screen: TextView
    private lateinit var tempoContribuicaoEditText: EditText
    private lateinit var idadeEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.second_activity)

        initializeViews()
        setupSipnner()

        val previousButton = findViewById<Button>(R.id.previous)
        previousButton.setOnClickListener {
            navigateToPreviousActivity()
        }
    }

    private fun setupSipnner() {
        val options = arrayOf("Masculino", "Feminino")
        spinner.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, options)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                // Handle item selection if needed
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                screen.text = "Selecione uma opção"
            }
        }
    }

    private fun initializeViews() {
        spinner = findViewById(R.id.sexo)
        screen = findViewById(R.id.screen)
        tempoContribuicaoEditText = findViewById(R.id.tempoContribuicao)
        idadeEditText = findViewById(R.id.idade)
    }

    @SuppressLint("SetTextI18n")
    fun calculateAction(view: View) {
        val tempoText = tempoContribuicaoEditText.text.toString()
        val idadeText = idadeEditText.text.toString()

        if (tempoText.isEmpty() || idadeText.isEmpty()) {
            screen.text = "Por favor, preencha todos os campos."
            return
        }

        val tempoValue = parseInt(tempoText)
        val idadeValue = parseInt(idadeText)
        val spinnerValue = spinner.selectedItem.toString()
        val pontos =  idadeValue + tempoValue

        if (tempoValue == 0 || idadeValue == 0 || idadeValue < tempoValue) {
            screen.text = "Valores inválidos."
            return
        }

        when {
            spinnerValue == "Masculino" && tempoValue < 35 -> {
                screen.text = "O tempo minino: 35 anos"
                return
            }

            spinnerValue == "Feminino" && tempoValue < 30 -> {
                screen.text = "O tempo minino: 30 anos"
                return
            }
            spinnerValue == "Masculino" && pontos < 96 -> {
                screen.text = "Pontos: ${idadeValue + tempoValue}, pontos insuficientes"
                return
            }
            spinnerValue == "Feminino" && pontos < 86 -> {
                screen.text = "Pontos: ${idadeValue + tempoValue}, pontos insuficientes"
                return
            }
        }

            screen.text = "Pontos: ${idadeValue + tempoValue}, Aposentado"
    }

    private fun navigateToPreviousActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}
