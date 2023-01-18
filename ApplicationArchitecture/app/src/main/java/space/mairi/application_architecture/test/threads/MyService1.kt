package space.mairi.application_architecture.test.threads

import android.app.Service
import android.content.Intent
import android.os.IBinder


class MyService1 : Service() {
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        //start HandlerThread
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        // post to HT
        return START_STICKY
    }

}