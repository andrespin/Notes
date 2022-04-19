package android.andrespin.notes.profile.logging

import android.andrespin.notes.BaseFragment
import android.andrespin.notes.R
import android.andrespin.notes.databinding.FragmentLoggingBinding
import android.andrespin.notes.profile.entrance.intent.EntranceState
import android.andrespin.notes.profile.logging.intent.LoggingIntent
import android.andrespin.notes.profile.logging.intent.LoggingState
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_logging.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoggingFragment : BaseFragment<FragmentLoggingBinding, LoggingViewModel>() {

    override val viewModelClass: Class<LoggingViewModel>
        get() = LoggingViewModel::class.java

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentLoggingBinding
        get() = FragmentLoggingBinding::inflate

    override fun destroy() {

    }

    override fun setUpViews() {
        initListeners()
        observeViewModel()
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.state.collect {
                when (it) {
                    is LoggingState.Idle -> idle()
                    is LoggingState.LoginIsBusy -> loginIsBusy()
                    is LoggingState.LoginIsOk -> loginIsOk()
                    is LoggingState.PassIsNotRepeated -> passIsNotRepeated()
                    is LoggingState.PassIsOk -> passIsOk()
                    is LoggingState.RegDataCorrect -> saveRegData()
                    is LoggingState.FieldsAreNotFilled ->
                        toastMessage("Заполните все пустые поля")
                }
            }
        }
    }

    private suspend fun saveRegData() {
        viewModel.intent.send(LoggingIntent.SaveRegData)
        findNavController().popBackStack()
    }

    private fun passIsOk() = with(viewBinding) {
        imgEnterPassword.setBackgroundResource(R.drawable.button_field_red)
        imgEnterPassword.setBackgroundResource(R.drawable.button_field_red)
    }

    private fun passIsNotRepeated() = with(viewBinding) {
        toastMessage("Повторите правильно пароль!")
        imgEnterPassword.setBackgroundResource(R.drawable.button_field_red_error)
        imgRepeatPassword.setBackgroundResource(R.drawable.button_field_red_error)
    }


    private fun loginIsOk() = with(viewBinding) {
        imgEnterLogin.setBackgroundResource(R.drawable.button_field_yellow)
    }

    private fun loginIsBusy() = with(viewBinding) {
        toastMessage("Такой логин занят")
        imgEnterLogin.setBackgroundResource(R.drawable.button_field_yellow_error)
    }

    private fun idle() {

    }


    private fun initListeners() = with(viewBinding) {
        imgBack.setOnClickListener {
            findNavController().popBackStack()
        }

        imgRegister.setOnClickListener {
            with(viewBinding) {
                lifecycleScope.launch(Dispatchers.Main) {
                    viewModel.intent.send(
                        LoggingIntent.Register(
                            editLogin.text.toString(),
                            editEnterPassword.text.toString(),
                            editRepeatPassword.text.toString()
                        )
                    )
                }
            }
        }
    }


}