import kotlin.system.exitProcess

fun main() {
    while (true) {
        print(
            "Выберите режим игры\n" +
                    "1 - Обычная игра\n" +
                    "2 - Легкая игра\n" +
                    "3 - Бесконечная игра\n" +
                    "4 - Бесконечная легкая игра\n"
        )
        val choice = readln().toIntOrNull()
        when (choice) {
            1 -> {
                Game.play()
                exitProcess(0)
            }
            2 -> {
                Game.play(true)
                exitProcess(0)
            }
            3 -> {
                Game.playEndless()
            }
            4 -> {
                Game.playEndless(true)
            }
            else -> {
                println("Неверный выбор, попробуйте еще раз")
            }
        }
    }
}

/* Для игры нужно:
* 1. Список слов (разные языки)
* 2. Разные виселицы
* 3. Ввод значений
* 4. Вывод отгаданных букв
* 5. Конечно число попыток
* 6. Время игры
* 7. Статистика игры
* 8. Мемоизация
* 9. Подсказки
*/