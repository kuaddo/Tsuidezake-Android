package jp.kuaddo.tsuidezake.data.local

import androidx.lifecycle.LiveData

interface PreferenceStorage {
    // TODO: 使うようになったタイミングでそれぞれの値を提供するLiveDataに変更する
    val preferenceChangedEvent: LiveData<Unit>
}
