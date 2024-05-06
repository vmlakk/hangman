package org.vmlakk

object HangmanStages {

    fun getHangman(mistakes: Int): String {
        return hangmanStages[mistakes]
    }

    fun isFail(mistakes: Int): Boolean {
        return mistakes >= hangmanStages.size - 1
    }

    private val hangmanStages = listOf(
        """
         |
         |
         |
         |
         |
     """,
        """
         |———|
         |
         |
         |
         |
     """,
        """
         |———|
         |   O
         |
         |
         |
     """,
        """
         |———|
         |   O
         |   |
         |
         |
     """,
        """
         |———|
         |   O
         |  /|\
         |
         |
     """,
        """
         |———|
         |   O
         |  /|\
         |  / \
         |
     """
    ).map { it.trimIndent() }
}
