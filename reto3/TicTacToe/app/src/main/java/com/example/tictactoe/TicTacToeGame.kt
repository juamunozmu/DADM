package com.example.tictactoe

class TicTacToeGame {

    companion object {
        const val HUMAN_PLAYER = 'X'
        const val COMPUTER_PLAYER = 'O'
        const val OPEN_SPOT = ' '
    }

    private val board = CharArray(9) { OPEN_SPOT }

    fun clearBoard() {
        for (i in board.indices) {
            board[i] = OPEN_SPOT
        }
    }

    fun setMove(player: Char, location: Int) {
        if (location in 0..8 && board[location] == OPEN_SPOT) {
            board[location] = player
        }
    }

    fun getBoard(): CharArray {
        return board.copyOf()
    }

    fun checkForWinner(): Int {

        val winningPositions = arrayOf(
            intArrayOf(0, 1, 2),
            intArrayOf(3, 4, 5),
            intArrayOf(6, 7, 8),
            intArrayOf(0, 3, 6),
            intArrayOf(1, 4, 7),
            intArrayOf(2, 5, 8),
            intArrayOf(0, 4, 8),
            intArrayOf(2, 4, 6)
        )

        for (position in winningPositions) {
            val a = position[0]
            val b = position[1]
            val c = position[2]

            if (board[a] != OPEN_SPOT &&
                board[a] == board[b] &&
                board[b] == board[c]
            ) {
                return if (board[a] == HUMAN_PLAYER) {
                    2
                } else {
                    3
                }
            }
        }

        if (board.none { it == OPEN_SPOT }) {
            return 1
        }

        return 0
    }

    fun getComputerMove(): Int {

        // 1. La computadora intenta ganar
        for (position in board.indices) {
            if (board[position] == OPEN_SPOT) {

                board[position] = COMPUTER_PLAYER

                if (checkForWinner() == 3) {
                    board[position] = OPEN_SPOT
                    return position
                }

                board[position] = OPEN_SPOT
            }
        }

        // 2. La computadora intenta bloquear al jugador
        for (position in board.indices) {
            if (board[position] == OPEN_SPOT) {

                board[position] = HUMAN_PLAYER

                if (checkForWinner() == 2) {
                    board[position] = OPEN_SPOT
                    return position
                }

                board[position] = OPEN_SPOT
            }
        }

        // 3. Si el centro está libre, ocupa el centro
        if (board[4] == OPEN_SPOT) {
            return 4
        }

        // 4. Si hay una esquina libre, ocupa una esquina
        val corners = listOf(0, 2, 6, 8)

        val availableCorners = corners.filter {
            board[it] == OPEN_SPOT
        }

        if (availableCorners.isNotEmpty()) {
            return availableCorners.random()
        }

        // 5. Finalmente, elegir cualquier casilla disponible
        val availablePositions = board.indices.filter {
            board[it] == OPEN_SPOT
        }

        return availablePositions.random()
    }
}