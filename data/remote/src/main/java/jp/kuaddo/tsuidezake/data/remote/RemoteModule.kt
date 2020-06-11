package jp.kuaddo.tsuidezake.data.remote

import dagger.Module
import dagger.Provides
import jp.kuaddo.tsuidezake.data.remote.internal.di.RemoteSubcomponent
import javax.inject.Singleton

@Module(subcomponents = [RemoteSubcomponent::class])
object RemoteModule {
    @Provides
    @Singleton
    internal fun provideTsuidezakeService(builder: RemoteSubcomponent.Builder): TsuidezakeService {
        return builder.build().tsuidezakeServiceImpl()
    }
}
