package jp.kuaddo.tsuidezake.util

import androidx.activity.result.ActivityResultRegistry
import androidx.lifecycle.LifecycleOwner
import dagger.assisted.AssistedFactory
import jp.kuaddo.tsuidezake.authui.SignInManager

// DaggerでAssistedFactoryを@Bindsや@Providesでinjectすることはできないのでapp moduleに集める。
// Error message: "Dagger does not support providing @AssistedFactory types."

@AssistedFactory
interface SignInManagerFactory {
    fun create(
        lifecycleOwner: LifecycleOwner,
        activityResultRegistry: ActivityResultRegistry
    ): SignInManager
}
