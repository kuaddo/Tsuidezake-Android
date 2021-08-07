package jp.kuaddo.tsuidezake.data.repository.internal.di

import dagger.Binds
import dagger.Module
import dagger.hilt.migration.DisableInstallInCheck
import jp.kuaddo.tsuidezake.data.repository.internal.RepositoryImpl
import jp.kuaddo.tsuidezake.domain.Repository

@Module
@DisableInstallInCheck
internal interface RepositoryModule {
    @Binds
    fun bindRepository(impl: RepositoryImpl): Repository
}
