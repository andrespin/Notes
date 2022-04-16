package android.andrespin.notes.profile.logging

import android.andrespin.notes.BaseViewModel
import android.andrespin.notes.model.di.RegPreference
import android.andrespin.notes.profile.logging.intent.LoggingIntent
import android.andrespin.notes.profile.logging.intent.LoggingState
import androidx.lifecycle.viewModelScope
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
    private val regPreference: RegPreference
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
                    is LoggingIntent.Register -> register(it)
                    is LoggingIntent.SaveRegData -> saveRegData()
                }
            }
        }
    }

    private fun saveRegData() {
        regPreference.setLogin(correctLogin)
        regPreference.setPassword(correctPassword)
    }

    private suspend fun register(it: LoggingIntent.Register) {
        setStateValue(LoggingState.Idle)
        isPassOk = it.pass1 == it.pass2
        isLoginOk = !isLoginRepeated(it.login)
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

    private fun isLoginRepeated(login: String): Boolean {
        return false
    }


}