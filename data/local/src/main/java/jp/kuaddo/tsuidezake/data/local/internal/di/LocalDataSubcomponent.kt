package jp.kuaddo.tsuidezake.data.local.internal.di

import dagger.Subcomponent
import jp.kuaddo.tsuidezake.data.local.internal.SharedPreferenceStorage

@LocalDataScope
@Subcomponent
internal interface LocalDataSubcomponent {

    fun sharedPreferenceStorage(): SharedPreferenceStorage

    @Subcomponent.Factory
    interface Factory {
        fun create(): LocalDataSubcomponent
    }
}
