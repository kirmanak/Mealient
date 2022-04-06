package gq.kirmanak.mealient.ui.addaccount

import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import gq.kirmanak.mealient.R

@AndroidEntryPoint
class AddAccountActivity : AppCompatActivity() {

    private val viewModel by viewModels<AddAccountViewModel>()

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        supportActionBar?.title = getString(R.string.app_name)
    }
}