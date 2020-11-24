package jp.kuaddo.tsuidezake.data.repository

import dagger.BindsInstance
import dagger.Component
import jp.kuaddo.tsuidezake.data.repository.internal.di.RepositoryModule
import jp.kuaddo.tsuidezake.data.repository.internal.di.RepositoryScope
import jp.kuaddo.tsuidezake.domain.Repository

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
}
