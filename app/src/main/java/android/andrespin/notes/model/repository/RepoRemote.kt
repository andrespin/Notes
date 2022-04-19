package android.andrespin.notes.model.repository

import android.andrespin.notes.model.RegData
import android.andrespin.notes.model.login_server
import android.andrespin.notes.model.pass_server
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

    override fun getDataByLogin(
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

    private fun handleData(objects: List<ParseObject>) {

        for (i in 0 until objects.size) {
            println(
                "${objects[i].getString(login_server)} , " +
                        " ${objects[i].getString(pass_server)}"
            )

        }
    }


}