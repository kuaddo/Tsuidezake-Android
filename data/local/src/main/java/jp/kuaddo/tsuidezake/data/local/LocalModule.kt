package jp.kuaddo.tsuidezake.data.local

import dagger.Module
import dagger.Provides
import jp.kuaddo.tsuidezake.data.local.internal.di.LocalSubcomponent
import javax.inject.Singleton

@Module(subcomponents = [LocalSubcomponent::class])
object LocalModule {
    @Provides
    @Singleton
    internal fun providePreferenceStorage(builder: LocalSubcomponent.Builder): PreferenceStorage {
        return builder.build().sharedPreferenceStorage()
    }
}
