package jp.kuaddo.tsuidezake.di.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import jp.kuaddo.tsuidezake.MainActivity
import jp.kuaddo.tsuidezake.di.ActivityScoped

@Suppress("unused")
@Module
abstract class ActivityModule {

    @ActivityScoped
    @ContributesAndroidInjector
    abstract fun contributeMainActivity(): MainActivity
}