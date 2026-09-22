package com.example.tictactoe

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem

class MainActivity : AppCompatActivity() {

    private lateinit var botones: Array<Button>
    private lateinit var textoEstado: TextView

    private lateinit var contadorJugador: TextView
    private lateinit var contadorComputadora: TextView
    private lateinit var contadorEmpates: TextView
    private lateinit var botonNuevaPartida: Button

    private val juego = TicTacToeGame()

    private var juegoTerminado = false

    // Marcadores
    private var victoriasJugador = 0
    private var victoriasComputadora = 0
    private var empates = 0


    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        menuInflater.inflate(R.menu.options_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {

            R.id.new_game -> {
                iniciarPartida()
                true
            }

            R.id.ai_difficulty -> {
                mostrarDialogoDificultad()
                true
            }

            R.id.quit -> {
                mostrarDialogoSalir()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun mostrarDialogoDificultad() {

        val niveles = arrayOf(
            "Fácil",
            "Difícil",
            "Experto"
        )

        val dificultadActual = when (juego.getDifficultyLevel()) {

            TicTacToeGame.DifficultyLevel.Easy -> 0

            TicTacToeGame.DifficultyLevel.Harder -> 1

            TicTacToeGame.DifficultyLevel.Expert -> 2
        }

        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Dificultad")
            .setSingleChoiceItems(
                niveles,
                dificultadActual
            ) { dialog, cual ->

                when (cual) {

                    0 -> juego.setDifficultyLevel(
                        TicTacToeGame.DifficultyLevel.Easy
                    )

                    1 -> juego.setDifficultyLevel(
                        TicTacToeGame.DifficultyLevel.Harder
                    )

                    2 -> juego.setDifficultyLevel(
                        TicTacToeGame.DifficultyLevel.Expert
                    )
                }

                dialog.dismiss()
            }
            .show()
    }

    private fun mostrarDialogoSalir() {

        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Salir")
            .setMessage("¿Seguro que quieres salir?")
            .setPositiveButton("Sí") { _, _ ->
                finish()
            }
            .setNegativeButton("No", null)
            .show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        botones = arrayOf(
            findViewById(R.id.boton0),
            findViewById(R.id.boton1),
            findViewById(R.id.boton2),
            findViewById(R.id.boton3),
            findViewById(R.id.boton4),
            findViewById(R.id.boton5),
            findViewById(R.id.boton6),
            findViewById(R.id.boton7),
            findViewById(R.id.boton8)
        )

        for (boton in botones) {
            boton.backgroundTintList = null
            boton.setBackgroundResource(R.drawable.celda)
            boton.minWidth = 0
            boton.minHeight = 0
            boton.setPadding(0, 0, 0, 0)
        }

        textoEstado = findViewById(R.id.textoEstado)

        contadorJugador = findViewById(R.id.contadorJugador)
        contadorComputadora = findViewById(R.id.contadorComputadora)
        contadorEmpates = findViewById(R.id.contadorEmpates)

        botonNuevaPartida = findViewById(R.id.botonNuevaPartida)

        botonNuevaPartida.setOnClickListener {
            iniciarPartida()
        }

        iniciarPartida()
    }

    private fun iniciarPartida() {

        juego.clearBoard()
        juegoTerminado = false

        for (i in botones.indices) {

            botones[i].text = ""
            botones[i].isEnabled = true

            botones[i].setOnClickListener {
                hacerMovimientoJugador(i)
            }
        }

        textoEstado.text = "Tu turno"
    }

    private fun hacerMovimientoJugador(posicion: Int) {

        if (juegoTerminado) {
            return
        }

        // Movimiento del jugador
        juego.setMove(
            TicTacToeGame.HUMAN_PLAYER,
            posicion
        )

        botones[posicion].text = "X"
        botones[posicion].isEnabled = false

        // Comprobar resultado
        var resultado = juego.checkForWinner()

        if (resultado != 0) {
            mostrarResultado(resultado)
            return
        }

        // Turno de la computadora
        textoEstado.text = "Turno de la computadora"

        val movimientoComputadora = juego.getComputerMove()

        if (movimientoComputadora != -1) {

            juego.setMove(
                TicTacToeGame.COMPUTER_PLAYER,
                movimientoComputadora
            )

            botones[movimientoComputadora].text = "O"
            botones[movimientoComputadora].isEnabled = false
        }

        // Comprobar nuevamente
        resultado = juego.checkForWinner()

        if (resultado != 0) {
            mostrarResultado(resultado)
        } else {
            textoEstado.text = "Tu turno"
        }
    }

    private fun mostrarResultado(resultado: Int) {

        juegoTerminado = true

        when (resultado) {

            1 -> {
                empates++
                textoEstado.text = "¡Empate!"
            }

            2 -> {
                victoriasJugador++
                textoEstado.text = "¡Ganaste!"
            }

            3 -> {
                victoriasComputadora++
                textoEstado.text = "¡Ganó la computadora!"
            }
        }

        actualizarContador()

        // Desactivar todas las casillas
        for (boton in botones) {
            boton.isEnabled = false
        }
    }

    private fun actualizarContador() {

        contadorJugador.text = "Tú: $victoriasJugador"

        contadorEmpates.text = "Empates: $empates"

        contadorComputadora.text = "PC: $victoriasComputadora"
    }
}