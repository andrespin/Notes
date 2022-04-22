package android.andrespin.notes.profile.entrance

import android.andrespin.notes.BaseViewModel
import android.andrespin.notes.R
import android.andrespin.notes.model.RegData
import android.andrespin.notes.model.di.RegPreference
import android.andrespin.notes.model.interactor.Interactor
import android.andrespin.notes.model.login_server
import android.andrespin.notes.model.pass_server
import android.andrespin.notes.notes.intent.NotesEvent
import android.andrespin.notes.profile.entrance.intent.EntranceEvent
import android.andrespin.notes.profile.entrance.intent.EntranceIntent
import android.andrespin.notes.profile.entrance.intent.EntranceState
import android.andrespin.notes.profile.my_profile.intent.ProfileIntent
import android.andrespin.notes.profile.my_profile.intent.ProfileState
import androidx.lifecycle.viewModelScope
import com.parse.ParseObject
import com.parse.livequery.ParseLiveQueryClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.flow.SharedFlow

@ObsoleteCoroutinesApi
@HiltViewModel
class EntranceViewModel
@Inject constructor(
    private val regPreference: RegPreference,
    private val interactor: Interactor
) : BaseViewModel() {

    val intent = Channel<EntranceIntent>(Channel.UNLIMITED)

    private val _state = MutableStateFlow<EntranceState>(EntranceState.Idle)
    val state: StateFlow<EntranceState> get() = _state

    private val _event = MutableSharedFlow<EntranceEvent>()
    val event: SharedFlow<EntranceEvent> = _event


    private fun setStateValue(value: EntranceState) {
        _state.value = value
    }

    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            intent.consumeAsFlow().collect {
                when (it) {
                    is EntranceIntent.LogIn -> logIn(it.reg)
                }
            }
        }
    }

    private suspend fun logIn(reg: RegData) {

        println("logIn $reg")

        setStateValue(EntranceState.Idle)
        delay(1)

        if (reg.login.isNullOrEmpty() || reg.password.isNullOrEmpty()) {
            setStateValue(EntranceState.FieldsAreNotFilled)

            _event.emit(EntranceEvent.FieldsAreNotFilled)

        } else {
            interactor.getCurrentLogin(reg.login)?.findInBackground { objects, e ->
                if (e == null) {
                    if (objects.isEmpty()) {
                        viewModelScope.launch {
                            _event.emit(EntranceEvent.LoginIsNotFound)
                        }
                    } else {

                        val data = getFilteredRegData(objects, reg.login)

                        if (data?.password == reg.password) {
                            regPreference.setPassword(reg.password)
                            regPreference.setLogin(reg.login)
                            setStateValue(EntranceState.LogIn)
                        } else {
                            viewModelScope.launch {
                                _event.emit(EntranceEvent.PassIsNotCorrect)
                            }
                        }
                    }
                } else {
                    e.printStackTrace()
                    setStateValue(EntranceState.Error(e.message!!))
                }
            }
        }

    }

    private fun getFilteredRegData(objects: List<ParseObject>, login: String): RegData? {
        for (i in objects.indices) {
            val log = objects[i].getString(login_server)
            val pass = objects[i].getString(pass_server)
            if (log == login) {
                return RegData(log, pass)
            }
        }
        return null
    }


}