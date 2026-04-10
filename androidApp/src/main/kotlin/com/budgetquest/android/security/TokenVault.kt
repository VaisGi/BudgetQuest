package com.budgetquest.android.security

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

/**
 * Secures high-value cryptographic strings (like Supabase GoTrue Auth JWTs) 
 * inside the hardware-backed Android Keystore.
 */
class TokenVault(context: Context) {

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        "budgetquest_secure_vault",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveToken(key: String, token: String) {
        sharedPreferences.edit().putString(key, token).apply()
    }

    fun getToken(key: String): String? {
        return sharedPreferences.getString(key, null)
    }

    fun clearToken(key: String) {
        sharedPreferences.edit().remove(key).apply()
    }

    companion object {
        const val KEY_ACCESS_TOKEN = "access_token"
        const val KEY_REFRESH_TOKEN = "refresh_token"
    }
}
