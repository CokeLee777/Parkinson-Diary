package healthcare.severance.parkinson.service

import android.content.Context
import android.content.SharedPreferences
import healthcare.severance.parkinson.R

class SessionManager(context: Context) {

    private var prefs: SharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
    // 상수 선언: static
    companion object {
        const val ACCESS_TOKEN = "access_token"
    }

    fun isAuthenticated(): Boolean {
        return prefs.contains(ACCESS_TOKEN)
    }

    fun saveAccessToken(accessToken: String) {
        val editor = prefs.edit()
        editor.putString(ACCESS_TOKEN, accessToken)
        editor.apply()
    }
}