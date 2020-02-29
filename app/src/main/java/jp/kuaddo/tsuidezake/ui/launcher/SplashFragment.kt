package jp.kuaddo.tsuidezake.ui.launcher

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import dagger.android.support.DaggerFragment
import jp.kuaddo.tsuidezake.R
import javax.inject.Inject

class SplashFragment : DaggerFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val launcherViewModel: LauncherViewModel by activityViewModels { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        launcherViewModel.hideActionBar()
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }
}