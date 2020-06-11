package jp.kuaddo.tsuidezake.data.local

import dagger.Module
import dagger.Provides
import jp.kuaddo.tsuidezake.core.scope.RepositoryScope
import jp.kuaddo.tsuidezake.data.local.internal.di.LocalSubcomponent

@Module(subcomponents = [LocalSubcomponent::class])
object LocalModule {
    @Provides
    @RepositoryScope
    internal fun providePreferenceStorage(builder: LocalSubcomponent.Builder): PreferenceStorage {
        return builder.build().sharedPreferenceStorage()
    }
}
