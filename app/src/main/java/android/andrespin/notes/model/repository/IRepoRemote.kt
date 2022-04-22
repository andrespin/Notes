package android.andrespin.notes.model.repository

import android.andrespin.notes.model.RegData
import android.andrespin.notes.model.database.NoteEntity
import com.parse.ParseObject
import com.parse.ParseQuery
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow

interface IRepoRemote {

    fun setRegData(reg: RegData)

    fun deleteRegData(reg: RegData)

    fun getRegDataByLogin(login: String): ParseQuery<ParseObject>?

    fun getAllNotes(login: String): ParseQuery<ParseObject>?

    fun setNote(noteEntity: NoteEntity, login: String)

    fun setNotes(notesEntity: List<NoteEntity>, login: String)

    fun deleteNote(noteEntity: NoteEntity, login: String)

}