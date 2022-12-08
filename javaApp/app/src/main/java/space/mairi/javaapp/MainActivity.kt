package space.mairi.javaapp

import android.app.Activity
import android.icu.text.CaseMap.Title
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import space.mairi.javaapp.R

class MainActivity : Activity() {
    data class Note(val title: String, val note: String)
    override fun onCreate(icicle: Bundle?) {
        super.onCreate(icicle)
        setContentView(R.layout.activity_main)
        val title = findViewById<View>(R.id.title) as TextView
        val note = findViewById<View>(R.id.note) as TextView
        val button = findViewById<View>(R.id.button) as Button
        button.setOnClickListener {
            val n : Note = Note("Title", "This is note!")
            title.setText(n.title)
            note.setText(n.note)

            for (i in 10 downTo 1 step 2){
                println("Hello Kotlin!")
            }
        }
    }


}

