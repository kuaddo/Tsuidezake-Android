package jp.kuaddo.tsuidezake.data.remote

import dagger.Module
import dagger.Provides
import jp.kuaddo.tsuidezake.core.scope.RepositoryScope
import jp.kuaddo.tsuidezake.data.remote.internal.di.RemoteDataSubcomponent

@Module(subcomponents = [RemoteDataSubcomponent::class])
object RemoteDataModule {
    @Provides
    @RepositoryScope
    internal fun provideTsuidezakeService(builder: RemoteDataSubcomponent.Builder): TsuidezakeService {
        return builder.build().tsuidezakeServiceImpl()
    }
}