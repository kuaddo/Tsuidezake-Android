package jp.kuaddo.tsuidezake.data.remote

import dagger.Module
import dagger.Provides
import jp.kuaddo.tsuidezake.core.scope.RepositoryScope
import jp.kuaddo.tsuidezake.data.remote.internal.di.RemoteSubcomponent

@Module(subcomponents = [RemoteSubcomponent::class])
object RemoteModule {
    @Provides
    @RepositoryScope
    internal fun provideTsuidezakeService(builder: RemoteSubcomponent.Builder): TsuidezakeService {
        return builder.build().tsuidezakeServiceImpl()
    }
}
