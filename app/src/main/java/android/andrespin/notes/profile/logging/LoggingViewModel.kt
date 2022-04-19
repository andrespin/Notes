package android.andrespin.notes.profile.logging

import android.andrespin.notes.BaseViewModel
import android.andrespin.notes.model.RegData
import android.andrespin.notes.model.di.RegPreference
import android.andrespin.notes.model.interactor.Interactor
import android.andrespin.notes.model.login_server
import android.andrespin.notes.profile.logging.intent.LoggingIntent
import android.andrespin.notes.profile.logging.intent.LoggingState
import androidx.lifecycle.viewModelScope
import com.parse.ParseObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoggingViewModel
@Inject constructor(
    private val regPreference: RegPreference,
    private val interactor: Interactor
) : BaseViewModel() {

    val intent = Channel<LoggingIntent>(Channel.UNLIMITED)

    private val _state = MutableStateFlow<LoggingState>(LoggingState.Idle)
    val state: StateFlow<LoggingState> get() = _state

    private var isPassOk = false
    private var isLoginOk = false

    private lateinit var correctLogin: String

    private lateinit var correctPassword: String

    private fun setStateValue(value: LoggingState) {
        _state.value = value
    }

    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            intent.consumeAsFlow().collect {
                when (it) {
                    is LoggingIntent.Register -> tryToRegister(it)
                    is LoggingIntent.SaveRegData -> saveRegData()
                }
            }
        }
    }

    private fun saveRegData() {
        println("saveRegData()")
        regPreference.setLogin(correctLogin)
        regPreference.setPassword(correctPassword)
        interactor.setRegData(
            RegData(
                correctLogin,
                correctPassword
            )
        )
    }

    private suspend fun tryToRegister(it: LoggingIntent.Register) {
        if (it.login.isEmpty() || it.pass1.isEmpty() || it.pass2.isEmpty()) {
            setStateValue(LoggingState.Idle)
            delay(1)
            setStateValue(LoggingState.FieldsAreNotFilled)
        } else {
            interactor.getCurrentLogin(
                it.login
            )?.findInBackground { objects, e ->
                if (e == null) {
                    viewModelScope.launch {
                        register(it, objects)
                    }
                } else {
                    e.printStackTrace()
                    // TODO
                }
            }
        }
    }

    private suspend fun register(it: LoggingIntent.Register, objects: MutableList<ParseObject>) {
        setStateValue(LoggingState.Idle)
        isPassOk = it.pass1 == it.pass2
        isLoginOk = isLoginRepeated(objects)
        delay(1)
        if (isLoginOk) {
            setStateValue(LoggingState.LoginIsOk)
        } else {
            setStateValue(LoggingState.LoginIsBusy)
        }
        setStateValue(LoggingState.Idle)
        delay(1)
        if (isPassOk) {
            setStateValue(LoggingState.PassIsOk)
        } else {
            setStateValue(LoggingState.PassIsNotRepeated)
        }
        setStateValue(LoggingState.Idle)
        delay(1)

        if (isPassOk && isLoginOk) {
            correctLogin = it.login
            correctPassword = it.pass1
            setStateValue(LoggingState.RegDataCorrect)
        }
    }

    private fun isLoginRepeated(objects: List<ParseObject>) = objects.isEmpty()

}