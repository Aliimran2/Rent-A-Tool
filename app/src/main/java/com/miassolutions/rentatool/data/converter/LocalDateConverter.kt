package com.miassolutions.rentatool.data.converter

import androidx.room.TypeConverter
import java.time.LocalDate

class LocalDateConverter {
    @TypeConverter
    fun fromLocalDate(date: LocalDate): Long = date.toEpochDay()

    @TypeConverter
    fun toLocalDate(dateLong: Long): LocalDate = LocalDate.ofEpochDay(dateLong)
}