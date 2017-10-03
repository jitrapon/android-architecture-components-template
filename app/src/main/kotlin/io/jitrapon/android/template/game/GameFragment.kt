package io.jitrapon.android.template.game

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.facebook.CallbackManager
import io.jitrapon.android.template.BaseFragment
import io.jitrapon.android.template.R
import io.jitrapon.android.template.authentication.FacebookAuth
import io.jitrapon.android.template.util.CardSnapHelper
import io.jitrapon.android.template.util.finish
import io.jitrapon.android.template.util.showAlertDialog
import io.jitrapon.android.template.util.showMessage
import kotlinx.android.synthetic.main.game_fragment.*

/**
 * Main Fragment containing the main game interface when user launches the app
 *
 * @author Jitrapon Tiachunpun
 */
class GameFragment : BaseFragment(), GameContract.View {

    companion object {
        const val TAG = "GameFragment"
    }

    private var cardSnapHelper: CardSnapHelper = CardSnapHelper()

    private val viewModel: GameViewModel by lazy {
        ViewModelProviders.of(this).get(GameViewModel::class.java)
    }

    private var loginDialog: AlertDialog? = null

    private val fbCallbackManager: CallbackManager by lazy {
        CallbackManager.Factory.create()
    }

    //region fragment lifecycle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycle.addObserver(viewModel)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.game_fragment, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        cardRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = CardAdapter(viewModel)
            cardSnapHelper.attachToRecyclerView(this)
            addOnScrollListener(object : RecyclerView.OnScrollListener() {

                override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                    if (dx > 0) {
                        viewModel.onCardBeginScroll()
                    }
                }

                override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)

                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        if (!viewModel.isLoggedIn()) {
                            cardRecyclerView.smoothScrollToPosition(0)
                        }
                        viewModel.onCardScrolledToPosition(cardSnapHelper.getSnapPosition())
                    }
                }
            })
        }

        // on message changed
        viewModel.getMessage().observe(this, Observer {
            showSnackMessage(it)
        })

        // on card list changed
        viewModel.getCards().observe(this, Observer {
            showCards(it)
        })

        // on show alert dialog
        viewModel.shouldShowFbLoginDialog().observe(this, Observer {
            it?.let {
                if (loginDialog == null) {
                    loginDialog = showAlertDialog(
                            title = getString(R.string.game_fb_dialog_title),
                            message = getString(R.string.game_fb_dialog_message),
                            onCancel = {
                                loginDialog = null
                            },
                            positiveOptionText = getString(R.string.game_fb_dialog_button),
                            onPositiveOptionClicked = {
                                FacebookAuth.apply {
                                    logout()
                                    registerCallback(fbCallbackManager, viewModel.onHandleFacebookCallback)
                                    login(this@GameFragment, arrayListOf(""))
                                }
                                loginDialog = null
                            })
                }
            }
        })

        // on finish view
        viewModel.shouldExit().observe(this, Observer {
            finish()
        })

        // on remove index
        viewModel.getRemoveIndex().observe(this, Observer {
            it?.let {
                cardRecyclerView.adapter.notifyItemRemoved(it)
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data != null && resultCode != Activity.RESULT_CANCELED) {
            fbCallbackManager.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onDestroy() {
        lifecycle.removeObserver(viewModel)

        super.onDestroy()
    }

    //endregion
    //region view contracts

    override fun showSnackMessage(message: String?) {
        showMessage(message)
    }

    override fun showCards(cards: ArrayList<Card>?) {
        cardRecyclerView.adapter.notifyDataSetChanged()
    }

    //endregion
}
