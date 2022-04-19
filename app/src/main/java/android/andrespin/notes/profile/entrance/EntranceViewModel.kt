package android.andrespin.notes.profile.entrance

import android.andrespin.notes.BaseViewModel
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

    // Тут не рабоает
//    private val _event = Channel<EntranceEvent>(Channel.CONFLATED)
//    val event: Channel<EntranceEvent> = _event

    private val _event = MutableSharedFlow<EntranceEvent>()
    val event : SharedFlow<EntranceEvent> = _event


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
                    is EntranceIntent.Click -> click()
                }
            }
        }
    }

    private fun click() {
// Вот тут не работает
        println("click()")
        viewModelScope.launch(Dispatchers.Main) {
            _event.emit(EntranceEvent.FieldsAreNotFilled)

        }

    }

    private suspend fun logIn(reg: RegData) {

        /*
        _event.send(EntranceEvent.FieldsAreNotFilled)


        println("logIn $reg")

        setStateValue(EntranceState.Idle)
        delay(1)

        if (reg.login.isNullOrEmpty() || reg.password.isNullOrEmpty()) {
            setStateValue(EntranceState.FieldsAreNotFilled)

            _event.send(EntranceEvent.FieldsAreNotFilled)

//            viewModelScope.launch(Dispatchers.Main) {
//                // _event.send(EntranceEvent.FieldsAreNotFilled)
//
//                println("reg.login.isNullOrEmpty() || reg.password.isNullOrEmpty()")
//            }
        } else {
            interactor.getCurrentLogin(reg.login)?.findInBackground { objects, e ->
                if (e == null) {
                    if (objects.isEmpty()) {
                        viewModelScope.launch {
                            _event.send(EntranceEvent.LoginIsNotFound)
                        }
                    } else {
                        if (objects[0].getString(pass_server) == reg.password) {
                            regPreference.setPassword(reg.password)
                            regPreference.setLogin(reg.login)

                            viewModelScope.launch {
                                _event.send(EntranceEvent.LoginIsNotFound)
                            }
                            setStateValue(EntranceState.LogIn)
                        } else {
                            viewModelScope.launch {
                                _event.send(EntranceEvent.PassIsNotCorrect)
                            }
                        }
                    }
                } else {
                    e.printStackTrace()
                    setStateValue(EntranceState.Error(e.message!!))
                }
            }
        }

        */
    }


}