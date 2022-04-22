package android.andrespin.notes.utils.sorter

import android.andrespin.notes.model.database.NoteEntity

interface Filter {


    /*
    С сервера не всегда приходят заметки, принадлежащие
     */

    suspend fun filterNotes(list: List<NoteEntity>, login: String): List<NoteEntity>


}