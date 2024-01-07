package com.dicoding.alfistoryapp.view.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.alfistoryapp.data.SessionRepository
import com.dicoding.alfistoryapp.data.response.DetailStoryResponse
import com.dicoding.alfistoryapp.data.response.Story
import com.dicoding.alfistoryapp.data.retrofit.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response


class DetailViewModel(private val repository: SessionRepository) : ViewModel() {

    companion object {
        private const val TAG = "DetailViewModel"
    }

    private val _result = MutableLiveData<Story>()
    val result: LiveData<Story> = _result

    fun getDetailStory(id:String) {
        viewModelScope.launch {
            try {
                val response = repository.getDetailStory(id)
                Log.d(TAG, "onSuccess: ${response.message}")
                _result.postValue(response.story)

            } catch (e: HttpException) {

                Log.d(TAG, "onError: ${e.message()}")
            }
        }
    }
    fun getDetailStory2(token:String,id:String) {
        viewModelScope.launch {
            try {
                val response = repository.getDetailStory2(token,id)
                Log.d(TAG, "onSuccess: ${response.message}")
                _result.postValue(response.story)
            } catch (e: HttpException) {

                Log.d(TAG, "onError: ${e.message()}")
            }
        }
    }

    fun getDetailStory3(token: String,id: String) {
        val client = ApiConfig.getApiToken(token).getDetailStory3(id)
        client.enqueue(object : Callback<DetailStoryResponse> {
            override fun onResponse(
                call: Call<DetailStoryResponse>,
                response: Response<DetailStoryResponse>
            ) {
                if (response.isSuccessful) {
                    _result.value = response.body()?.story
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<DetailStoryResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }
}