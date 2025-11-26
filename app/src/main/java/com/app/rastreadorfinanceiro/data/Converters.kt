package com.app.rastreadorfinanceiro.data

import androidx.room.TypeConverter
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import java.time.LocalDateTime

class Converters {


    @TypeConverter
    fun fromTimestamp(value: String?): LocalDateTime? {
        return value?.let { LocalDateTime.parse(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: LocalDateTime?): String? {
        return date?.toString()
    }


    @TypeConverter
    fun fromColor(value: Int?): Color? {
        return value?.let { Color(it) }
    }

    @TypeConverter
    fun colorToInt(color: Color?): Int? {
        return color?.toArgb()
    }
}