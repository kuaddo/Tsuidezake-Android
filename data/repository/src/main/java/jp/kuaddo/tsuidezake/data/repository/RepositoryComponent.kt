package jp.kuaddo.tsuidezake.data.repository

import dagger.Component
import jp.kuaddo.tsuidezake.data.local.LocalDataComponent
import jp.kuaddo.tsuidezake.data.remote.RemoteDataComponent
import jp.kuaddo.tsuidezake.data.repository.internal.di.RepositoryModule
import jp.kuaddo.tsuidezake.data.repository.internal.di.RepositoryScope

@RepositoryScope
@Component(
    modules = [
        RepositoryModule::class
    ],
    dependencies = [
        LocalDataComponent::class,
        RemoteDataComponent::class
    ]
)
interface RepositoryComponent {
    val repository: Repository

    @Component.Factory
    interface Factory {
        fun create(
            localDataComponent: LocalDataComponent,
            remoteDataComponent: RemoteDataComponent
        ): RepositoryComponent
    }
}