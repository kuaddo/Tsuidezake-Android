package jp.kuaddo.tsuidezake.ui.common

import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class BottomNavigationViewController(
    private val bottomNavigationView: BottomNavigationView,
    private val fragmentManager: FragmentManager,
    private val containerId: Int,
    private val navGraphIds: List<Int>
) {
    fun setupWithNavController(): LiveData<NavController> {
        // Result. Mutable live data with the selected controlled
        val selectedNavController = MutableLiveData<NavController>()

        val tagAndNavHostFragments = navGraphIds.map { navGraphId ->
            val fragmentTag = getFragmentTag(navGraphId)
            fragmentTag to obtainNavHostFragment(
                fragmentManager,
                fragmentTag,
                navGraphId,
                containerId
            )
        }
        val graphIdToTagMap = tagAndNavHostFragments.associate { (tag, fragment) ->
            fragment.navController.graph.id to tag
        }
        var selectedItemTag = graphIdToTagMap[bottomNavigationView.selectedItemId]

        tagAndNavHostFragments.forEach { (tag, fragment) ->
            val childFragmentManager = fragment.childFragmentManager
            childFragmentManager.addOnBackStackChangedListener {
                if (tag == selectedItemTag) {
                    bottomNavigationView.isVisible = childFragmentManager.backStackEntryCount == 0
                }
            }

            // Attach or detach nav host fragment depending on whether it's the selected item.
            if (fragment.navController.graph.id == bottomNavigationView.selectedItemId) {
                // Update livedata with the selected graph
                selectedNavController.value = fragment.navController
                bottomNavigationView.isVisible = childFragmentManager.backStackEntryCount == 0
                attachNavHostFragment(fragmentManager, fragment)
            } else {
                detachNavHostFragment(fragmentManager, fragment)
            }
        }

        // When a navigation item is selected
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            // Don't do anything if the state has already been saved.
            if (fragmentManager.isStateSaved) {
                return@setOnNavigationItemSelectedListener false
            }
            val newlySelectedItemTag = graphIdToTagMap[item.itemId]
            if (selectedItemTag == newlySelectedItemTag) {
                return@setOnNavigationItemSelectedListener false
            }

            val previousFragment = fragmentManager.findFragmentByTag(selectedItemTag)
            val newlySelectedFragment = fragmentManager.findFragmentByTag(newlySelectedItemTag)
                as NavHostFragment

            fragmentManager.beginTransaction()
                .apply { previousFragment?.let { detach(it) } }
                .attach(newlySelectedFragment)
                .setPrimaryNavigationFragment(newlySelectedFragment)
                .setReorderingAllowed(true)
                .commit()

            selectedItemTag = newlySelectedItemTag
            selectedNavController.value = newlySelectedFragment.navController
            true
        }

        setupItemReselected(fragmentManager, graphIdToTagMap)
        return selectedNavController
    }

    private fun detachNavHostFragment(
        fragmentManager: FragmentManager,
        navHostFragment: NavHostFragment
    ) {
        fragmentManager.beginTransaction()
            .detach(navHostFragment)
            .commitNow()
    }

    private fun attachNavHostFragment(
        fragmentManager: FragmentManager,
        navHostFragment: NavHostFragment
    ) {
        fragmentManager.beginTransaction()
            .attach(navHostFragment)
            .setPrimaryNavigationFragment(navHostFragment)
            .commitNow()
    }

    private fun obtainNavHostFragment(
        fragmentManager: FragmentManager,
        fragmentTag: String,
        navGraphId: Int,
        containerId: Int
    ): NavHostFragment {
        val existingFragment = fragmentManager.findFragmentByTag(fragmentTag) as? NavHostFragment
        existingFragment?.let { return it }

        val navHostFragment = NavHostFragment.create(navGraphId)
        fragmentManager.beginTransaction()
            .add(containerId, navHostFragment, fragmentTag)
            .commitNow()
        return navHostFragment
    }

    private fun setupItemReselected(
        fragmentManager: FragmentManager,
        graphIdToTagMap: Map<Int, String>
    ) {
        bottomNavigationView.setOnNavigationItemReselectedListener { item ->
            val newlySelectedItemTag = graphIdToTagMap[item.itemId]
            val selectedFragment = fragmentManager.findFragmentByTag(newlySelectedItemTag)
                as NavHostFragment
            val navController = selectedFragment.navController
            // Pop the back stack to the start destination of the current navController graph
            navController.popBackStack(
                navController.graph.startDestination,
                false
            )
        }
    }

    companion object {
        private fun getFragmentTag(navGraphId: Int) = "BottomNavigation#$navGraphId"
    }
}
