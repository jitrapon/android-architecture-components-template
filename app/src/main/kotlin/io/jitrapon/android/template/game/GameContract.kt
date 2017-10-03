package io.jitrapon.android.template.game

/**
 * This interface contains blueprints for what the view should do for Game Screen
 * and the necessary business logic functions required
 *
 * @author Jitrapon Tiachunpun
 */
interface GameContract {

    /* what the view should be able to do */
    interface View {

        fun showSnackMessage(message: String?)

        fun showCards(cards: ArrayList<Card>?)
    }

    /* Card UI State model */
    class CardUI(val text: String, val showExitButton: Boolean)

    /* controller for business logic of the game view */
    interface ViewModel {

        /* show greeting message when the user first launch the app */
        fun greetUser()

        /* load all the cards to be displayed */
        fun loadCards()

        /* get list of the cards loaded */
        fun getCardSize(): Int

        /* get the UI state of each card at a specified position */
        fun getCardState(position: Int): CardUI?

        /* called when card starts to scroll */
        fun onCardBeginScroll()

        /* called when card is scrolled to this position */
        fun onCardScrolledToPosition(position: Int)

        /* called when the card's exit button is clicked */
        fun onCardExitButtonClicked(position: Int)
    }
}