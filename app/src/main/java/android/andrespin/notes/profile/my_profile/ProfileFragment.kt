package android.andrespin.notes.profile.my_profile

import android.andrespin.notes.BaseFragment
import android.andrespin.notes.R
import android.andrespin.notes.databinding.FragmentProfileBinding
import android.andrespin.notes.profile.my_profile.intent.ProfileIntent
import android.andrespin.notes.profile.my_profile.intent.ProfileState
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class ProfileFragment : BaseFragment<FragmentProfileBinding, ProfileViewModel>() {

    override val viewModelClass: Class<ProfileViewModel>
        get() = ProfileViewModel::class.java

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentProfileBinding
        get() = FragmentProfileBinding::inflate

    override fun setUpViews() {
        observeViewModel()
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.state.collect {
                when (it) {
                    is ProfileState.ProfileIsAuthorized -> showAuthorizedProfile()
                    is ProfileState.ProfileIsNotAuthorized -> showNotAuthorizedProfile()
                }
            }
        }
    }

    private fun showNotAuthorizedProfile() {
        showNotAuthorizedContent()
        initAuthorizedContentListeners()
    }

    private fun showNotAuthorizedContent() {
        viewBinding.authorized.root.visibility = View.GONE
        viewBinding.notAuthorized.root.visibility = View.VISIBLE
    }

    private fun initAuthorizedContentListeners() {
        with(viewBinding.authorized) {
            imgBackAuthorized.setOnClickListener {
                findNavController().popBackStack()
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
        }

    }

    private fun showAuthorizedProfile() {
        viewBinding.authorized.root.visibility = View.GONE
        viewBinding.notAuthorized.root.visibility = View.VISIBLE
        initNotAuthorizedListenersContent()
    }

    private fun initNotAuthorizedListenersContent() {
        with(viewBinding.notAuthorized) {

            imgBack.setOnClickListener {
                findNavController().popBackStack()
            }

            imgEntrance.setOnClickListener {
                findNavController().navigate(R.id.action_profile_to_entrance)
                println("Click")
            }

            imgRegistration.setOnClickListener {
                findNavController().navigate(R.id.action_profile_to_logging)
            }

        }
    }

}