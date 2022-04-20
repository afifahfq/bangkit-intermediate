package com.example.storyapp.Preferences

import android.content.Context
import com.example.storyapp.Models.User

internal class UserPreference(context: Context) {
    companion object {
        private const val PREFS_NAME = "user_pref"
        private const val USERID = "userId"
        private const val NAME = "name"
        private const val TOKEN = "token"
    }
    private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    fun setUser(value: User) {
        val editor = preferences.edit()
        editor.putString(USERID, value.userId)
        editor.putString(NAME, value.name)
        editor.putString(TOKEN, value.token)
        editor.apply()
    }
    fun getUser(): User {
        val model = User()
        model.userId = preferences.getString(USERID, "")
        model.name = preferences.getString(NAME, "")
        model.token = preferences.getString(TOKEN, "")
        return model
    }
}