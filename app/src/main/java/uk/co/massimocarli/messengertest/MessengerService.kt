package uk.co.massimocarli.messengertest

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import android.util.Log
import kotlin.concurrent.thread

class MessengerService : Service(), Counter {

  companion object {
    const val TAG = "MessengerService"
    const val WHAT_START = 1
    const val WHAT_STOP = 2
  }

  lateinit var messenger: Messenger
  @Volatile
  var running = false

  override fun onCreate() {
    super.onCreate()
    log("onCreate")
  }

  override fun onBind(intent: Intent?): IBinder? {
    messenger = Messenger(LocalHandler(this))
    return messenger.binder
  }

  override fun startCounter() {
    running = true
    thread {
      for (i in 0..100) {
        if (!running) {
          break
        }
        Thread.sleep(1000)
        if (!running) {
          break
        }
        log("Count: $i")
      }
      log("Completed!")
    }
  }

  override fun stopCounter() {
    running = false;
  }

  internal class LocalHandler(
    val counter: Counter
  ) : Handler() {
    override fun handleMessage(msg: Message) {
      when (msg.what) {
        WHAT_START -> counter.startCounter()
        WHAT_STOP -> counter.stopCounter()
        else -> super.handleMessage(msg)
      }
    }
  }

  override fun onDestroy() {
    super.onDestroy()
    running = false
    log("onDestroy")
  }

  private fun log(msg: String) {
    Log.d(TAG, "-> $msg")
  }
}