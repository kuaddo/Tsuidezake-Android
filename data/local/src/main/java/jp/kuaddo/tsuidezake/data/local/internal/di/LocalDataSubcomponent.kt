package jp.kuaddo.tsuidezake.data.local.internal.di

import dagger.Subcomponent
import jp.kuaddo.tsuidezake.data.local.internal.SharedPreferenceStorage

@LocalDataScope
@Subcomponent
internal interface LocalDataSubcomponent {

    fun sharedPreferenceStorage(): SharedPreferenceStorage

    @Subcomponent.Builder
    interface Builder {
        fun build(): LocalDataSubcomponent
    }
}