package jp.kuaddo.tsuidezake.data.local

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jp.kuaddo.tsuidezake.data.local.internal.di.LocalDataModule
import jp.kuaddo.tsuidezake.data.local.internal.di.LocalDataScope
import jp.kuaddo.tsuidezake.data.repository.LocalDataSource
import jp.kuaddo.tsuidezake.data.repository.PreferenceStorage
import javax.inject.Singleton

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

    @Module
    @InstallIn(SingletonComponent::class)
    object HiltModule {
        @Provides
        @Singleton
        fun provideLocalDataComponent(@ApplicationContext context: Context): LocalDataComponent =
            DaggerLocalDataComponent.factory().create(context)

        @Provides
        fun providePreferenceStorage(component: LocalDataComponent): PreferenceStorage =
            component.preferenceStorage

        @Provides
        fun provideLocalDataSource(component: LocalDataComponent): LocalDataSource =
            component.localDataSource
    }
}
