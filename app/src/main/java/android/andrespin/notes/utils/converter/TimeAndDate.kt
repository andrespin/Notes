package android.andrespin.notes.utils.converter

import android.andrespin.notes.model.Date
import android.andrespin.notes.model.Time

interface TimeAndDate {

    fun convertToTime(timeAndDate: String) : Time

    fun convertToDate(timeAndDate: String) : Date

    fun addNameOfMonth(date: Date): Date

}