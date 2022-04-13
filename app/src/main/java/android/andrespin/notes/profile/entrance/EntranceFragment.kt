package android.andrespin.notes.profile.entrance

import android.andrespin.notes.BaseFragment
import android.andrespin.notes.databinding.FragmentEntranceBinding
import android.andrespin.notes.databinding.FragmentNotesBinding
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController

class EntranceFragment : BaseFragment<FragmentEntranceBinding, EntranceViewModel>() {
    override val viewModelClass: Class<EntranceViewModel>
        get() = EntranceViewModel::class.java
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentEntranceBinding
        get() = FragmentEntranceBinding::inflate

    override fun setUpViews() {
        initListeners()
    }

    private fun initListeners() {

        with(viewBinding) {
            imgBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }


    }

}