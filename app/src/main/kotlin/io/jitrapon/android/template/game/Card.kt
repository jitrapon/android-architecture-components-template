package io.jitrapon.android.template.game

/**
 * Base interface for all card types
 *
 * @author Jitrapon Tiachunpun
 */
interface Card {

    fun getText(): String
    fun isLastCard(): Boolean = false
}

class CardA : Card {

    override fun getText() = "A"
}

class CardB: Card {

    override fun getText() = "B"
}

class CardC: Card {

    override fun getText() = "C"
}

class CardD: Card {

    override fun getText() = "D"
    override fun isLastCard(): Boolean {
        return true
    }
}