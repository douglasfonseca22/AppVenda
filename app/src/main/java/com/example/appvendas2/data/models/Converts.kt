package com.example.appvendas2.data.models

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun fromItemList(itemList: List<Item>): String {
        val gson = Gson()
        return gson.toJson(itemList)
    }

    @TypeConverter
    fun toItemList(itemString: String): List<Item> {
        val gson = Gson()
        return gson.fromJson(itemString, object : TypeToken<List<Item>>() {}.type)
    }
}
