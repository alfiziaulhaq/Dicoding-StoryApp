package com.dicoding.alfistoryapp.view.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dicoding.alfistoryapp.data.SessionRepository
import com.dicoding.alfistoryapp.data.preferences.SessionModel
import com.dicoding.alfistoryapp.data.response.GetStoryResponse
import com.dicoding.alfistoryapp.data.response.ListStoryItem
import com.dicoding.alfistoryapp.data.retrofit.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response


class MainViewModel(private val repository: SessionRepository) : ViewModel() {

    companion object {
        private const val TAG = "MainViewModel"
    }

    //story with pagingdata
    fun storyPagingData(token: String): LiveData<PagingData<ListStoryItem>> =
        repository.getStories2(token).cachedIn(viewModelScope)

    //story without paging data
    private val _result = MutableLiveData<List<ListStoryItem>>()
    val listStories: LiveData<List<ListStoryItem>> = _result

    fun getStories() {
        viewModelScope.launch {
            try {
                val response = repository.getStories()
                Log.d(TAG, "onSuccess: ${response.message}")
                _result.postValue(response.listStory)

            } catch (e: HttpException) {

                Log.d(TAG, "onError: ${e.message()}")
            }
        }
    }

    fun getStories2(token: String) {
        val client = ApiConfig.getApiToken(token).getStory()
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

    fun getSession(): LiveData<SessionModel> {
        return repository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}