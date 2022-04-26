package android.andrespin.notes.model.interactor

import android.andrespin.notes.model.RegData
import android.andrespin.notes.model.database.NoteEntity
import com.parse.ParseObject
import com.parse.ParseQuery
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface Interactor {

    fun setRegData(reg: RegData)

    /**
     * При вызове данного метода происходит попытка получить
     * строку из базы данных с таким методом, если строка получена, то
     * значит пользователь с таким логином уже существует.
     */

    fun getCurrentLogin(login: String): ParseQuery<ParseObject>?

    suspend fun synchronizeNotes(
        isSyncingOn: Boolean,
        login: String,
        scope: CoroutineScope?
    ): Flow<SyncState>


    suspend fun getAllNotes(

    ): List<NoteEntity>

    suspend fun getAllNotes(
        login: String
    ): ParseQuery<ParseObject>?

    suspend fun saveNotes(
        notes: List<NoteEntity>
    )

    suspend fun saveNotes(
        notes: List<NoteEntity>,
        login: String
    )

    /**
     * Сохраняет записи пользователя, которые есть на сервере,
     * но которых нет в базе данных и наоборот
     */

    suspend fun saveMissingNotes(
        missingNotesDb: List<NoteEntity>,
        missingNotesServer: List<NoteEntity>,
        login: String
    )

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