package android.andrespin.notes.main.intent

sealed class MainEvent {

    data class ActiveSlide(val number: Int) : MainEvent()

}