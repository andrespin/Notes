package android.andrespin.notes.profile.my_profile

import android.andrespin.notes.App
import android.andrespin.notes.BaseFragment
import android.andrespin.notes.R
import android.andrespin.notes.databinding.FragmentProfileBinding
import android.andrespin.notes.model.RegData
import android.andrespin.notes.profile.my_profile.intent.ProfileIntent
import android.andrespin.notes.profile.my_profile.intent.ProfileState
import android.content.Intent
import android.content.pm.PackageManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

import android.os.Build
import android.Manifest
import android.andrespin.notes.profile.my_profile.intent.ProfileEvent

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding, ProfileViewModel>() {

    override val viewModelClass: Class<ProfileViewModel>
        get() = ProfileViewModel::class.java

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentProfileBinding
        get() = FragmentProfileBinding::inflate

    override fun setUpViews() {
        observeViewModel()

        initAddPhotoListener()

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
                    is ProfileState.SyncOff -> hideSyncStatus()
                    is ProfileState.Loading -> showLoading()
                    is ProfileState.Success -> showSuccess()
                    is ProfileState.Error -> showError()
                }
            }
        }

        lifecycleScope.launch {
            viewModel.event.collect {
                when (it) {
                    is ProfileEvent.SyncOnSuccess -> setSyncOnSuccess()
                    is ProfileEvent.SyncOnError -> setSyncOnError()
                    is ProfileEvent.SyncOff -> setSyncOff()
                }


            }
        }

    }

    private fun setSyncOnError() = with(viewBinding.authorized) {
      //  switchSyncing.setOnClickListener(null)
        switchSyncing.isChecked = true
        showError()
    }

    private fun setSyncOff() {
        viewBinding.authorized.switchSyncing.isChecked = false
    }

    private fun setSyncOnSuccess() {
        viewBinding.authorized.switchSyncing.isChecked = true
        showSuccess()
    }

    private fun hideSyncStatus() = with(viewBinding.authorized) {
        imgError.visibility = View.GONE
        imgSuccess.visibility = View.GONE
        progressBarRound.visibility = View.GONE
    }

    private fun showError() = with(viewBinding.authorized) {
        imgError.visibility = View.VISIBLE
        imgSuccess.visibility = View.GONE
        progressBarRound.visibility = View.GONE
    }

    private fun showSuccess() = with(viewBinding.authorized) {
        imgError.visibility = View.GONE
        imgSuccess.visibility = View.VISIBLE
        progressBarRound.visibility = View.GONE
    }

    private fun showLoading() = with(viewBinding.authorized) {
        imgError.visibility = View.GONE
        imgSuccess.visibility = View.GONE
        progressBarRound.visibility = View.VISIBLE
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

    // -------------- new photo adding

    // https://github.com/Dhaval2404/ImagePicker

    private fun pickImageFromGallery() {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    companion object {
        //image pick code
        private val IMAGE_PICK_CODE = 1000;

        //Permission code
        val PERMISSION_CODE = 1001;
    }


    //handle requested permission result


    private fun initAddPhotoListener() {
        //  viewBinding.authorized.imgAddPhoto

        //BUTTON CLICK
        viewBinding.authorized.imgAddPhoto.setOnClickListener {
            //check runtime permission
            //    app.checkSelfPermission()

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (requireActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED
                ) {
                    //permission denied
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE);
                    //show popup to request runtime permission
                    requestPermissions(permissions, PERMISSION_CODE);
                } else {
                    //permission already granted
                    pickImageFromGallery();
                }
            } else {
                //system OS is < Marshmallow
                pickImageFromGallery();
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    //permission from popup granted
                    pickImageFromGallery()
                } else {
                    //permission from popup denied
                    Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    //handle result of picked image
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == -1 && requestCode == IMAGE_PICK_CODE) {
            viewBinding.authorized.imgAva.setImageURI(data?.data)
        }
    }

}