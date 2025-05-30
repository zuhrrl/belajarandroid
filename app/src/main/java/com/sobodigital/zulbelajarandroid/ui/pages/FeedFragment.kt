package com.sobodigital.zulbelajarandroid.ui.pages

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sobodigital.zulbelajarandroid.data.model.Story
import com.sobodigital.zulbelajarandroid.databinding.FragmentFeedStoriesBinding
import com.sobodigital.zulbelajarandroid.ui.adapter.StoryAdapter
import com.sobodigital.zulbelajarandroid.utils.navigateHome
import com.sobodigital.zulbelajarandroid.utils.navigateToLogin
import com.sobodigital.zulbelajarandroid.viewmodel.FeedViewModel
import com.sobodigital.zulbelajarandroid.viewmodel.FeedViewModelFactory


class FeedFragment : Fragment() {
    private lateinit var eventRecyclerView: RecyclerView
    private var stories = listOf<Story>()

    private fun showRecyclerList(list: List<Story>) {
        eventRecyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = StoryAdapter(list)
        eventRecyclerView.adapter = adapter

        adapter.setOnItemClickCallback(object: StoryAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Story) {
                val intent = Intent(context, StoryDetailActivity::class.java)
                intent.putExtra("id", data.id)
                startActivity(intent)
            }

        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentFeedStoriesBinding.inflate(layoutInflater)
        eventRecyclerView = binding.rvEvent

        val factory: FeedViewModelFactory = FeedViewModelFactory.getInstance(requireActivity())
        val feedViewModel: FeedViewModel by viewModels { factory }

        feedViewModel.fetchStory()
        feedViewModel.listEvent.observe(viewLifecycleOwner) { data ->
            Log.d(TAG, "OBSERVER $data")
            stories = data
            showRecyclerList(stories)
        }

        feedViewModel.isLoading.observe(viewLifecycleOwner) { isloading ->
            if(isloading) {
                binding.loading.visibility = View.VISIBLE
                return@observe
            }
            binding.loading.visibility = View.GONE
            return@observe
        }

        feedViewModel.errorData.observe(viewLifecycleOwner) { data ->
            Log.e(TAG, "error data $data")
            val message = data?.error

            if(data?.code == 401) {
                feedViewModel.clearAllSetting()
//                navigateToLogin(requireContext())
                return@observe
            }
            if(message != null && stories.isEmpty()) {
                binding.errorMessage.visibility = View.VISIBLE
                binding.errorMessage.text = message
                return@observe
            }
            binding.errorMessage.visibility = View.GONE
            return@observe
        }

        return binding.root
    }

    companion object {
        private const val TAG = "UpcomingEventFragment"
    }
}