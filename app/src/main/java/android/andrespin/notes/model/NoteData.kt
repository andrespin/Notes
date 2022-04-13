package android.andrespin.notes.model

import android.view.View

data class NoteData(
    val header: String,
    val body: String,
    val time: Time,
    val date: Date,
    val id: Int = 0,
    var isChecked: Boolean = false,
    var background: Int = 0,
    var checkedBackground: Int = 0,
    var checkedVisibility: Int = View.INVISIBLE
)