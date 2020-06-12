package jp.kuaddo.tsuidezake.data.local

import dagger.Module
import dagger.Provides
import jp.kuaddo.tsuidezake.data.local.internal.di.LocalDataSubcomponent

@Module(subcomponents = [LocalDataSubcomponent::class])
object LocalDataModule {
    @Provides
    internal fun providePreferenceStorage(factory: LocalDataSubcomponent.Factory): PreferenceStorage {
        return factory.create().sharedPreferenceStorage()
    }
}
