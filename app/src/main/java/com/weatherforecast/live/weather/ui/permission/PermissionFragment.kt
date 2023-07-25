package com.weatherforecast.live.weather.ui.permission

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.weatherforecast.live.weather.R
import com.weatherforecast.live.weather.activities.MainActivity
import com.weatherforecast.live.weather.common.AppSharePreference
import com.weatherforecast.live.weather.databinding.FragmentPermissionBinding
import com.permissionx.guolindev.PermissionX
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PermissionFragment : Fragment() {
    private lateinit var dataBinding: FragmentPermissionBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding = FragmentPermissionBinding.inflate(inflater)
        dataBinding.lifecycleOwner = viewLifecycleOwner
        dataBinding.btnGo.setOnClickListener {
            if (dataBinding.switch1.isChecked) {
                val p = (requireActivity() as? MainActivity)?.preferences
                p?.edit()?.putBoolean(AppSharePreference.FIRST_LAUNCH, false)?.apply()
                (requireActivity() as? MainActivity)?.nav()?.popBackStack(R.id.introFragment, true)
                (requireActivity() as? MainActivity)?.nav()?.navigate(R.id.homeFragment)
            }
        }
        dataBinding.switch1.setOnCheckedChangeListener { buttonView, isChecked ->
            PermissionX.init(requireActivity())
                    .permissions(android.Manifest.permission.ACCESS_COARSE_LOCATION)
                    .request { allGranted, grantedList, deniedList ->
                        dataBinding.switch1.isChecked = allGranted
                    }
        }
        /*dataBinding.switch2.setOnCheckedChangeListener { buttonView, isChecked ->
            PermissionX.init(requireActivity())
                    .permissions(android.Manifest.permission.CAMERA)
                    .request { allGranted, grantedList, deniedList ->
                        dataBinding.switch2.isChecked = allGranted
                    }
        }*/
        return dataBinding.root
    }

    private fun requestCameraPermission() {

    }
}