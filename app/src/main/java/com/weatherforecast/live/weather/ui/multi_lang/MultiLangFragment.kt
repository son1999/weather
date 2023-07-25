package com.weatherforecast.live.weather.ui.multi_lang

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.weatherforecast.live.weather.R
import com.weatherforecast.live.weather.base.fragment.BaseFragment
import com.weatherforecast.live.weather.databinding.FragmentMultiLangBinding
import com.weatherforecast.live.weather.ui.adapters.ItemMultiLangAdapter
import com.weatherforecast.live.weather.ui.dialog_rating.DialogRatingFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MultiLangFragment : BaseFragment() {

    private lateinit var dataBinding: FragmentMultiLangBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding = FragmentMultiLangBinding.inflate(inflater)
        dataBinding.lifecycleOwner = viewLifecycleOwner


        //show rating dialog
        DialogRatingFragment().show(childFragmentManager, "show rating fragment")

        //navigation
        dataBinding.btnChooseLang.setOnClickListener {

            navigateToPage(R.id.introFragment)
        }

        //ui
        dataBinding.tvTitile.setTypeface(Typeface.DEFAULT_BOLD)

        //bind rcv langs list
        val rcvLangs = dataBinding.rcvLangs
        rcvLangs.adapter = ItemMultiLangAdapter(
            ItemMultiLangAdapter.dummyData,
            requireContext(),
            0,
        ){ position, item ->

        }
        rcvLangs.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        return dataBinding.root


    }
}