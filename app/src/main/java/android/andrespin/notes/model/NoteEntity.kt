package android.andrespin.notes.model

import androidx.room.Entity
import androidx.room.PrimaryKey





data class RegistrationData(
    val login: String,
    val password: String
)

data class Time(val h: String, val m: String, val s: String)

data class Date(val d: String, val m: String, val y: String, val nameOfMonth: String? = null)