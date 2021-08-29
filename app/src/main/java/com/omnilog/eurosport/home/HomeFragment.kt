package com.omnilog.eurosport.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.omnilog.eurosport.databinding.HomeFragmentBinding
import com.omnilog.eurosport.home.network.ApiHelper
import com.omnilog.eurosport.home.repository.HomeRepositoryImpl
import com.omnilog.eurosport.home.usecase.HomeUseCaseImpl
import com.omnilog.eurosport.player.PlayerActivity
import com.omnilog.eurosport.player.PlayerActivity.Companion.EXTRA_URL
import kotlinx.coroutines.flow.collect

class HomeFragment : Fragment() {

    // That should be instantiated by DI
    private val homeApi = ApiHelper.getHomeApiInterface()
    private val homeRepository = HomeRepositoryImpl(homeApi)
    private val homeUseCase = HomeUseCaseImpl(homeRepository)
    private val viewModelFactory = HomeViewModelFactory(homeUseCase)
    private val viewModel: HomeViewModel by viewModels { viewModelFactory }

    private lateinit var binding: HomeFragmentBinding

    private val adapter: ContentListAdapter by lazy {
        ContentListAdapter(
            onVideoClickListener = viewModel,
            onStoryClickListener = viewModel
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = HomeFragmentBinding.inflate(inflater)

        val navController = findNavController()
        binding.toolbar.setupWithNavController(navController)

        with(binding.listView) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@HomeFragment.adapter
        }

        initViewStateObserver(binding)
        initActionFlow()

        binding.swipeToRefresh.setOnRefreshListener {
            viewModel.getHomeContent()
        }

        return binding.root
    }

    private fun initActionFlow() {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.actions.collect { handleActions(it) }
        }
    }

    private fun initViewStateObserver(binding: HomeFragmentBinding) {
        viewModel.viewState.observe(viewLifecycleOwner) {
            processLoading(it is HomeViewState.Loading)
            when (it) {
                is HomeViewState.Error -> Snackbar.make(
                    binding.root,
                    it.message,
                    Snackbar.LENGTH_SHORT
                ).show()
                is HomeViewState.Success -> adapter.submitList(it.contentList)
            }
        }
    }

    private fun processLoading(isLoading: Boolean) {
        binding.swipeToRefresh.isRefreshing = isLoading
    }

    private fun handleActions(action: HomeAction) {
        when (action) {
            is HomeAction.GoToVideoPlayer -> {
                val intent = Intent(context, PlayerActivity::class.java).apply {
                    putExtra(EXTRA_URL, action.url)
                }
                startActivity(intent)
            }
            is HomeAction.GoToStoryDetail -> {
                // We should only pass the story id and get the full information from an API call
                val direction = HomeFragmentDirections.actionHomeToStory(action.story)
                view?.findNavController()?.navigate(direction)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getHomeContent()
    }
}