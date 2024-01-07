package com.dicoding.alfistoryapp

import com.dicoding.alfistoryapp.data.response.ListStoryItem

object DataDummy {

    fun generateDummyStoryResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val story = ListStoryItem(
                i.toString(),
                "created + $i",
                "name $i",
                "desc $i",
                0.23,
                "id $i",
                0.56,
            )
            items.add(story)
        }
        return items
    }
}