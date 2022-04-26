package android.andrespin.notes.profile.my_profile

import android.andrespin.notes.BaseViewModel
import android.andrespin.notes.model.RegData
import android.andrespin.notes.model.di.RegPreference
import android.andrespin.notes.model.interactor.Interactor
import android.andrespin.notes.model.interactor.SyncState
import android.andrespin.notes.model.syncingResultSuccess
import android.andrespin.notes.notes.intent.NotesEvent
import android.andrespin.notes.notes.intent.NotesState
import android.andrespin.notes.profile.logging.intent.LoggingState
import android.andrespin.notes.profile.my_profile.intent.ProfileEvent
import android.andrespin.notes.profile.my_profile.intent.ProfileIntent
import android.andrespin.notes.profile.my_profile.intent.ProfileState
import android.andrespin.notes.utils.converter.DataTypes
import android.andrespin.notes.utils.sorter.ISorter
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel
@Inject constructor(
    private val regPreference: RegPreference,
    private val interactor: Interactor,
    private val converter: DataTypes,
    private val sorter: ISorter
) : BaseViewModel() {

    private var isAuthorized: Boolean = false

    val intent = Channel<ProfileIntent>(Channel.UNLIMITED)

    private val _state = MutableStateFlow<ProfileState>(ProfileState.Idle)
    val state: StateFlow<ProfileState> get() = _state

    private val _event = MutableSharedFlow<ProfileEvent>()

    val event: SharedFlow<ProfileEvent> = _event


    private fun setStateValue(value: ProfileState) {
        _state.value = value
    }

    init {
        handleIntent()
    }

    private suspend fun initAuthorizationStatus() {
        println("initAuthorizationStatus")
        val reg = regPreference.getRegData()
        isAuthorized = !reg.login.isNullOrBlank() && !reg.password.isNullOrBlank()
        println("reg $reg")
        sendAuthorisationStatus(reg)

    }

    private fun handleIntent() {
        viewModelScope.launch {
            intent.consumeAsFlow().collect {
                when (it) {
                    is ProfileIntent.TurnSyncingOn -> turnSyncingModeOnInDatabase()
                    is ProfileIntent.TurnSyncingOff -> turnSyncingModeOffInDatabase()
                    is ProfileIntent.CheckAuthorization -> initAuthorizationStatus()
                    is ProfileIntent.LogOut -> logOut()
                    is ProfileIntent.LogIn -> logIn(it.login, it.pass)
                }
            }
        }
    }

    private fun logIn(login: String, pass: String) {
        regPreference.setPassword(pass)
        regPreference.setLogin(login)
    }

    private suspend fun logOut() {
        regPreference.removeAllRegData()
        isAuthorized = false
        initAuthorizationStatus()
    }

    private suspend fun turnSyncingModeOffInDatabase() {
        setStateValue(ProfileState.Idle)
        delay(1)
        setStateValue(ProfileState.SyncOff)
        println("Off")
        regPreference.setSyncingState(false)
    }

    private suspend fun turnSyncingModeOnInDatabase() {
        println("On")
        regPreference.setSyncingState(true)
        syncDataWithServer()
    }

    private suspend fun syncDataWithServer() {

        // setStateValue(ProfileState.Idle)

        setStateValue(ProfileState.Loading)

        val state = regPreference.getSyncingState()
        val reg = regPreference.getRegData()

        if (reg.login != null)
            interactor.getAllNotes(reg.login)
                ?.findInBackground { objects, e ->
                    if (e == null) {
                        val server = converter.convertParseObjectToNoteEntityList(objects)

                        viewModelScope.launch {

                            val db = interactor.getAllNotes()
                            val sorted = sorter.sortNotesForSyncing(db, server)

                            interactor.saveMissingNotes(
                                sorted.missingNotesDb,
                                sorted.missingNotesServer,
                                reg.login
                            )
                            setStateValue(ProfileState.Success)
                        }
                    } else {
                        setStateValue(ProfileState.Error)
                        e.printStackTrace()
                    }
                }

    }

    private fun errorState(it: SyncState.Error) {
        println("SyncState.Error ${it.error}")
    }

    private fun successState(it: SyncState.Success) {
        println("SyncState.Success ${it.notesFromServer}")
    }

    private suspend fun sendAuthorisationStatus(regData: RegData) {
        if (isAuthorized) {
            println("isAuthorized $isAuthorized")
            setStateValue(ProfileState.Idle)
            //   delay(1)
            setStateValue(ProfileState.ProfileIsAuthorized(regData))
            sendSyncEnability()
        } else {
            println("isAuthorized $isAuthorized")
            setStateValue(ProfileState.Idle)
            //  delay(1)
            setStateValue(ProfileState.ProfileIsNotAuthorized)
        }
    }

    private fun sendSyncEnability() = viewModelScope.launch {
        if (regPreference.getSyncingState())
            if (regPreference.getSyncingResult() == syncingResultSuccess) {
                _event.emit(ProfileEvent.SyncOnSuccess)
            } else {
                _event.emit(ProfileEvent.SyncOnError)
            }
        else
            _event.emit(ProfileEvent.SyncOff)
    }


}