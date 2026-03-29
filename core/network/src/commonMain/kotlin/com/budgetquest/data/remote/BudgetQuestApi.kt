package com.budgetquest.data.remote

/**
 * Placeholder interface for future backend API integration.
 * When backend is ready, implement this with Ktor HTTP client.
 * The repository layer will compose local + remote data sources.
 */
interface BudgetQuestApi {
    // Auth
    suspend fun login(email: String, password: String): AuthToken
    suspend fun register(email: String, password: String, name: String): AuthToken

    // Transactions (sync)
    suspend fun syncTransactions(localTransactions: List<Any>): List<Any>

    // Profile
    suspend fun getRemoteProfile(): Any
    suspend fun updateRemoteProfile(profile: Any)

    // Plaid (bank sync — premium only)
    suspend fun linkBankAccount(publicToken: String)
    suspend fun getLinkedAccounts(): List<Any>
    suspend fun fetchBankTransactions(): List<Any>
}

data class AuthToken(
    val accessToken: String,
    val refreshToken: String,
    val expiresAt: Long
)
