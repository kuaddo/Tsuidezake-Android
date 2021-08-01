package jp.kuaddo.tsuidezake.data.repository

import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jp.kuaddo.tsuidezake.data.repository.internal.di.RepositoryModule
import jp.kuaddo.tsuidezake.data.repository.internal.di.RepositoryScope
import jp.kuaddo.tsuidezake.domain.Repository
import javax.inject.Singleton

@RepositoryScope
@Component(
    modules = [RepositoryModule::class]
)
interface RepositoryComponent {
    val repository: Repository

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance preferenceStorage: PreferenceStorage,
            @BindsInstance localDataSource: LocalDataSource,
            @BindsInstance tsuidezakeService: TsuidezakeService,
            @BindsInstance authService: AuthService
        ): RepositoryComponent
    }

    @Module
    @InstallIn(SingletonComponent::class)
    object HiltModule {
        @Provides
        @Singleton
        fun provideRepositoryComponent(
            preferenceStorage: PreferenceStorage,
            localDataSource: LocalDataSource,
            tsuidezakeService: TsuidezakeService,
            authService: AuthService
        ): RepositoryComponent = DaggerRepositoryComponent.factory().create(
            preferenceStorage,
            localDataSource,
            tsuidezakeService,
            authService
        )

        @Provides
        fun provideTsuidezakeService(component: RepositoryComponent): Repository =
            component.repository
    }
}
