package jp.kuaddo.tsuidezake.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.ui.setupActionBarWithNavController
import com.wada811.databinding.dataBinding
import dagger.hilt.android.AndroidEntryPoint
import jp.kuaddo.tsuidezake.R
import jp.kuaddo.tsuidezake.databinding.ActivityMainBinding
import jp.kuaddo.tsuidezake.ui.common.BottomNavigationViewController
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private val viewModel: MainViewModel by viewModels()
    private val binding: ActivityMainBinding by dataBinding()
    private lateinit var bottomNavigationViewController: BottomNavigationViewController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel

        if (savedInstanceState == null) {
            setupBottomNavigation()
        }

        lifecycleScope.launch { viewModel.showRankingEvent.collect { showRankingFragment() } }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        setupBottomNavigation()
    }

    override fun onSupportNavigateUp(): Boolean {
        return bottomNavigationViewController.navigateUp()
    }

    private fun setupBottomNavigation() {
        bottomNavigationViewController = BottomNavigationViewController(
            binding.navView,
            supportFragmentManager,
            containerId = R.id.nav_host_container,
            navGraphIds = listOf(
                R.navigation.ranking,
                R.navigation.wish,
                R.navigation.tasted,
                R.navigation.my_page
            ),
            // ActionBarOnDestinationChangedListenerが何度も追加されてしまうが、BottomNavigationの
            // 要素が変化した際にAction barを更新する為に必要。また、追加したリスナーの削除は現状の
            // NavComponentの実装だと不可能なので諦めるしか無い。
            ::setupActionBarWithNavController
        )
        bottomNavigationViewController.setupWithNavController()
    }

    private fun showRankingFragment() = bottomNavigationViewController.goTo(R.navigation.ranking)
}
