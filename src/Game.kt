import java.io.File
import java.util.*

object Game {
    private var mistakes = 0
    private val correctLetters = mutableSetOf<Char>()
    private val wrongLetters = mutableSetOf<Char>()

    fun play() {
        val randomWord = getRandomWord().lowercase(Locale.getDefault())

        while (!HangmanStages.isFail(mistakes) && getMaskedWord(randomWord) != randomWord) {
            println(HangmanStages.getHangman(mistakes))
            println(getMaskedWord(randomWord))
            if (wrongLetters.isNotEmpty()) {
                println("Неверные буквы: $wrongLetters")
            }

            val inputLetter = GameInput.input("Введите букву: ")
            if (inputLetter == null) {
                println("Неверный ввод! Введите только русскую букву!")
                continue
            }
            if (correctLetters.contains(inputLetter) || wrongLetters.contains(inputLetter)) {
                println("Эта буква уже была введена, попробуйте еще раз")
                continue
            }
            if (randomWord.contains(inputLetter, true)) {
                println("Буква $inputLetter действительно есть в слове!")
                correctLetters.add(inputLetter)
            } else {
                println("Данной буквы нету в слове!")
                wrongLetters.add(inputLetter)
                mistakes++
            }
        }


        println(if (HangmanStages.isFail(mistakes)) "Вы проиграли!" else "Вы выиграли! поздравляем!")
    }

    fun playEndless() {
        while(true) {
            play()
            resetGame()
        }
    }

    private fun resetGame() {
        mistakes = 0
        correctLetters.clear()
        wrongLetters.clear()
    }

    private fun getRandomWord(): String {
        val file = File("assets/wordlist.txt")
        val shuffledWordList = file.readLines().shuffled()
        return shuffledWordList[0]
    }

    private fun getMaskedWord(word: String): String {
        return word.map { if (correctLetters.contains(it)) it else '_' }.joinToString("")
    }

    private object GameInput {
        fun input(message: String?): Char? {
            if (message != null) print(message)
            var input = readln().singleOrNull()?.lowercaseChar()
            return if (Regex("[а-я+]").matches(input.toString())) input else null
        }
    }
}