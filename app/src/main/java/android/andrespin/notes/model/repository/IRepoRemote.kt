package android.andrespin.notes.model.repository

import android.andrespin.notes.model.RegData
import com.parse.ParseObject
import com.parse.ParseQuery
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow

interface IRepoRemote {

    fun setRegData(reg: RegData)

    fun getDataByLogin(login: String): ParseQuery<ParseObject>?

}