package com.budgetquest.presentation.auth

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.providers.builtin.Phone
import io.github.jan.supabase.auth.OtpType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AuthState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
)

class AuthViewModel(
    private val supabaseClient: SupabaseClient
) {
    private val scope = CoroutineScope(Dispatchers.Main)

    private val _authState = MutableStateFlow(AuthState())
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    fun loginWithEmail(email: String, pass: String) {
        scope.launch {
            _authState.value = AuthState(isLoading = true)
            try {
                supabaseClient.auth.signInWith(Email) {
                    this.email = email
                    this.password = pass
                }
                _authState.value = AuthState(isSuccess = true)
            } catch (e: Exception) {
                _authState.value = AuthState(error = e.message ?: "Authentication failed")
            }
        }
    }

    fun requestMobileOtp(phone: String) {
        scope.launch {
            _authState.value = AuthState(isLoading = true)
            try {
                supabaseClient.auth.signInWith(Phone) {
                    this.phone = phone
                }
                _authState.value = AuthState(isSuccess = true)
            } catch (e: Exception) {
                _authState.value = AuthState(error = e.message ?: "OTP Request failed")
            }
        }
    }

    fun verifyMobileOtp(phone: String, token: String) {
        scope.launch {
            _authState.value = AuthState(isLoading = true)
            try {
                supabaseClient.auth.verifyPhoneOtp(
                    type = OtpType.Phone.SMS,
                    phone = phone,
                    token = token
                )
                _authState.value = AuthState(isSuccess = true)
            } catch (e: Exception) {
                _authState.value = AuthState(error = "Invalid OTP")
            }
        }
    }

    fun resetError() {
        _authState.value = _authState.value.copy(error = null)
    }
}
