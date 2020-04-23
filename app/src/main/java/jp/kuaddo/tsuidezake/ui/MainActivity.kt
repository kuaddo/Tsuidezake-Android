package jp.kuaddo.tsuidezake.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.ui.setupActionBarWithNavController
import dagger.android.support.DaggerAppCompatActivity
import jp.kuaddo.tsuidezake.R
import jp.kuaddo.tsuidezake.databinding.ActivityMainBinding
import jp.kuaddo.tsuidezake.extensions.dataBinding
import jp.kuaddo.tsuidezake.extensions.observeNonNull
import jp.kuaddo.tsuidezake.extensions.setupWithNavController
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: MainViewModel by viewModels { viewModelFactory }
    private val binding by dataBinding<ActivityMainBinding>(R.layout.activity_main)
    private lateinit var currentNavController: LiveData<NavController>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel

        if (savedInstanceState == null) {
            setupBottomNavigation()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController.value?.navigateUp() ?: false
    }

    private fun setupBottomNavigation() {
        val controller = binding.navView.setupWithNavController(
            navGraphIds = listOf(
                R.navigation.ranking,
                R.navigation.finish_drinking,
                R.navigation.want_to_drink,
                R.navigation.my_page
            ),
            fragmentManager = supportFragmentManager,
            containerId = R.id.nav_host_container,
            intent = intent
        )

        // Whenever the selected controller changes, setup the action bar.
        controller.observeNonNull(this) { navController ->
            setupActionBarWithNavController(navController)
        }
        currentNavController = controller
    }
}
