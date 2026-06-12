package com.rapsodo.golfperformance.ui.playerdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.rapsodo.golfperformance.databinding.FragmentPlayerDetailBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayerDetailFragment : Fragment() {

    private var _binding: FragmentPlayerDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PlayerDetailViewModel by viewModel()
    private val args: PlayerDetailFragmentArgs by navArgs()
    private lateinit var adapter: ShotAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentPlayerDetailBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadShots(args.playerId)
        setupRecyclerView()
        observeViewModel()
        
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.refreshShots(args.playerId)
        }
        
        binding.btnStats.setOnClickListener {
            val action = PlayerDetailFragmentDirections.actionPlayerDetailFragmentToStatsFragment(args.playerId)
            findNavController().navigate(action)
        }
    }

    private fun setupRecyclerView() {
        adapter = ShotAdapter()
        binding.shotsRecyclerView.adapter = adapter
        
        viewLifecycleOwner.lifecycleScope.launch {
            adapter.loadStateFlow.collectLatest { loadStates ->
                val isListEmpty = loadStates.refresh is androidx.paging.LoadState.NotLoading && adapter.itemCount == 0
                binding.emptyState.visibility = if (isListEmpty) View.VISIBLE else View.GONE
                binding.swipeRefresh.visibility = if (isListEmpty && !binding.swipeRefresh.isRefreshing) View.GONE else View.VISIBLE
            }
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.pagingShots.collectLatest { pagingData ->
                    adapter.submitData(pagingData)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
