package jp.kuaddo.tsuidezake.data.local

import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.WorkerThread
import androidx.core.content.edit
import jp.kuaddo.tsuidezake.core.live.UnitLiveEvent
import javax.inject.Inject
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

interface PreferenceStorage {
    val preferenceChangedEvent: UnitLiveEvent
}

class SharedPreferenceStorage @Inject constructor(context: Context) : PreferenceStorage {
    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    override val preferenceChangedEvent = UnitLiveEvent(generateUUIDTag = true)

    // リスナーをフィールドとして保持しておかないと、初回起動時のコールバックが呼ばれない
    private val changeListener = SharedPreferences.OnSharedPreferenceChangeListener { _, _ ->
        preferenceChangedEvent.callFromWorkerThread()
    }

    init {
        prefs.registerOnSharedPreferenceChangeListener(changeListener)
    }

    companion object {
        private const val PREFS_NAME = "tsuidezake"
    }
}

class IntPreference(
    private val prefs: SharedPreferences,
    private val name: String,
    private val defaultValue: Int
) : ReadWriteProperty<Any, Int> {

    @WorkerThread
    override fun getValue(thisRef: Any, property: KProperty<*>): Int {
        return prefs.getInt(name, defaultValue)
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Int) {
        prefs.edit { putInt(name, value) }
    }
}

class BooleanPreference(
    private val prefs: SharedPreferences,
    private val name: String,
    private val defaultValue: Boolean
) : ReadWriteProperty<Any, Boolean> {

    @WorkerThread
    override fun getValue(thisRef: Any, property: KProperty<*>): Boolean {
        return prefs.getBoolean(name, defaultValue)
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Boolean) {
        prefs.edit { putBoolean(name, value) }
    }
}

class StringPreference(
    private val prefs: SharedPreferences,
    private val name: String,
    private val defaultValue: String?
) : ReadWriteProperty<Any, String?> {

    @WorkerThread
    override fun getValue(thisRef: Any, property: KProperty<*>): String? {
        return prefs.getString(name, defaultValue)
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: String?) {
        prefs.edit { putString(name, value) }
    }
}
