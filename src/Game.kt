import java.io.File
import java.util.*
import kotlin.time.DurationUnit
import kotlin.time.TimeSource

object Game {
    private var mistakes = 0
    private val correctLetters = mutableSetOf<Char>()

    fun play() {
        val timeSource = TimeSource.Monotonic
        val startGameTimeMark = timeSource.markNow()

        val randomWord = getRandomWord().lowercase(Locale.getDefault())

        while (!HangmanStages.isFail(mistakes) && getMaskedWord(randomWord) != randomWord) {
            mistakes = 0
            println(HangmanStages.getHangman(mistakes))
            println(getMaskedWord(randomWord))

            val inputLetter = GameInput.input("Введите букву: ")
            if (inputLetter == null) {
                println("Неверный ввод! Введите только букву!")
                continue
            }
            if (correctLetters.contains(inputLetter)) {
                println("Эта буква уже была введена, попробуйте еще раз")
                continue
            }
            if (randomWord.contains(inputLetter, true)) {
                println("Буква $inputLetter действительно есть в слове!")
                correctLetters.add(inputLetter)
            } else {
                println("Данной буквы нету в слове!")
                mistakes++
            }
        }

        val endGameTimeMark = timeSource.markNow()
        val playTime = endGameTimeMark - startGameTimeMark
        println(if (HangmanStages.isFail(mistakes)) "Вы проиграли!" else "Вы выиграли! поздравляем!")
        println("Ваша игра длилась ${playTime.inWholeMinutes} минут и ${playTime.toLong(DurationUnit.SECONDS) % 60} секунд")
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
            return readln().singleOrNull()?.lowercaseChar()
        }
    }
}