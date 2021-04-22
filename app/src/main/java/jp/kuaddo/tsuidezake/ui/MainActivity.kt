package jp.kuaddo.tsuidezake.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.ui.setupActionBarWithNavController
import com.wada811.databinding.dataBinding
import dagger.android.support.DaggerAppCompatActivity
import jp.kuaddo.tsuidezake.R
import jp.kuaddo.tsuidezake.databinding.ActivityMainBinding
import jp.kuaddo.tsuidezake.extensions.observeNonNull
import jp.kuaddo.tsuidezake.ui.common.BottomNavigationViewController
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity(R.layout.activity_main) {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: MainViewModel by viewModels { viewModelFactory }
    private val binding: ActivityMainBinding by dataBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel

        if (savedInstanceState == null) {
            setupBottomNavigation()
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        setupBottomNavigation()
    }

    private fun setupBottomNavigation() {
        val bottomNavController = BottomNavigationViewController(
            binding.navView,
            supportFragmentManager,
            containerId = R.id.nav_host_container,
            navGraphIds = listOf(
                R.navigation.ranking,
                R.navigation.wish,
                R.navigation.tasted,
                R.navigation.my_page
            )
        )

        // Whenever the selected controller changes, setup the action bar.
        // ActionBarOnDestinationChangedListenerが何度も追加されてしまうが、BottomNavigationの要素
        // が変化した際にAction barを更新する為に必要。また、追加したリスナーの削除は現状の
        // NavComponentの実装だと不可能なので諦めるしか無い。
        bottomNavController.setupWithNavController()
            .observeNonNull(this, ::setupActionBarWithNavController)
    }
}
