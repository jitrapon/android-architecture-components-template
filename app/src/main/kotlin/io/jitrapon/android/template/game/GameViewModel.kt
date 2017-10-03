package io.jitrapon.android.template.game

import android.app.Application
import android.arch.lifecycle.*
import android.support.annotation.StringRes
import android.util.Log
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginResult
import io.jitrapon.android.template.R
import io.jitrapon.android.template.SingleLiveEvent
import io.jitrapon.android.template.TemplateApplication

/**
 * Controls the presentation of the GameView and the business logic for playing the game
 * TODO for simple LiveData, simply set it, for complex ones, create base class for LiveDataModel
 * that contains data and error (to be displayed). LiveDataModels are models that is a live data,
 * but is also UI-display ready, where the View can use it directly to display data. This is in contrast
 * to the RepositoryModel
 *
 * @author Jitrapon Tiachunpun
 */
class GameViewModel(context: Application) :
        LifecycleObserver,
        GameContract.ViewModel,
        AndroidViewModel(context) {

    /* message to be observed by the view */
    private val message = SingleLiveEvent<String>()

    /* data to be shown as cards */
    private val cards = MutableLiveData<ArrayList<Card>>()

    /* whether or not the user has logged in */
    private var isLoggedIn: Boolean = false

    /* whether or not the view should show facebook dialog message */
    private val shouldShowFbLoginDialog = SingleLiveEvent<Boolean>()

    /* trigger to close the view */
    private val shouldFinishView = MutableLiveData<Boolean>()

    /* the index of the card to be removed */
    private val removeIndex = MutableLiveData<Int>()

    /* the previously scrolled card position */
    private var prevScrollPos: Int = 0

    private val fbLogger by lazy {
        AppEventsLogger.newLogger(getApplication())
    }

    val onHandleFacebookCallback = object : FacebookCallback<LoginResult> {

        override fun onCancel() {
            //nothing happens
        }

        override fun onSuccess(result: LoginResult?) {
            isLoggedIn = true
            message.value = getString(R.string.game_login_success_message)
        }

        override fun onError(error: FacebookException?) {
            message.value = getString(R.string.game_login_failure_message)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    override fun greetUser() {
        message.value = getString(R.string.game_greeting_message)
        loadCards()
    }

    /**
     * This will be retrieved using a Repository, but for now mock in-memory data
     */
    override fun loadCards() {
        cards.value = ArrayList<Card>().apply {
            add(CardA())
            add(CardB())
            add(CardC())
            add(CardD())
        }
    }

    /**
     * Handle view events when it starts to scroll. If the user is not logged in,
     * show facebook login dialog
     */
    override fun onCardBeginScroll() {
        if (!isLoggedIn) {
            shouldShowFbLoginDialog.value = true
        }
    }

    override fun getCardSize() = cards.value?.size ?: 0

    override fun getCardState(position: Int): GameContract.CardUI? {
        cards.value?.let {
            if (it.isNotEmpty() && position >= 0 && position < it.size) {
                return GameContract.CardUI(it[position].getText(), it[position].isLastCard())
            }
        }
        return null
    }

    override fun onCardScrolledToPosition(position: Int) {
        if (isLoggedIn) {
            if (position > prevScrollPos) {
                prevScrollPos = position

                cards.value?.let {
                    if (it.size > 1) {
                        it.removeAt(0)
                        removeIndex.value = 0
                        prevScrollPos = 0

                        // log event
                        Log.d("DEBUG", "Scroll to card ${cards.value?.get(0)?.getText()}")
                        fbLogger.logEvent("Card${cards.value?.get(0)?.getText()}")
                    }
                }
            }
        }
    }

    override fun onCardExitButtonClicked(position: Int) {
        shouldFinishView.value = true
    }

    //region lifecycle data

    fun getMessage(): LiveData<String> = message

    fun getCards(): LiveData<ArrayList<Card>> = cards

    fun shouldShowFbLoginDialog(): LiveData<Boolean> = shouldShowFbLoginDialog

    fun shouldExit(): LiveData<Boolean> = shouldFinishView

    fun getRemoveIndex(): LiveData<Int> = removeIndex

    //endregion

    private fun getString(@StringRes id: Int) = getApplication<TemplateApplication>().resources.getString(id)
}
