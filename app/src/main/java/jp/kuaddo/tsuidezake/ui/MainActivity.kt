package jp.kuaddo.tsuidezake.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.wada811.databinding.dataBinding
import dagger.hilt.android.AndroidEntryPoint
import jp.kuaddo.tsuidezake.R
import jp.kuaddo.tsuidezake.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private val viewModel: MainViewModel by viewModels()
    private val binding: ActivityMainBinding by dataBinding()
    private lateinit var navController: NavController
    private lateinit var appBarConfig: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_container) as NavHostFragment
        navController = navHostFragment.navController
        binding.navView.setupWithNavController(navController)

        appBarConfig = AppBarConfiguration(
            setOf(
                R.id.menu_ranking,
                R.id.menu_wish,
                R.id.menu_tasted,
                R.id.menu_my_page
            )
        )
        setupActionBarWithNavController(navController, appBarConfig)
        lifecycleScope.launch { viewModel.showRankingEvent.collect { showRankingFragment() } }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfig)
    }

    private fun showRankingFragment() {
        binding.navView.selectedItemId = R.id.menu_ranking
    }
}
