package com.budgetquest.android.security

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class TokenVaultTest {

    private lateinit var tokenVault: TokenVault

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        tokenVault = TokenVault(context)
    }

    @Test
    fun `saveToken writes access token and getToken retrieves it securely`() {
        val mockToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.dummy_token_data"
        tokenVault.saveToken(TokenVault.KEY_ACCESS_TOKEN, mockToken)
        
        val retrieved = tokenVault.getToken(TokenVault.KEY_ACCESS_TOKEN)
        assertEquals(mockToken, retrieved)
    }

    @Test
    fun `clearToken successfully removes token from encrypted preferences`() {
        val mockToken = "dummy_refresh_token"
        tokenVault.saveToken(TokenVault.KEY_REFRESH_TOKEN, mockToken)
        
        // Ensure it's there
        assertEquals(mockToken, tokenVault.getToken(TokenVault.KEY_REFRESH_TOKEN))
        
        // Clear it
        tokenVault.clearToken(TokenVault.KEY_REFRESH_TOKEN)
        
        // Assert it's gone
        assertNull(tokenVault.getToken(TokenVault.KEY_REFRESH_TOKEN))
    }

    @Test
    fun `getToken returns null when no token exists`() {
        assertNull(tokenVault.getToken("non_existent_key"))
    }
}
