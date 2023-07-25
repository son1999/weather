package com.weatherforecast.live.weather.ui.home

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.util.Pair
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.example.ap_translator.models.DbConstants
import com.example.ap_translator.models.SingletonLanguageTrans
import com.weatherforecast.live.weather.R
import com.weatherforecast.live.weather.base.fragment.BaseFragment
import com.weatherforecast.live.weather.databinding.FragmentHomeBinding
import com.weatherforecast.live.weather.ui.adapters.ItemLanguageTransAdapter
import com.weatherforecast.live.weather.vision.GraphicOverlay
import com.weatherforecast.live.weather.vision.TextGraphic
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class HomeFragment : BaseFragment() {

    private lateinit var dataBinding: FragmentHomeBinding
    private val viewmodel by viewModels<HomeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding = FragmentHomeBinding.inflate(inflater)
        dataBinding.lifecycleOwner = viewLifecycleOwner
        registerObserverLoadingEvent(viewmodel, viewLifecycleOwner)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    }
}