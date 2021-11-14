package gq.kirmanak.mealie

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import gq.kirmanak.mealie.databinding.MainActivityBinding
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.v("onCreate() called with: savedInstanceState = $savedInstanceState")
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
    }
}