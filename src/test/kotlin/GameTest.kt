package org.vmlakk

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream
import java.nio.charset.Charset

/**
 * Тестовый класс для проверки функциональности игры "Виселица".
 */
class GameTest {

    /**
     * Тест проверяет сценарий выигрыша в игре.
     * Вводятся правильные буквы слова "фишка".
     */
    @Test
    fun `test game win`() {
        val input = "ф\nи\nш\nк\nа\n"
        System.setIn(ByteArrayInputStream(input.toByteArray(Charset.defaultCharset())))
        val result = Game.play(getRandomWord = {"фишка"})
        assertTrue(result)
    }

    /**
     * Тест проверяет сценарий проигрыша в игре.
     * Вводятся неправильные буквы для слова "арбуз".
     */
    @Test
    fun `test game lose`() {
        val input = "ф\nи\nш\nк\nх\n"
        System.setIn(ByteArrayInputStream(input.toByteArray(Charset.defaultCharset())))
        val result = Game.play(getRandomWord = {"арбуз"})
        assertFalse(result)
    }
}
