package android.andrespin.notes.utils.converter

import android.andrespin.notes.model.NoteData
import android.andrespin.notes.model.database.NoteEntity
import com.parse.ParseObject

interface DataTypes {

    fun convertToNoteEntityList(list: List<NoteData>): List<NoteEntity>

    fun convertToNoteDataList(list: List<NoteEntity>): List<NoteData>

    fun convertToNoteData(noteEntity: NoteEntity): NoteData

    fun convertToNoteEntity(noteData: NoteData): NoteEntity

    fun convertParseObjectToNoteEntityList(objects: List<ParseObject>): List<NoteEntity>

    // objects: List<ParseObject>

}