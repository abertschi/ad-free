@file:Suppress("NOTHING_TO_INLINE")
package org.jetbrains.anko

import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.widget.Toast

import android.content.Context
import timber.log.Timber

// XXX: Workaround to fix Anko Deprecation

interface AnkoLogger {
  val loggerTag: String get() = javaClass.simpleName
}

inline fun AnkoLogger.verbose(msg: () -> Any?) = Timber.tag(loggerTag).v(msg().toString())
inline fun AnkoLogger.debug  (msg: () -> Any?) = Timber.tag(loggerTag).d(msg().toString())
inline fun AnkoLogger.info   (msg: () -> Any?) = Timber.tag(loggerTag).i(msg().toString())
inline fun AnkoLogger.warn   (msg: () -> Any?) = Timber.tag(loggerTag).w(msg().toString())
inline fun AnkoLogger.error  (msg: () -> Any?) = Timber.tag(loggerTag).e(msg().toString())
inline fun AnkoLogger.wtf    (msg: () -> Any?) = Timber.tag(loggerTag).wtf(msg().toString())

// Direct message overloads (if you used info("x") somewhere)
inline fun AnkoLogger.verbose(msg: Any?) = Timber.tag(loggerTag).v(msg.toString())
inline fun AnkoLogger.debug  (msg: Any?) = Timber.tag(loggerTag).d(msg.toString())
inline fun AnkoLogger.info   (msg: Any?) = Timber.tag(loggerTag).i(msg.toString())
inline fun AnkoLogger.warn   (msg: Any?) = Timber.tag(loggerTag).w(msg.toString())
inline fun AnkoLogger.error  (msg: Any?) = Timber.tag(loggerTag).e(msg.toString())
inline fun AnkoLogger.wtf    (msg: Any?) = Timber.tag(loggerTag).wtf(msg.toString())


inline fun AnkoLogger.error(t: Throwable, msg: () -> Any? = { "" }) =
  Timber.tag(loggerTag).e(t, msg().toString())

inline fun AnkoLogger.warn(t: Throwable, msg: () -> Any? = { "" }) =
  Timber.tag(loggerTag).w(t, msg().toString())

fun Activity.runOnUiThread(action: () -> Unit) {
  if (Looper.myLooper() == Looper.getMainLooper()) action()
  else Handler(Looper.getMainLooper()).post(action)
}

fun Activity.longToast(message: String) =
  Toast.makeText(this, message, Toast.LENGTH_LONG).show()

fun Activity.longToast(message: Int) =
  Toast.makeText(this, message, Toast.LENGTH_LONG).show()

fun Context.runOnUiThread(action: () -> Unit) {
  if (Looper.myLooper() == Looper.getMainLooper()) action()
  else Handler(Looper.getMainLooper()).post(action)
}

fun Context.toast(message: CharSequence) =
  Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

fun Context.longToast(message: CharSequence) =
  Toast.makeText(this, message, Toast.LENGTH_LONG).show()