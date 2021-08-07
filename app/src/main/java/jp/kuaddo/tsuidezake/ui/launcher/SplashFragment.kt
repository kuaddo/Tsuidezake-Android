package jp.kuaddo.tsuidezake.ui.launcher

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import jp.kuaddo.tsuidezake.R

@AndroidEntryPoint
class SplashFragment : Fragment(R.layout.fragment_splash) {
    private val launcherViewModel: LauncherViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        launcherViewModel.hideActionBar()
    }
}
