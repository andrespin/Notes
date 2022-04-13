package android.andrespin.notes.model.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [NoteEntity::class], version = 2)
abstract class Database : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}