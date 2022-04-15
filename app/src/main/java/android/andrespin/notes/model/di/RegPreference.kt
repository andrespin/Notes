package android.andrespin.notes.model.di

import android.andrespin.notes.model.RegData
import android.andrespin.notes.model.login_key
import android.andrespin.notes.model.password_key
import android.andrespin.notes.model.reg_preferences
import android.content.Context
import android.preference.PreferenceManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RegPreference @Inject constructor(@ApplicationContext context: Context) {

    val prefs = context.getSharedPreferences(reg_preferences, Context.MODE_PRIVATE)

    fun setLogin(query: String) {
        prefs.edit().putString(login_key, query).apply()
    }

    fun getLogin() = prefs.getString(login_key, "")

    fun getRegData() : RegData =
        RegData(
            getLogin(),
            getPassword()
        )

    fun setPassword(query: String) {
        prefs.edit().putString(password_key, query).apply()
    }

    fun getPassword() = prefs.getString(password_key, "")

    fun removeAllRegData() {
        prefs.edit().remove(password_key).apply()
        prefs.edit().remove(login_key).apply()
    }

}
