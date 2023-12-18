package com.tzh.datastore_module

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class SharePreferenceHelper(context: Context) {
    val appContxt = context.applicationContext

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "OIS_DataStore")

        const val LOCAL_KEY = "SHAREPREFERENCE_KEY"
        const val API_URL_KEY = "ApiUrl_Key"
        const val System_Doc_KEY = "SystemDoc"
        const val Last_DataSync_Date_KEY = "Last_DataSync_Date_KEY"
        const val USER_ID_KEY = "UserIdKey"
        const val USER_NAME_KEY = "UserNameKey"
        const val READER_POWER_KEY = "Reader_Power_Key"
        private val USER = stringPreferencesKey("USER")
    }

    suspend fun saveUser(user: User) {
        appContxt.dataStore.edit {
            it[USER] = Gson().toJson(user)
        }
    }

    suspend fun clearUser() {
        appContxt.dataStore.edit {
            it[USER] = ""
        }
    }

    fun getUser(): Flow<User>? {
        return try {
            appContxt.dataStore.data.map {
                Gson().fromJson(it[USER], User::class.java)
            }
        } catch (e: Exception) {
            null
        }
    }

    var userName by context.sharePreference<String?>(USER_NAME_KEY, "", "")

    var userId by context.sharePreference<Int?>(USER_ID_KEY, 0, null)

    var apiUrl by context.sharePreference<String?>(API_URL_KEY, "", null)

    var systemDoc by context.sharePreference(System_Doc_KEY, 0, 0)

    var readerPower by context.sharePreference(READER_POWER_KEY, 50, 50)

    private var _lastDataSyncDate by context.sharePreference(Last_DataSync_Date_KEY, "")


}

inline fun <reified T> Context.sharePreference(key: String, defaultType: T, value: Any? = null) =
    SharePreferenceDelegate(this, key, defaultType, value = value)

class SharePreferenceDelegate<T>(
    private val context: Context, private val key: String, private val defaultType: T, private val value: Any? = null
) : ReadWriteProperty<Any?, T?> {

    private val sharePreferenceHelper by lazy {
        context.getSharedPreferences(context.packageName + SharePreferenceHelper.LOCAL_KEY, Context.MODE_PRIVATE)
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): T? {
        return when (defaultType) {
            is Int? -> {
                sharePreferenceHelper.getInt(key, (value as Int?) ?: 0) as T?
            }

            Int -> {
                sharePreferenceHelper.getInt(key, (value as Int)) as T
            }

            is Long? -> {
                sharePreferenceHelper.getLong(key, (value as Long?) ?: 0) as T?
            }

            Long -> {
                sharePreferenceHelper.getLong(key, (value as Long)) as T
            }

            is String? -> {
                sharePreferenceHelper.getString(key, (value as String?)) as T?
            }

            String -> {
                sharePreferenceHelper.getString(key, (value as String)) as T
            }

            is Boolean? -> {
                sharePreferenceHelper.getBoolean(key, (value as Boolean?) ?: false) as T?
            }

            Boolean -> {
                sharePreferenceHelper.getBoolean(key, (value as Boolean)) as T
            }

            else -> {
                throw IllegalArgumentException("Unsupported type")
            }
        }
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
        with(sharePreferenceHelper.edit()) {
            when (value) {
                is Int -> putInt(key, value)
                is Long -> putLong(key, value)
                is String -> putString(key, value)
                is Boolean -> putBoolean(key, value)
                // Add more types as needed (Boolean, Float, etc.)
                else -> throw IllegalArgumentException("Unsupported type")
            }
            apply()
        }
    }

}
