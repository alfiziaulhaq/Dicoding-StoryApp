package com.dicoding.alfistoryapp.view.maps

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.alfistoryapp.data.SessionRepository
import com.dicoding.alfistoryapp.data.response.GetStoryResponse
import com.dicoding.alfistoryapp.data.response.ListStoryItem
import com.dicoding.alfistoryapp.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapsViewModel(private val repository: SessionRepository) : ViewModel() {

    companion object {
        private const val TAG = "MapsViewModel"
    }

    private val _result = MutableLiveData<List<ListStoryItem>>()
    val result: LiveData<List<ListStoryItem>> = _result

    fun getStoriesLocation(token: String) {
        val client = ApiConfig.getApiToken(token).getStoriesLocation2()
        client.enqueue(object : Callback<GetStoryResponse> {
            override fun onResponse(
                call: Call<GetStoryResponse>,
                response: Response<GetStoryResponse>
            ) {
                if (response.isSuccessful) {
                    _result.value = response.body()?.listStory
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<GetStoryResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

}