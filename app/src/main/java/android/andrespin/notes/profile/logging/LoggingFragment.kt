package android.andrespin.notes.profile.logging

import android.andrespin.notes.BaseFragment
import android.andrespin.notes.R
import android.andrespin.notes.databinding.FragmentLoggingBinding
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController

class LoggingFragment : BaseFragment<FragmentLoggingBinding, LoggingViewModel>() {

    override val viewModelClass: Class<LoggingViewModel>
        get() = LoggingViewModel::class.java

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentLoggingBinding
        get() = FragmentLoggingBinding::inflate

    override fun destroy() {

    }

    override fun setUpViews() {
        initListeners()
    }

    private fun initListeners() {
        with(viewBinding) {
            imgBack.setOnClickListener {
                findNavController().popBackStack()
            }

//            imgEntrance.setOnClickListener {
//                findNavController().navigate(R.id.action_profile_to_entrance)
//            }
//
//            imgRegistration.setOnClickListener {
//                findNavController().navigate(R.id.action_profile_to_logging)
//            }

        }
    }


}