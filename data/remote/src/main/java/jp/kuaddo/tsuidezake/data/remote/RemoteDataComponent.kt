package jp.kuaddo.tsuidezake.data.remote

import dagger.Component
import jp.kuaddo.tsuidezake.data.auth.AuthenticationComponent
import jp.kuaddo.tsuidezake.data.auth.DaggerAuthenticationComponent
import jp.kuaddo.tsuidezake.data.remote.internal.di.RemoteDataModule
import jp.kuaddo.tsuidezake.data.remote.internal.di.RemoteDataScope

@RemoteDataScope
@Component(
    modules = [RemoteDataModule::class],
    dependencies = [AuthenticationComponent::class]
)
interface RemoteDataComponent {
    val tsuidezakeService: TsuidezakeService

    @Component.Factory
    interface Factory {
        fun create(
            authenticationComponent: AuthenticationComponent =
                DaggerAuthenticationComponent.create()
        ): RemoteDataComponent
    }
}