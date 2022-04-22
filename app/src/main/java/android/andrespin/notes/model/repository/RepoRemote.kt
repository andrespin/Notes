package android.andrespin.notes.model.repository

import android.andrespin.notes.model.*
import android.andrespin.notes.model.database.NoteEntity
import android.util.Log
import com.parse.ParseObject
import com.parse.ParseQuery

class RepoRemote : IRepoRemote {

    override fun setRegData(reg: RegData) {
        val regData = ParseObject("RegData")
        regData.put(login_server, reg.login!!)
        regData.put(pass_server, reg.password!!)
        regData.saveInBackground {
            if (it != null) {
                it.localizedMessage?.let { message -> Log.e("Server error", message) }
            } else {
                Log.d("Server", "Object saved.")
            }
        }
    }

    override fun deleteRegData(reg: RegData) {
        val regData = ParseObject("RegData")
        regData.put(login_server, reg.login!!)
        regData.put(pass_server, reg.password!!)
        regData.deleteInBackground {
            if (it != null) {
                it.localizedMessage?.let { message -> Log.e("Server error", message) }
            } else {
                Log.d("Server", "Object deleted.")
            }
        }
    }

    override fun getRegDataByLogin(
        login: String
    ): ParseQuery<ParseObject>? {
        val query = ParseQuery.getQuery<ParseObject>("RegData")
        query.whereContains(login_server, login)
        query.orderByDescending("createdAt")
//        query.findInBackground { objects, e ->
//
//            if (e == null) {
//
//            } else {
//                e.printStackTrace()
//            }
//
//        }

        return query
    }


    override fun getAllNotes(login: String): ParseQuery<ParseObject>? {
        val query = ParseQuery.getQuery<ParseObject>("Note")
        query.whereContains(server_note_login, login)
        query.orderByDescending("createdAt")
        return query
    }


    override fun setNote(noteEntity: NoteEntity, login: String) {

        val note = ParseObject("Note")

        note.put(server_note_login, login)

        note.put(server_note_id, noteEntity.id.toString())

        note.put(server_note_header, noteEntity.header)

        note.put(server_note_body, noteEntity.body)

        note.put(server_note_time_hours, noteEntity.timeHours)

        note.put(server_note_time_minutes, noteEntity.timeMinutes)

        note.put(server_note_time_seconds, noteEntity.timeSeconds)

        note.put(server_note_date_day, noteEntity.dateDay)

        note.put(server_note_date_month, noteEntity.dateMonth)

        note.put(server_note_date_year, noteEntity.dateYear)

        note.saveInBackground {
            if (it != null) {
                it.localizedMessage?.let { message -> Log.e("Server error", message) }
            } else {
                Log.d("Server", "Note saved.")
            }
        }

    }

    override fun setNotes(notesEntity: List<NoteEntity>, login: String) {
        for (i in 0 until notesEntity.size) {
            setNote(notesEntity[i], login)
        }
    }

    override fun deleteNote(noteEntity: NoteEntity, login: String) {

        val note = ParseObject("Note")

        note.put(server_note_login, login)

        note.put(server_note_id, noteEntity.id)

        note.put(server_note_header, noteEntity.header)

        note.put(server_note_body, noteEntity.body)

        note.put(server_note_time_hours, noteEntity.timeHours)

        note.put(server_note_time_minutes, noteEntity.timeMinutes)

        note.put(server_note_time_seconds, noteEntity.timeSeconds)

        note.put(server_note_date_day, noteEntity.dateDay)

        note.put(server_note_date_month, noteEntity.dateMonth)

        note.put(server_note_date_year, noteEntity.dateYear)

        note.deleteInBackground {
            if (it == null) {
                Log.d("Server", "Note deleted.")
            } else {
                it.localizedMessage?.let { message -> Log.e("Server error", message) }
            }

        }

    }


    /*
   adapter.onDeleteListener.observe(this@MainActivity, { parseObject ->
            progressDialog?.show()
            parseObject.deleteInBackground { e ->
                progressDialog?.dismiss()
                if (e == null) {
                    //We deleted the object and fetching data again.
                    getTodoList()
                } else {
                    showAlert("Error", e.message!!)
                }
            }
        })
     */


    private fun handleData(objects: List<ParseObject>) {

        for (i in 0 until objects.size) {
            println(
                "${objects[i].getString(login_server)} , " +
                        " ${objects[i].getString(pass_server)}"
            )

        }
    }


}