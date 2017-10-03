package io.jitrapon.android.template.game

import android.os.Bundle
import io.jitrapon.android.template.BaseActivity
import io.jitrapon.android.template.R
import io.jitrapon.android.template.util.addFragment
import io.jitrapon.android.template.util.setupActionBar
import kotlinx.android.synthetic.main.toolbar.*

class GameActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.empty_activity)

        tag = "GameScreen"
        setupActionBar(toolbar, {
            title = "Game"
        })

        savedInstanceState ?: addFragment(R.id.fragment_container, GameFragment(), GameFragment.TAG)
    }
}
