package jp.kuaddo.tsuidezake.data.remote

import dagger.Component
import jp.kuaddo.tsuidezake.data.remote.internal.di.RemoteDataModule
import jp.kuaddo.tsuidezake.data.remote.internal.di.RemoteDataScope

@RemoteDataScope
@Component(
    modules = [RemoteDataModule::class]
)
interface RemoteDataComponent {
    val tsuidezakeService: TsuidezakeService

    @Component.Factory
    interface Factory {
        fun create(): RemoteDataComponent
    }
}