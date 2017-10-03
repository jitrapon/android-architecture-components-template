package io.jitrapon.android.template.authentication

import android.support.v4.app.Fragment
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import java.lang.Exception

/**
 * Convenient class to wrap around Facebook SDK
 *
 * @author Jitrapon Tiachunpun
 */
object FacebookAuth {

    fun login(fragment: Fragment, permissions: Collection<String>) {
        LoginManager.getInstance().logInWithReadPermissions(fragment, permissions)
    }

    fun registerCallback(callbackManager: CallbackManager, callback: FacebookCallback<LoginResult>) {
        LoginManager.getInstance().registerCallback(callbackManager, callback)
    }

    fun logout() {
        try {
            LoginManager.getInstance().logOut()
        }
        catch (ex: Exception) {
            ex.printStackTrace()
        }
    }
}
