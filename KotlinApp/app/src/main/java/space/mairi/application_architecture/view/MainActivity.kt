package space.mairi.application_architecture.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import space.mairi.application_architecture.R
import space.mairi.application_architecture.databinding.MainActivityBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding : MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = MainActivityBinding.inflate(layoutInflater)
        val view = binding.getRoot()
        setContentView(view)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
    }
}