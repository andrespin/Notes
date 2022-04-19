package android.andrespin.notes.model.interactor

import android.andrespin.notes.model.RegData
import android.andrespin.notes.model.database.NoteEntity
import com.parse.ParseObject
import com.parse.ParseQuery

interface Interactor {

    fun setRegData(reg: RegData)

    /**
     * При вызове данного метода происходит попытка получить
     * строку из базы данных с таким методом, если строка получена, то
     * значит пользователь с таким логином уже существует.
     */

    fun getCurrentLogin(login: String): ParseQuery<ParseObject>?

    suspend fun getAllNotes(
        isAuthorized: Boolean = false,
        isConnected: Boolean = false
    ): List<NoteEntity>

    suspend fun saveNote(
        noteEntity: NoteEntity,
        isAuthorized: Boolean = false,
        isConnected: Boolean = false
    )

    suspend fun updateNote(
        noteEntity: NoteEntity,
        isAuthorized: Boolean = false,
        isConnected: Boolean = false
    )

    suspend fun deleteNote(
        noteEntity: NoteEntity,
        isAuthorized: Boolean = false,
        isConnected: Boolean = false
    )

    suspend fun deleteNotes(
        noteEntity: MutableList<NoteEntity>,
        isAuthorized: Boolean = false,
        isConnected: Boolean = false
    )

    suspend fun saveNoteList(
        noteList: List<NoteEntity>,
        isAuthorized: Boolean = false,
        isConnected: Boolean = false
    )

    suspend fun getNoteById(
        id: Int,
        isAuthorized: Boolean = false,
        isConnected: Boolean = false
    ): NoteEntity

}