package com.rapsodo.golfperformance.ui.stats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.rapsodo.golfperformance.databinding.FragmentStatsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class StatsFragment : Fragment() {

    private var _binding: FragmentStatsBinding? = null
    private val binding get() = _binding!!
    private val args: StatsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStatsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // In a real app, we would use args.playerId to fetch specific stats
        setupCharts()
    }

    private fun setupCharts() {
        // Mock data for now, would come from ViewModel
        val entries = listOf(
            Entry(1f, 250f),
            Entry(2f, 265f),
            Entry(3f, 258f),
            Entry(4f, 272f),
            Entry(5f, 280f)
        )

        val dataSet = LineDataSet(entries, "Carry Distance (yds)")
        val lineData = LineData(dataSet)
        binding.distanceChart.data = lineData
        binding.distanceChart.invalidate()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
