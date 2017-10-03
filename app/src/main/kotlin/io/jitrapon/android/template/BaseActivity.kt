package io.jitrapon.android.template

import android.support.v7.app.AppCompatActivity

/**
 * Wrapper around Android's AppCompatActivity. Contains convenience functions
 * relating to fragment transactions, activity transitions, Android's new runtime permission handling,
 * analytics, and more. All activities should extend from this class.
 *
 * @author Jitrapon Tiachunpun
 */
abstract class BaseActivity : AppCompatActivity() {

    /* subclass should overwrite this variable for naming the activity */
    var tag: String = "base"
}
