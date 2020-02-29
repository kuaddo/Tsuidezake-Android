package jp.kuaddo.tsuidezake.ui.launcher

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import dagger.android.support.DaggerAppCompatActivity
import jp.kuaddo.tsuidezake.R
import jp.kuaddo.tsuidezake.databinding.ActivityLauncherBinding
import jp.kuaddo.tsuidezake.extensions.dataBinding
import jp.kuaddo.tsuidezake.extensions.observeNonNull
import jp.kuaddo.tsuidezake.ui.MainActivity
import javax.inject.Inject

class LauncherActivity : DaggerAppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: LauncherViewModel by viewModels { viewModelFactory }
    private val binding by dataBinding<ActivityLauncherBinding>(R.layout.activity_launcher)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host)!!
        setupActionBarWithNavController(navHostFragment.findNavController())
        observe()
    }

    private fun observe() {
        viewModel.isVisibleActionBar.observeNonNull(this) {
            if (it) supportActionBar?.show()
            else supportActionBar?.hide()
        }

        viewModel.isInitialized.observeNonNull(this) {
            if (it) startMainActivityWithFinish()
        }
    }

    private fun startMainActivityWithFinish() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
