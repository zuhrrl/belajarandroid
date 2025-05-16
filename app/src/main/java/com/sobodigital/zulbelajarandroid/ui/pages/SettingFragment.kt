package com.sobodigital.zulbelajarandroid.ui.pages

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.viewModels
import com.sobodigital.zulbelajarandroid.R
import com.sobodigital.zulbelajarandroid.data.local.SettingPreferences
import com.sobodigital.zulbelajarandroid.data.local.dataStore
import com.sobodigital.zulbelajarandroid.databinding.FragmentSettingBinding
import com.sobodigital.zulbelajarandroid.viewmodel.EventMainViewModel
import com.sobodigital.zulbelajarandroid.viewmodel.EventMainViewModelFactory
import com.sobodigital.zulbelajarandroid.viewmodel.SettingViewModel
import com.sobodigital.zulbelajarandroid.viewmodel.SettingViewModelFactory


class SettingFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentSettingBinding.inflate(layoutInflater)

        val factory: SettingViewModelFactory = SettingViewModelFactory.getInstance(requireContext())
        val viewModel: SettingViewModel by viewModels { factory }

        viewModel.getThemeSettings().observe(viewLifecycleOwner) { isDarkModeActive: Boolean ->
            binding.switchTheme.isChecked = isDarkModeActive
        }

        binding.switchTheme.setOnCheckedChangeListener { _, isChecked ->
            viewModel.saveThemeSetting(isChecked)
        }

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SettingFragment().apply {

            }
    }
}