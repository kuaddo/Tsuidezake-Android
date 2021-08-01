package jp.kuaddo.tsuidezake.data.remote

import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jp.kuaddo.tsuidezake.data.remote.internal.di.RemoteDataModule
import jp.kuaddo.tsuidezake.data.remote.internal.di.RemoteDataScope
import jp.kuaddo.tsuidezake.data.repository.TsuidezakeService
import javax.inject.Singleton

@RemoteDataScope
@Component(
    modules = [RemoteDataModule::class]
)
interface RemoteDataComponent {
    val tsuidezakeService: TsuidezakeService

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance authToken: AuthToken): RemoteDataComponent
    }

    @Module
    @InstallIn(SingletonComponent::class)
    object HiltModule {
        @Provides
        @Singleton
        fun provideRemoteDataComponent(authToken: AuthToken): RemoteDataComponent =
            DaggerRemoteDataComponent.factory().create(authToken)

        @Provides
        fun provideTsuidezakeService(component: RemoteDataComponent): TsuidezakeService =
            component.tsuidezakeService
    }
}
