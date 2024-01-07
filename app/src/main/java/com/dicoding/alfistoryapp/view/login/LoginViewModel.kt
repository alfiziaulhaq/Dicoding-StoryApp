package com.dicoding.alfistoryapp.view.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.alfistoryapp.data.SessionRepository
import com.dicoding.alfistoryapp.data.preferences.SessionModel
import com.dicoding.alfistoryapp.data.response.LoginResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginViewModel(private val repository: SessionRepository) : ViewModel() {

    companion object {
        private const val TAG = "LoginViewModel"
    }

    private val _loginResponse = MutableLiveData<LoginResponse>()
    val loginResponse: LiveData<LoginResponse> = _loginResponse

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                //get success message
                val response = repository.login(email, password)
                saveSession(
                    SessionModel(
                        response.loginResult.userId,
                        response.loginResult.name,
                        response.loginResult.token,
                        email,
                        true
                    )
                )
                _loginResponse.postValue(response)
                Log.d(TAG, "onSuccess: ${response.message}")
            } catch (e: HttpException) {
                //get error message
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, LoginResponse::class.java)
                val errorMessage = errorBody.message
                _loginResponse.postValue(errorBody)
                Log.d(TAG, "onError: $errorMessage")
            }
        }
    }

    private fun saveSession(user: SessionModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }
}