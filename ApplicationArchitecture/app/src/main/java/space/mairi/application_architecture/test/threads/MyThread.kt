package space.mairi.application_architecture.test.threads

import android.os.Looper

class MyThread : Thread() {
    override fun run() {
        Looper.prepare()
        Looper.loop()

    }

}