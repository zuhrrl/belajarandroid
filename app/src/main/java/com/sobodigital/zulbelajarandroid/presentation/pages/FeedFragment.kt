package com.sobodigital.zulbelajarandroid.presentation.pages

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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.switchMap
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sobodigital.zulbelajarandroid.data.Result
import com.sobodigital.zulbelajarandroid.databinding.FragmentFeedStoriesBinding
import com.sobodigital.zulbelajarandroid.domain.model.Story
import com.sobodigital.zulbelajarandroid.presentation.adapter.LoadingStateAdapter
import com.sobodigital.zulbelajarandroid.presentation.adapter.StoryAdapterWithPaging
import com.sobodigital.zulbelajarandroid.presentation.viewmodel.FeedViewModel
import com.sobodigital.zulbelajarandroid.presentation.viewmodel.FeedViewModelFactory
import com.sobodigital.zulbelajarandroid.utils.navigateToLogin
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.launch

class FeedFragment : Fragment() {
    private lateinit var storyRecylerView: RecyclerView
    private lateinit var adapterWithPaging: StoryAdapterWithPaging
    private lateinit var viewModel: FeedViewModel
    private var stories = listOf<Story>()

    private fun showRecyclerList(feedViewModel: FeedViewModel) {
        Log.d(TAG, "Show recyler list")
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

        val pagingData = feedViewModel.pagingData.switchMap { it }
        pagingData.observe(viewLifecycleOwner, { data ->
            Log.d(TAG, "getStoreis()")
            adapter.submitData(lifecycle, data)

        })


        lifecycleScope.launch {
            adapterWithPaging.loadStateFlow
                .distinctUntilChangedBy { it.refresh }
                .collectLatest { loadState ->

                    when (val refreshState = loadState.refresh) {
                        is LoadState.Loading -> {
                            viewModel.setLoading(true)
                            viewModel.setErrorData(Result.Error(null))
                        }

                        is LoadState.NotLoading -> {
                            viewModel.setLoading(false)
                            viewModel.setErrorData(Result.Error(null))
                        }

                        is LoadState.Error -> {
                            viewModel.setLoading(false)

                            val errorMessage = refreshState.error.localizedMessage ?: "Unknown error"
                            Log.d(TAG, "error loadstate ${errorMessage}")

                            var errorCode : Int? = null
                            if(errorMessage.contains(Regex("code", RegexOption.IGNORE_CASE))) {
                                errorCode = errorMessage.split(":")[1].trim().toInt()
                            }
                            viewModel.setErrorData(Result.Error(errorMessage, errorCode))
                        }
                    }
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

        Log.d(TAG, "Feed fagment onCreateView")

        feedViewModel.fetchStoryWithPaging()

        feedViewModel.isLoading.observe(viewLifecycleOwner) { isloading ->
            if(isloading) {
                binding.loading.visibility = View.VISIBLE
                return@observe
            }
            binding.loading.visibility = View.GONE
            return@observe
        }

        feedViewModel.errorData.observe(viewLifecycleOwner) { data ->
            Log.e(TAG, "Error data feed $data")
            Log.e(TAG, "Error data feed code ${data?.code}")

            val message = data?.error

            if(data?.code == 401) {
                Log.d(TAG, "Response is 401")
                feedViewModel.clearAllSetting()
                 navigateToLogin(requireContext())
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

        showRecyclerList(feedViewModel)


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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
    companion object {
        private val TAG = FeedFragment::class.java.simpleName
    }
}