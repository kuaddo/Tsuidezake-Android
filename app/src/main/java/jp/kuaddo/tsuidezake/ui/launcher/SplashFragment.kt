package jp.kuaddo.tsuidezake.ui.launcher

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import dagger.android.support.DaggerFragment
import jp.kuaddo.tsuidezake.R
import javax.inject.Inject

class SplashFragment : DaggerFragment(R.layout.fragment_splash) {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val launcherViewModel: LauncherViewModel by activityViewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        launcherViewModel.hideActionBar()
    }
}