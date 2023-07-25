package com.weatherforecast.live.weather.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.weatherforecast.live.weather.base.fragment.BaseFragment
import com.weatherforecast.live.weather.databinding.FragmentHomeBinding
import com.weatherforecast.live.weather.databinding.FragmentSearchBinding
import com.weatherforecast.live.weather.ui.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : BaseFragment() {

    private lateinit var dataBinding: FragmentSearchBinding
    private val viewmodel by viewModels<SearchViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        dataBinding = FragmentSearchBinding.inflate(inflater)
        dataBinding.lifecycleOwner = viewLifecycleOwner
        registerObserverLoadingEvent(viewmodel, viewLifecycleOwner)
        viewmodel.fetchData()
        dataBinding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
        return dataBinding.root
    }

}