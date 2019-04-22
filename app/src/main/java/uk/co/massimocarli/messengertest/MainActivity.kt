package uk.co.massimocarli.messengertest

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

  companion object {
    const val WHAT_START = 1
    const val WHAT_STOP = 2
  }

  private var bounded = false
  private var messenger: Messenger? = null

  private val connection = object : ServiceConnection {

    override fun onServiceConnected(className: ComponentName, service: IBinder) {
      bounded = true
      messenger = Messenger(service)
    }

    override fun onServiceDisconnected(arg0: ComponentName) {
      bounded = false
      messenger = null
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
  }

  override fun onStart() {
    super.onStart()
    Intent(this, MessengerService::class.java).also { intent ->
      bindService(intent, connection, Context.BIND_AUTO_CREATE)
    }
  }

  override fun onStop() {
    super.onStop()
    unbindService(connection)
  }

  fun startCounter(view: View) {
    if (bounded) {
      val msg: Message = Message.obtain(null, WHAT_START, 0, 0)
      messenger?.send(msg)
    }
  }

  fun stopCounter(view: View) {
    if (bounded) {
      val msg: Message = Message.obtain(null, WHAT_STOP, 0, 0)
      messenger?.send(msg)
    }
  }
}
