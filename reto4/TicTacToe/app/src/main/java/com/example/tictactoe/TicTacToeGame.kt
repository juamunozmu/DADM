package com.example.tictactoe

class TicTacToeGame {

    enum class DifficultyLevel {
        Easy,
        Harder,
        Expert
    }

    private var difficultyLevel = DifficultyLevel.Expert

    fun getDifficultyLevel(): DifficultyLevel {
        return difficultyLevel
    }

    fun setDifficultyLevel(level: DifficultyLevel) {
        difficultyLevel = level
    }
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

        return when (difficultyLevel) {

            DifficultyLevel.Easy -> {
                getRandomMove()
            }

            DifficultyLevel.Harder -> {
                val winningMove = getWinningMove()

                if (winningMove != -1) {
                    winningMove
                } else {
                    getRandomMove()
                }
            }

            DifficultyLevel.Expert -> {
                val winningMove = getWinningMove()

                if (winningMove != -1) {
                    winningMove
                } else {
                    val blockingMove = getBlockingMove()

                    if (blockingMove != -1) {
                        blockingMove
                    } else {
                        getRandomMove()
                    }
                }
            }
        }
    }
    private fun getRandomMove(): Int {

        val availablePositions = board.indices.filter {
            board[it] == OPEN_SPOT
        }

        if (availablePositions.isEmpty()) {
            return -1
        }

        return availablePositions.random()
    }

    private fun getWinningMove(): Int {

        for (position in board.indices) {

            if (board[position] == OPEN_SPOT) {

                board[position] = COMPUTER_PLAYER

                val winner = checkForWinner()

                board[position] = OPEN_SPOT

                if (winner == 3) {
                    return position
                }
            }
        }

        return -1
    }

    private fun getBlockingMove(): Int {

        for (position in board.indices) {

            if (board[position] == OPEN_SPOT) {

                board[position] = HUMAN_PLAYER

                val winner = checkForWinner()

                board[position] = OPEN_SPOT

                if (winner == 2) {
                    return position
                }
            }
        }

        return -1
    }
}