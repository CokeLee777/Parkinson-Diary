package healthcare.severance.parkinson.service

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import healthcare.severance.parkinson.R

class SessionManager(context: Context) {

    private var prefs: SharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
    // 상수 선언: static
    companion object {
        const val ACCESS_TOKEN = "access_token"
        const val ALARM_IS_ACTIVE = "alarm_is_active"
    }

    fun getAccessToken(): String? {
        return prefs.getString(ACCESS_TOKEN, "NO_VALUE")
    }

    fun isAuthenticated(): Boolean {
        return prefs.contains(ACCESS_TOKEN)
    }

    fun saveAccessToken(accessToken: String) {
        val editor = prefs.edit()
        editor.putString(ACCESS_TOKEN, accessToken)
        editor.apply()
    }

    fun unAuthenticate() {
        val editor = prefs.edit()
        editor.clear()
        editor.apply()
    }

    fun isAlarmActive(): Boolean {
        return prefs.getBoolean(ALARM_IS_ACTIVE, true)
    }

    fun saveAlarmIsActive(){
        val editor = prefs.edit()
        editor.putBoolean(ALARM_IS_ACTIVE, true)
        editor.apply()
    }

    fun updateAlarmIsActive(isActive: Boolean){
        val editor = prefs.edit()
        editor.putBoolean(ALARM_IS_ACTIVE, isActive)
        editor.apply()
    }
}