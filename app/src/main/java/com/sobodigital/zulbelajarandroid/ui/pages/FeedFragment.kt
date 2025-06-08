package com.sobodigital.zulbelajarandroid.ui.pages

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sobodigital.zulbelajarandroid.data.model.Story
import com.sobodigital.zulbelajarandroid.databinding.FragmentFeedStoriesBinding
import com.sobodigital.zulbelajarandroid.ui.adapter.LoadingStateAdapter
import com.sobodigital.zulbelajarandroid.ui.adapter.StoryAdapterWithPaging
import com.sobodigital.zulbelajarandroid.viewmodel.FeedViewModel
import com.sobodigital.zulbelajarandroid.viewmodel.FeedViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.launch


class FeedFragment : Fragment() {
    private lateinit var storyRecylerView: RecyclerView
    private lateinit var adapterWithPaging: StoryAdapterWithPaging
    private lateinit var viewModel: FeedViewModel
    private var stories = listOf<Story>()

    private fun showRecyclerList(pagingData: PagingData<Story>) {
        storyRecylerView.layoutManager = LinearLayoutManager(context)
        val adapter = StoryAdapterWithPaging()
        adapterWithPaging = adapter
        storyRecylerView.adapter = adapter

        adapter.setOnItemClickCallback(object: StoryAdapterWithPaging.OnItemClickCallback {
            override fun onItemClicked(data: Story) {
                val intent = Intent(context, StoryDetailActivity::class.java)
                intent.putExtra("id", data.id)
                startActivity(intent)
            }

        })

        storyRecylerView.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )

        adapter.submitData(lifecycle, pagingData)

        lifecycleScope.launch {
            adapterWithPaging.loadStateFlow
                .distinctUntilChangedBy { it.refresh }
                .collectLatest { loadState ->
                    val isLoading = loadState.refresh is LoadState.Loading
                    viewModel.setLoading(isLoading)
                }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentFeedStoriesBinding.inflate(layoutInflater)
        storyRecylerView = binding.rvEvent

        val factory: FeedViewModelFactory = FeedViewModelFactory.getInstance(requireActivity())
        val feedViewModel: FeedViewModel by viewModels { factory }
        viewModel = feedViewModel

        feedViewModel.fetchStoryWithPaging()

        feedViewModel.getStories().observe(viewLifecycleOwner, { data ->
            showRecyclerList(data)

        })

        feedViewModel.isLoading.observe(viewLifecycleOwner) { isloading ->
            if(isloading) {
                binding.loading.visibility = View.VISIBLE
                return@observe
            }
            binding.loading.visibility = View.GONE
            return@observe
        }

        feedViewModel.errorData.observe(viewLifecycleOwner) { data ->
            Log.e(TAG, "Error data $data")
            val message = data?.error

            if(data?.code == 401) {
                feedViewModel.clearAllSetting()
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

        val slideRight = ValueAnimator.ofFloat(0f, 10f).apply {
            duration = 1500
            addUpdateListener { updatedAnimation ->
                binding.rvEvent.translationX = updatedAnimation.animatedValue as Float
            }
        }

        val slideLeft = ValueAnimator.ofFloat(10f, 1f).apply {
            duration = 1000
            addUpdateListener { updatedAnimation ->
                binding.rvEvent.translationX = updatedAnimation.animatedValue as Float
            }
        }

        AnimatorSet().apply {
            play(slideRight).before(slideLeft)
            start()
        }

        return binding.root
    }

    companion object {
        private val TAG = FeedFragment::class.java.simpleName
    }
}