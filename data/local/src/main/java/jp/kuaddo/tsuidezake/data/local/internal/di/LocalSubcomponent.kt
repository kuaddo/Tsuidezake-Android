package jp.kuaddo.tsuidezake.data.local.internal.di

import dagger.Subcomponent
import jp.kuaddo.tsuidezake.data.local.internal.SharedPreferenceStorage

@LocalScope
@Subcomponent
internal interface LocalSubcomponent {

    fun sharedPreferenceStorage(): SharedPreferenceStorage

    @Subcomponent.Builder
    interface Builder {
        fun build(): LocalSubcomponent
    }
}
