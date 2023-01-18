package space.mairi.application_architecture.test.threads

import android.app.IntentService
import android.content.Intent
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager

private const val TAG = "MainServiceTAG"
const val MAIN_SERVICE_STRING_EXTRA = "MainServiceExtra"
const val MAIN_SERVICE_INT_EXTRA = "MainServiceExtra"

class MainService(name : String = "MainService") : IntentService(name) {

    override fun onHandleIntent(intent: Intent?) {
        createLogMessage("onMandleIntent")

        intent?.let {
            sendBack(it.getIntExtra(MAIN_SERVICE_STRING_EXTRA, 0).toString())
        }

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createLogMessage("onStartComand")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        createLogMessage("onDestroy")
        super.onDestroy()
    }

    private fun createLogMessage(message : String) {
        Log.d(TAG, message)
    }

    private fun sendBack(result: String) {
        val broadcastIntent = Intent(TEST_BROADCAST_INTENT_FILTER).apply {
            putExtra(THREADS_FRAGMENT_BROADCAST_EXTRA, result)
        }

        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent)
    }

}