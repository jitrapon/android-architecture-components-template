package io.jitrapon.android.template.util

import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.widget.Toast

/**
 * Various extension functions for Fragment
 *
 * @author Jitrapon Tiachunpun
 */

fun Fragment.finish() {
    activity?.finish()
}

fun Fragment.showMessage(message: String?, isDebug: Boolean = false) {
    activity?.let {
        if (!message.isNullOrBlank()) {
            if (isDebug) {
                Toast.makeText(it, message, Toast.LENGTH_LONG).show()
            }
            else {
                view?.let {
                    Handler().postDelayed({
                        Snackbar.make(it, message.toString(), Snackbar.LENGTH_LONG).show()
                    }, 200)
                }
            }
        }
    }
}

fun Fragment.showAlertDialog(title: String?, message: String?, positiveOptionText: String? = null,
                    onPositiveOptionClicked: (() -> Unit)? = null, negativeOptionText: String? = null,
                    onNegativeOptionClicked: (() -> Unit)? = null, isCancelable: Boolean = true,
                             onCancel: (() -> Unit)? = null): AlertDialog? {
    if (!message.isNullOrBlank()) {
        activity?.let {
            return AlertDialog.Builder(context).apply {
                if (!title.isNullOrBlank()) {
                    setTitle(title)
                }
                setMessage(message)
                if (!positiveOptionText.isNullOrBlank()) {
                    setPositiveButton(positiveOptionText, { _, _ ->
                        onPositiveOptionClicked?.invoke()
                    })
                }
                if (!negativeOptionText.isNullOrBlank()) {
                    setNegativeButton(negativeOptionText, { _, _ ->
                        onNegativeOptionClicked?.invoke()
                    })
                }
                setOnCancelListener {
                    onCancel?.invoke()
                }
            }.setCancelable(isCancelable).show()
        }
    }
    return null
}
