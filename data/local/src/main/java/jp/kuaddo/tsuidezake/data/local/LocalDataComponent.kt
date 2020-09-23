package jp.kuaddo.tsuidezake.data.local

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import jp.kuaddo.tsuidezake.data.local.internal.di.LocalDataModule
import jp.kuaddo.tsuidezake.data.local.internal.di.LocalDataScope

@LocalDataScope
@Component(
    modules = [LocalDataModule::class]
)
interface LocalDataComponent {
    val preferenceStorage: PreferenceStorage

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Context): LocalDataComponent
    }
}
