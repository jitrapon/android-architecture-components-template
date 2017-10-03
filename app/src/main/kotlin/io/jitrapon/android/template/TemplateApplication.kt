package io.jitrapon.android.template

import android.app.Application
import android.support.v7.app.AppCompatDelegate
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger

/**
 * @author Jitrapon Tiachunpun
 */
class TemplateApplication : Application() {

    companion object {
        init {
            // allow vector to be renderable in old devices
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }
    }

    override fun onCreate() {
        super.onCreate()

        FacebookSdk.sdkInitialize(applicationContext)
        AppEventsLogger.activateApp(this)
    }
}
