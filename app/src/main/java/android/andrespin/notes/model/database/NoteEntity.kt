package android.andrespin.notes.model.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note_table")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val header: String,
    val body: String,
    val timeHours: String,
    val timeMinutes: String,
    val timeSeconds: String,
    val dateDay: String,
    val dateMonth: String,
    val dateYear: String
)







