package org.vmlakk

import java.io.File
import java.util.*
import kotlin.time.DurationUnit
import kotlin.time.TimeSource

/**
 * Объект, реализующий логику игры "Виселица".
 * Включает основные игровые методы, такие как запуск игры, бесконечный режим и вспомогательные функции.
 */
object Game {
    private var mistakes = 0
    private val correctLetters = mutableSetOf<Char>()
    private val wrongLetters = mutableSetOf<Char>()

    /**
     * Запускает игру с возможностью выбора легкого режима и задания случайного слова.
     *
     * @param easyMode Флаг, указывающий на легкий режим. Если true, половина букв в слове будут открыты.
     * @param getRandomWord Функция для получения случайного слова. Если не указана, используется внутренняя функция.
     * @return true, если игрок выиграл; false, если проиграл.
     */
    fun play(easyMode: Boolean = false, getRandomWord: (() -> String)? = null): Boolean {
        resetGame()
        val timeSource = TimeSource.Monotonic
        val startGameTimeMark = timeSource.markNow()

        val randomWord = getRandomWord?.invoke() ?: getRandomWord().lowercase(Locale.getDefault())
        if(easyMode) openHalfLetters(randomWord)

        while (!HangmanStages.isFail(mistakes) && getMaskedWord(randomWord) != randomWord) {
            println(HangmanStages.getHangman(mistakes))
            println(getMaskedWord(randomWord))
            if (wrongLetters.isNotEmpty()) {
                println("Неверные буквы: $wrongLetters")
            }

            val inputLetter = GameInput.input("Введите букву или + для подсказки: ")
            if (inputLetter == null) {
                println("Неверный ввод! Введите только русскую букву!")
                continue
            }
            if(inputLetter == '+') {
                println("Подсказка - в это слове есть буква ${getAdvice(randomWord)}")
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

        val endGameTimeMark = timeSource.markNow()
        val playTime = endGameTimeMark - startGameTimeMark
        println(if (HangmanStages.isFail(mistakes)) "Вы проиграли!" else "Вы выиграли! поздравляем!")
        println("Ваша игра длилась ${playTime.inWholeMinutes} минут и ${playTime.toLong(DurationUnit.SECONDS) % 60} секунд")

        return !HangmanStages.isFail(mistakes)
    }

    /**
     * Запускает игру в бесконечном режиме.
     * Игра продолжается до тех пор, пока игрок не решит остановиться.
     *
     * @param easyMode Флаг, указывающий на легкий режим. Если true, половина букв в слове будут открыты.
     */
    fun playEndless(easyMode: Boolean = false) {
        while(true) {
            play(easyMode)
            resetGame()
        }
    }

    /**
     * Сбрасывает состояние игры, очищая списки правильных и неправильных букв и обнуляя счетчик ошибок.
     */
    private fun resetGame() {
        mistakes = 0
        correctLetters.clear()
        wrongLetters.clear()
    }

    /**
     * Получает случайное слово из файла "assets/wordlist.txt".
     * Файл должен содержать список слов, каждое из которых находится на отдельной строке.
     *
     * @return случайное слово из списка.
     */
    private fun getRandomWord(): String {
        val file = File("assets/wordlist.txt")
        val shuffledWordList = file.readLines().shuffled()
        return shuffledWordList[0]
    }

    /**
     * Открывает половину букв в слове для облегченного режима игры.
     *
     * @param word Слово, в котором будут открыты буквы.
     */
    private fun openHalfLetters(word: String) {
        val lettersToOpenCount = word.length / 2
        var alreadyOpen = 0
        for (letter in word) {
            if (correctLetters.contains(letter)) continue

            alreadyOpen += word.count { it == letter }
            correctLetters.add(letter)

            if (alreadyOpen >= lettersToOpenCount) break
        }
    }

    /**
     * Возвращает текущее состояние угадываемого слова с учетом открытых букв.
     *
     * @param word Слово, которое угадывает игрок.
     * @return строка, представляющая текущее состояние угадываемого слова.
     */
    private fun getMaskedWord(word: String): String {
        return word.map { if (correctLetters.contains(it)) it else '_' }.joinToString("")
    }

    /**
     * Предоставляет подсказку, возвращая первую неотгаданную букву в слове.
     *
     * @param word Слово, которое угадывает игрок.
     * @return первая неотгаданная буква в слове.
     */
    private fun getAdvice(word: String) : Char {
        val maskedWord = getMaskedWord(word)
        return word.zip(maskedWord).first { it.first != it.second }.first
    }

    /**
     * Внутренний объект для обработки ввода пользователя.
     */
    private object GameInput {
        /**
         * Считывает ввод пользователя и проверяет корректность введенной буквы.
         *
         * @param message Сообщение, отображаемое пользователю перед вводом.
         * @return введенная буква или null, если ввод некорректен.
         */
        fun input(message: String?): Char? {
            if (message != null) print(message)
            val input = readln().singleOrNull()?.lowercaseChar()
            return if (Regex("[а-я+]").matches(input.toString())) input else null
        }
    }
}
