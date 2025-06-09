package com.sobodigital.zulbelajarandroid.ui.pages

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.sobodigital.zulbelajarandroid.databinding.FragmentSettingBinding
import com.sobodigital.zulbelajarandroid.utils.navigateToLogin
import com.sobodigital.zulbelajarandroid.viewmodel.SettingViewModel
import com.sobodigital.zulbelajarandroid.viewmodel.SettingViewModelFactory


class SettingFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentSettingBinding.inflate(layoutInflater)

        val factory: SettingViewModelFactory = SettingViewModelFactory.getInstance(requireContext())
        val viewModel: SettingViewModel by viewModels { factory }

        val settingFactory: SettingViewModelFactory = SettingViewModelFactory.getInstance(requireContext())
        val settingViewModel: SettingViewModel by viewModels { settingFactory }


        viewModel.getThemeSettings().observe(viewLifecycleOwner) { isDarkModeActive: Any ->
            binding.switchTheme.isChecked = isDarkModeActive as Boolean
        }

        binding.switchTheme.setOnCheckedChangeListener { _, isChecked ->
            viewModel.saveThemeSetting(isChecked)
        }

        binding.btnLogout.setOnClickListener {
            viewModel.logoutApp()
            navigateToLogin(requireContext())

        }
        return binding.root
    }

    companion object {
        private var TAG = SettingFragment::class.java.simpleName
    }

}