package jp.kuaddo.tsuidezake.data.remote

import dagger.BindsInstance
import dagger.Component
import jp.kuaddo.tsuidezake.data.auth.AuthService
import jp.kuaddo.tsuidezake.data.remote.internal.di.RemoteDataModule
import jp.kuaddo.tsuidezake.data.remote.internal.di.RemoteDataScope
import jp.kuaddo.tsuidezake.data.repository.TsuidezakeService

@RemoteDataScope
@Component(
    modules = [RemoteDataModule::class]
)
interface RemoteDataComponent {
    val tsuidezakeService: TsuidezakeService

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance authService: AuthService): RemoteDataComponent
    }
}
