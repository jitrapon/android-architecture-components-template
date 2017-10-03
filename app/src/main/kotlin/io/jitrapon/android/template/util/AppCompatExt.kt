package io.jitrapon.android.template.util

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.support.annotation.IdRes
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import io.jitrapon.android.template.BaseFragment

/**
 * Various extension functions for AppCompatActivity
 *
 * @author Jitrapon Tiachunpun
 */

fun AppCompatActivity.setupActionBar(toolbar: Toolbar, action: ActionBar.() -> Unit) {
    setSupportActionBar(toolbar)
    supportActionBar?.run {
        action()
    }
}

/**
 * Adds a fragment into the container ViewGroup in this activity, specify toBackStack = true
 * to indicate that this transaction will be remembered, and back behavior is applied
 */
fun AppCompatActivity.addFragment(@IdRes container: Int, fragment: BaseFragment, fragmentTag: String? = null,
                                  toBackStack: Boolean = false, stateName: String? = null) {
    if (toBackStack) {
        supportFragmentManager.transact {
            addToBackStack(stateName)
            replace(container, fragment, fragmentTag)
        }
    }
    else {
        supportFragmentManager.transact {
            add(container, fragment, fragmentTag)
        }
    }
}

/**
 * Convenient function for retrieving the ViewModel based on its class name
 */
fun <T : ViewModel> AppCompatActivity.obtainViewModel(viewModelClass: Class<T>) =
        ViewModelProviders.of(this, ViewModelProvider.NewInstanceFactory()).get(viewModelClass)

/**
 * Runs a FragmentTransaction, then calls commit().
 */
private inline fun FragmentManager.transact(action: FragmentTransaction.() -> Unit) {
    beginTransaction().apply {
        action()
    }.commit()
}
