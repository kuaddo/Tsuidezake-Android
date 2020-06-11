package jp.kuaddo.tsuidezake.data.local

import jp.kuaddo.tsuidezake.core.live.UnitLiveEvent

interface PreferenceStorage {
    // TODO: 使うようになったタイミングでLiveDataに変更する
    val preferenceChangedEvent: UnitLiveEvent
}
