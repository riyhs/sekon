package com.sekon.app.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences

object Preference {
    @SuppressLint("CommitPrefEdits")

    fun initPref(context: Context, name: String): SharedPreferences {
        return context.getSharedPreferences(name, Context.MODE_PRIVATE)
    }

    private fun editorPreference(context: Context, name: String): SharedPreferences.Editor {
        val sharedPref = context.getSharedPreferences(name, Context.MODE_PRIVATE)
        return sharedPref.edit()
    }

    fun saveToken(token: String, context: Context) {
        val editor = editorPreference(context, "onSignIn")
        editor.putString("token", token)
        editor.apply()
    }

    fun saveSiswaName(name: String, context: Context) {
        val editor = editorPreference(context, "onSignIn")
        editor.putString("name", name)
        editor.apply()
    }

    fun saveSiswaId(id: String, context: Context) {
        val editor = editorPreference(context, "onSignIn")
        editor.putString("id", id)
        editor.apply()
    }

    fun onBoardingFinished(context: Context, state: Boolean) {
        val editor = editorPreference(context, "onBoarding")
        editor.putBoolean("Finished", state)
        editor.apply()
    }

}