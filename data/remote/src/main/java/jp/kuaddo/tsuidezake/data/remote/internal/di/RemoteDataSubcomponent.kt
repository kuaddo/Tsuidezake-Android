package jp.kuaddo.tsuidezake.data.remote.internal.di

import dagger.Subcomponent
import jp.kuaddo.tsuidezake.data.remote.internal.TsuidezakeServiceImpl

@RemoteDataScope
@Subcomponent(
    modules = [ApiModule::class]
)
internal interface RemoteDataSubcomponent {

    fun tsuidezakeServiceImpl(): TsuidezakeServiceImpl

    @Subcomponent.Builder
    interface Builder {
        fun build(): RemoteDataSubcomponent
    }
}