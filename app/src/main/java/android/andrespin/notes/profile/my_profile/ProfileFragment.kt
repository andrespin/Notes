package android.andrespin.notes.profile.my_profile

import android.andrespin.notes.BaseFragment
import android.andrespin.notes.R
import android.andrespin.notes.databinding.FragmentProfileBinding
import android.andrespin.notes.model.RegData
import android.andrespin.notes.profile.my_profile.intent.ProfileIntent
import android.andrespin.notes.profile.my_profile.intent.ProfileState
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding, ProfileViewModel>() {

    override val viewModelClass: Class<ProfileViewModel>
        get() = ProfileViewModel::class.java

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentProfileBinding
        get() = FragmentProfileBinding::inflate

    override fun setUpViews() {
        observeViewModel()

        lifecycleScope.launch(Dispatchers.Main) {
            viewModel.intent.send(ProfileIntent.CheckAuthorization)
        }

    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.state.collect {
                when (it) {
                    is ProfileState.ProfileIsAuthorized -> showAuthorizedProfile(it.data)
                    is ProfileState.ProfileIsNotAuthorized -> showNotAuthorizedProfile()
                }
            }
        }
    }

    private fun showNotAuthorizedProfile() {
        println("Not Authorized")
        showNotAuthorizedContent()
        initNotAuthorizedListenersContent()
    }

    private fun showAuthorizedProfile(data: RegData) {
        println("showAuthorizedProfile(data: RegData) ${data.login}")
        showAuthorizedContent()
        initAuthorizedContentListeners()
        setAuthorizedContent(data)
    }

    private fun setAuthorizedContent(data: RegData) = with(viewBinding.authorized) {
        txtLoginAuthorized.text = data.login
    }

    private fun initAuthorizedContentListeners() {
        with(viewBinding.authorized) {

            imgBackAuthorized.setOnClickListener {
                lifecycleScope.launch {
                    findNavController().popBackStack()
                }
            }

            switchSyncing.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    lifecycleScope.launch {
                        viewModel.intent.send(ProfileIntent.TurnSyncingOn)
                    }
                } else {
                    lifecycleScope.launch {
                        viewModel.intent.send(ProfileIntent.TurnSyncingOff)
                    }
                }
            }

            imgLogOut.setOnClickListener {
                lifecycleScope.launch {
                    viewModel.intent.send(ProfileIntent.LogOut)
                }
            }
        }

    }


    private fun initNotAuthorizedListenersContent() {
        with(viewBinding.notAuthorized) {

            imgBack.setOnClickListener {
                findNavController().popBackStack()
            }

            imgEntrance.setOnClickListener {


                findNavController().navigate(R.id.action_profile_to_entrance)

            }

            imgRegistration.setOnClickListener {
                findNavController().navigate(R.id.action_profile_to_logging)
            }

        }
    }

    private fun showAuthorizedContent() {
        viewBinding.authorized.root.visibility = View.VISIBLE
        viewBinding.notAuthorized.root.visibility = View.GONE
    }

    private fun showNotAuthorizedContent() {
        viewBinding.authorized.root.visibility = View.GONE
        viewBinding.notAuthorized.root.visibility = View.VISIBLE
    }




}