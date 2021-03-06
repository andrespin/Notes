package android.andrespin.notes.profile.entrance

import android.andrespin.notes.BaseFragment
import android.andrespin.notes.R
import android.andrespin.notes.databinding.FragmentEntranceBinding
import android.andrespin.notes.databinding.FragmentNotesBinding
import android.andrespin.notes.model.RegData
import android.andrespin.notes.profile.entrance.intent.EntranceEvent
import android.andrespin.notes.profile.entrance.intent.EntranceIntent
import android.andrespin.notes.profile.entrance.intent.EntranceState
import android.andrespin.notes.profile.my_profile.intent.ProfileState
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EntranceFragment : BaseFragment<FragmentEntranceBinding, EntranceViewModel>() {
    override val viewModelClass: Class<EntranceViewModel>
        get() = EntranceViewModel::class.java
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentEntranceBinding
        get() = FragmentEntranceBinding::inflate

    override fun setUpViews() {
        initListeners()
        observeViewModel()
    }

    private fun observeViewModel() {

       // getResources().getString()

        lifecycleScope.launch {
            viewModel.state.collect {
                when (it) {
                    is EntranceState.LogIn -> logIn()
                    is EntranceState.FieldsAreNotFilled ->
                        toastMessage("Заполните все пустые поля")
                    is EntranceState.Error -> handleError()
                }
            }
        }

        lifecycleScope.launch {
            viewModel.event.collect {
                when (it) {
                    is EntranceEvent.FieldsAreNotFilled ->
                        toastMessage(getString(R.string.fill_all_empty_fields))
                    is EntranceEvent.PassIsNotCorrect ->
                        toastMessage("Неверный логин или пароль")
                    is EntranceEvent.LoginIsNotFound ->
                        toastMessage("Пользователь с таким логином не найден")

                }
            }
        }


    }

    private fun handleError() {
        println("Error")
    }

    private fun logIn() {
        findNavController().popBackStack()
    }

    private fun initListeners() {

        with(viewBinding) {
            imgBack.setOnClickListener {
                findNavController().popBackStack()
            }

            imgEnter.setOnClickListener {
                lifecycleScope.launch(Dispatchers.Main) {
//                    viewModel.intent.send(EntranceIntent.Click)

                    viewModel.intent.send(
                        EntranceIntent.LogIn(
                            RegData(
                                editLogin.text.toString(),
                                editPassword.text.toString()
                            )
                        )
                    )
                }
            }
        }
    }
}