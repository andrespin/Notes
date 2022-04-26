package android.andrespin.notes.welcome_slides.intent

sealed class WelcomeEvent {

    object OpenApp : WelcomeEvent()

    data class OpenSlide(val slideNumber: Int) : WelcomeEvent()

}