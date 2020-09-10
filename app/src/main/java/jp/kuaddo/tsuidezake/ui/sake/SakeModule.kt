package jp.kuaddo.tsuidezake.ui.sake

import com.squareup.inject.assisted.dagger2.AssistedModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@AssistedModule
@Module(includes = [AssistedInject_SakeModule::class])
abstract class SakeModule {

    @ContributesAndroidInjector
    abstract fun contributeSakeDetailFragment(): SakeDetailFragment
}
