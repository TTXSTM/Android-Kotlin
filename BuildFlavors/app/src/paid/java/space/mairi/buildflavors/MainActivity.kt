package space.mairi.buildflavors

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import space.mairi.buildflavors.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.textView.text = getString(R.string.hello)

        binding.paidButton.setOnClickListener {
            Toast.makeText(this, "Paid version", Toast.LENGTH_LONG).show()
        }
    }
}