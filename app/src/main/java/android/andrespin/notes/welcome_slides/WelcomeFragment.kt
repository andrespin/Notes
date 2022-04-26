package android.andrespin.notes.welcome_slides


import android.andrespin.notes.BaseFragment
import android.andrespin.notes.R
import android.andrespin.notes.databinding.FragmentLoggingBinding
import android.andrespin.notes.databinding.FragmentWelcomeBinding
import android.andrespin.notes.notes.adapter.NotesAdapter
import android.andrespin.notes.notes.intent.NotesIntent
import android.andrespin.notes.profile.logging.LoggingViewModel
import android.andrespin.notes.welcome_slides.adapter.WelcomeAdapter
import android.andrespin.notes.welcome_slides.intent.WelcomeEvent
import android.andrespin.notes.welcome_slides.intent.WelcomeIntent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WelcomeFragment(

) : BaseFragment<FragmentWelcomeBinding, WelcomeViewModel>() {

    private val adapter: WelcomeAdapter
            by lazy { WelcomeAdapter() }

    override val viewModelClass: Class<WelcomeViewModel>
        get() = WelcomeViewModel::class.java

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentWelcomeBinding
        get() = FragmentWelcomeBinding::inflate

    override fun destroy() {

    }

    override fun setUpViews() {
        initViewPager()
        observeViewModel()
        viewBinding.imgNextRound.setOnClickListener {
            lifecycleScope.launch {
                viewModel.intent.send(WelcomeIntent.NextSlide(getCurrentItem(), getSize()))
            }
        }

    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.event.collect {
                when (it) {
                    is WelcomeEvent.OpenSlide -> openSlide(it)
                }
            }
        }
    }

    private fun openSlide(it: WelcomeEvent.OpenSlide) {
        setIndicators(it.slideNumber)
        setSlide(it.slideNumber)
    }

    private fun initViewPager() {
        viewBinding.viewPager.adapter = adapter
    }

    private fun setIndicators(number: Int) = with(viewBinding) {
        when (number) {
            0 -> imgOne.setImageResource(R.drawable.ic_swipe_indicator_active)
            1 -> imgTwo.setImageResource(R.drawable.ic_swipe_indicator_active)
            2 -> imgThree.setImageResource(R.drawable.ic_swipe_indicator_active)
            3 -> imgFour.setImageResource(R.drawable.ic_swipe_indicator_active)
        }
    }

    private fun setSlide(n: Int) {
        viewBinding.viewPager.currentItem = n
    }


    private fun getSize(): Int = adapter.layouts.size ?: 0

    private fun getCurrentItem(): Int = viewBinding.viewPager.currentItem


}