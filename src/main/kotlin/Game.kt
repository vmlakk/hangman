package org.vmlakk

import java.io.File
import java.util.*
import kotlin.time.DurationUnit
import kotlin.time.TimeSource

object Game {
    private var mistakes = 0
    private val correctLetters = mutableSetOf<Char>()
    private val wrongLetters = mutableSetOf<Char>()

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

    fun playEndless(easyMode: Boolean = false) {
        while(true) {
            play(easyMode)
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

    private fun getMaskedWord(word: String): String {
        return word.map { if (correctLetters.contains(it)) it else '_' }.joinToString("")
    }

    private fun getAdvice(word: String) : Char {
        val maskedWord = getMaskedWord(word)
        return word.zip(maskedWord).first { it.first != it.second }.first
    }

    private object GameInput {
        fun input(message: String?): Char? {
            if (message != null) print(message)
            val input = readln().singleOrNull()?.lowercaseChar()
            return if (Regex("[а-я+]").matches(input.toString())) input else null
        }
    }
}
