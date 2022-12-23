package space.mairi.movieapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import space.mairi.movieapp.databinding.ActivityMainBinding
import space.mairi.movieapp.ui.main.MainFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.getRoot()
        setContentView(view)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
    }
}