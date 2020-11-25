package jp.kuaddo.tsuidezake.data.local

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import jp.kuaddo.tsuidezake.data.local.internal.di.LocalDataModule
import jp.kuaddo.tsuidezake.data.local.internal.di.LocalDataScope
import jp.kuaddo.tsuidezake.data.repository.LocalDataSource
import jp.kuaddo.tsuidezake.data.repository.PreferenceStorage

@LocalDataScope
@Component(
    modules = [LocalDataModule::class]
)
interface LocalDataComponent {
    val preferenceStorage: PreferenceStorage
    val localDataSource: LocalDataSource

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Context): LocalDataComponent
    }
}
