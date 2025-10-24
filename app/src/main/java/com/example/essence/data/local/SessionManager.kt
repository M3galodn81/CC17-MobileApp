package com.example.essence.data.local

import android.content.Context
import android.content.SharedPreferences
import com.example.essence.data.model.User
import com.example.essence.data.model.UserRole

/**
 * A singleton object to manage the user's session data in SharedPreferences.
 */
object SessionManager {

    private const val PREFS_NAME = "essence_session_prefs"
    private const val KEY_USER_ID = "user_id"
    private const val KEY_USER_EMAIL = "user_email"
    private const val KEY_USER_ROLE = "user_role"
    private const val KEY_USER_FULL_NAME = "user_full_name"

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    /**
     * Call this function when the user successfully logs in.
     * It saves the essential user details to SharedPreferences.
     */
    fun saveSession(context: Context, user: User) {
        val editor = getPreferences(context).edit()
        editor.putString(KEY_USER_ID, user.id)
        editor.putString(KEY_USER_EMAIL, user.email)
        editor.putString(KEY_USER_ROLE, user.role.name) // Save enum as string
        editor.putString(KEY_USER_FULL_NAME, user.fullName)
        editor.apply()
    }

    /**
     * Call this function when the user logs out.
     * It clears all session data.
     */
    fun clearSession(context: Context) {
        val editor = getPreferences(context).edit()
        editor.clear()
        editor.apply()
    }

    /**
     * Checks if a user is currently logged in (i.e., if a user ID is saved).
     */
    fun isLoggedIn(context: Context): Boolean {
        return getPreferences(context).getString(KEY_USER_ID, null) != null
    }

    /**
     * Retrieves the saved user ID.
     * Returns null if no user is logged in.
     */
    fun getUserId(context: Context): String? {
        return getPreferences(context).getString(KEY_USER_ID, null)
    }

    /**
     * Retrieves the saved user's full name.
     * Returns null if no user is logged in.
     */
    fun getFullName(context: Context): String? {
        return getPreferences(context).getString(KEY_USER_FULL_NAME, null)
    }

    /**
     * Retrieves the saved user's role.
     * Defaults to EMPLOYEE if no role is found (for security).
     */
    fun getUserRole(context: Context): UserRole {
        val roleName = getPreferences(context).getString(KEY_USER_ROLE, UserRole.EMPLOYEE.name)
        return try {
            UserRole.valueOf(roleName ?: UserRole.EMPLOYEE.name)
        } catch (e: IllegalArgumentException) {
            UserRole.EMPLOYEE // Default to least privileged
        }
    }
}
