package gq.kirmanak.mealie

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import gq.kirmanak.mealie.databinding.MainActivityBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}