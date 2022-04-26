package android.andrespin.notes.main.intent

sealed class MainState {

    object Idle : MainState()

    object FirstLaunch : MainState()

    object Launch : MainState()

}