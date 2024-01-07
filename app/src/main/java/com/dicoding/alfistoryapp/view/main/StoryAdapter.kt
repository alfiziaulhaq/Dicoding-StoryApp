package com.dicoding.alfistoryapp.view.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.alfistoryapp.data.response.ListStoryItem
import com.dicoding.alfistoryapp.databinding.StoryItemBinding

class StoryAdapter : PagingDataAdapter<ListStoryItem, StoryAdapter.MyViewHolder>(DIFF_CALLBACK) {

    //open function for searchview event listener
    private lateinit var onItemClickCallback: OnItemClickCallback
    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = StoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = getItem(position)
        if(user!=null){
            holder.bind(user)
        }
    }

    inner class MyViewHolder(val binding: StoryItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(users: ListStoryItem) {
            //searchview listener
            binding.root.setOnClickListener { onItemClickCallback.onItemClicked(users) }

            //show to recyclerview
            binding.tvName.text="${users.name}"
            binding.tvDescription.text = "${users.description}"
            Glide.with(binding.root)
                .load(users.photoUrl) // URL Gambar
                .into(binding.ivPhoto)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem) : Boolean {
                return oldItem == newItem
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: ListStoryItem)
    }

}