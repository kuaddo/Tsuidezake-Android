package jp.kuaddo.tsuidezake.data.repository

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import jp.kuaddo.tsuidezake.data.local.LocalDataModule
import jp.kuaddo.tsuidezake.data.remote.RemoteDataModule
import jp.kuaddo.tsuidezake.data.repository.internal.di.RepositoryModule

@Component(
    modules = [
        LocalDataModule::class,
        RemoteDataModule::class,
        RepositoryModule::class
    ]
)
interface RepositoryComponent {
    val repository: Repository

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance applicationContext: Context
        ): RepositoryComponent
    }
}