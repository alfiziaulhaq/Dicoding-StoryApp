package com.dicoding.alfistoryapp.view.posting

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.alfistoryapp.data.SessionRepository
import com.dicoding.alfistoryapp.data.response.PostStoryResponse
import com.dicoding.alfistoryapp.data.retrofit.ApiConfig
import com.google.gson.Gson
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class PostingViewModel(private val repository: SessionRepository) : ViewModel() {

    companion object {
        private const val TAG = "PostingViewModel"
    }

    private val _result = MutableLiveData<PostStoryResponse>()
    val call: LiveData<PostStoryResponse> = _result

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun postStory(image: MultipartBody.Part, description: RequestBody) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.postStory(image,description)
                Log.d(TAG, "onSuccess: ${response.message}")
                _result.postValue(response)
                _isLoading.value =false
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, PostStoryResponse::class.java)
                val errorMessage = errorBody.message
                _isLoading.postValue(false)
                _result.postValue(errorBody)
                Log.d(TAG, "onError: $errorMessage")
            }
        }
    }

    fun postStory2(token: String,image: MultipartBody.Part, description: RequestBody) {
        val client = ApiConfig.getApiToken(token).postStory2(image,description)
        client.enqueue(object : Callback<PostStoryResponse> {
            override fun onResponse(
                call: Call<PostStoryResponse>,
                response: Response<PostStoryResponse>
            ) {
                if (response.isSuccessful) {
                    _result.value = response.body()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<PostStoryResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

}