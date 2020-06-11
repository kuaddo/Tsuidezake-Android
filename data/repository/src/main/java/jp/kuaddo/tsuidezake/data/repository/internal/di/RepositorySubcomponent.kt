package jp.kuaddo.tsuidezake.data.repository.internal.di

import dagger.Subcomponent
import jp.kuaddo.tsuidezake.core.scope.RepositoryScope
import jp.kuaddo.tsuidezake.data.local.LocalDataModule
import jp.kuaddo.tsuidezake.data.remote.RemoteDataModule
import jp.kuaddo.tsuidezake.data.repository.internal.RepositoryImpl

@RepositoryScope
@Subcomponent(
    modules = [
        LocalDataModule::class,
        RemoteDataModule::class
    ]
)
internal interface RepositorySubcomponent {

    fun repositoryImpl(): RepositoryImpl

    @Subcomponent.Builder
    interface Builder {
        fun build(): RepositorySubcomponent
    }
}