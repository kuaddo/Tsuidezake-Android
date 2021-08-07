package jp.kuaddo.tsuidezake.ui.launcher

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.wada811.databinding.dataBinding
import dagger.hilt.android.AndroidEntryPoint
import jp.kuaddo.tsuidezake.R
import jp.kuaddo.tsuidezake.databinding.ActivityLauncherBinding
import jp.kuaddo.tsuidezake.extensions.observeNonNull
import jp.kuaddo.tsuidezake.ui.MainActivity
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LauncherActivity : AppCompatActivity(R.layout.activity_launcher) {
    private val viewModel: LauncherViewModel by viewModels()
    private val binding: ActivityLauncherBinding by dataBinding()

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

        lifecycleScope.launch {
            viewModel.canStart.collect {
                if (it) startMainActivityWithFinish()
            }
        }
    }

    private fun startMainActivityWithFinish() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
