package jp.kuaddo.tsuidezake.data.remote.internal.di

import dagger.Subcomponent
import jp.kuaddo.tsuidezake.data.remote.internal.TsuidezakeServiceImpl

@RemoteScope
@Subcomponent(
    modules = [ApiModule::class]
)
internal interface RemoteSubcomponent {

    fun tsuidezakeServiceImpl(): TsuidezakeServiceImpl

    @Subcomponent.Builder
    interface Builder {
        fun build(): RemoteSubcomponent
    }
}