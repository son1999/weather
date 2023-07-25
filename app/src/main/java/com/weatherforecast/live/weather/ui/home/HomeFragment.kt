package com.fatherofapps.androidbase.ui.home

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
import com.fatherofapps.androidbase.R
import com.fatherofapps.androidbase.base.fragment.BaseFragment
import com.fatherofapps.androidbase.databinding.FragmentHomeBinding
import com.fatherofapps.androidbase.ui.adapters.ItemLanguageTransAdapter
import com.fatherofapps.androidbase.vision.GraphicOverlay
import com.fatherofapps.androidbase.vision.TextGraphic
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
        setupLanguage()

//        dataBinding.btnNextScr.setOnClickListener {
//            navigateToPage(R.id.multiLangFragment)
//        }
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btnChooseLangFrom: Button = dataBinding.mainBtnChooseLangFrom
        val btnChooseLangTo: Button = dataBinding.mainBtnChooseLangTo

        btnChooseLangFrom.text = SingletonLanguageTrans.getInstance().langTransFrom.name

        btnChooseLangTo.text = SingletonLanguageTrans.getInstance().langTransTo.name

        if ( viewmodel.currentFrom != SingletonLanguageTrans.getInstance().langTransFrom.code!! || viewmodel.currentTo != SingletonLanguageTrans.getInstance().langTransTo.code!!) {
            viewmodel.currentFrom = SingletonLanguageTrans.getInstance().langTransFrom.code!!
            viewmodel.currentTo = SingletonLanguageTrans.getInstance().langTransTo.code!!
            val options = TranslatorOptions.Builder()
                    .setSourceLanguage(viewmodel.currentFrom)
                    .setTargetLanguage(viewmodel.currentTo)
                    .build()

            SingletonLanguageTrans.getInstance().translator = Translation.getClient(options)
            val conditions = DownloadConditions.Builder()
                    .build()
            showLoading(true)
            SingletonLanguageTrans.getInstance().translator?.downloadModelIfNeeded(conditions)
                    ?.addOnSuccessListener {
                        // Model downloaded successfully. Okay to start translating.
                        // (Set a flag, unhide the translation UI, etc.)
                        Log.i("app", "Download success!")
                        showLoading(false)
                        if (viewmodel.mSelectedImage != null) {
                            runTextRecognition(viewmodel.mSelectedImage!!)
                        }
                    }
                    ?.addOnFailureListener { exception ->
                        // Model couldn’t be downloaded or other internal error.
                        // ...
                        showLoading(false)
                    }

        } else {

            runTextRecognition(viewmodel.mSelectedImage)
        }
    }

    private fun setupLanguage() {
        if (viewmodel.currentTo == "") {
            try {
                val defaultLanguageCode = Locale.getDefault().language
                Log.i(DbConstants.APP_NAME, defaultLanguageCode)
                val languageIndex = ItemLanguageTransAdapter.dummyData.indexOfFirst { defaultLanguageCode.contains(it.code!!) }
                SingletonLanguageTrans.getInstance().langTransTo = ItemLanguageTransAdapter.dummyData[languageIndex]
                SingletonLanguageTrans.getInstance().langTransToPositionSeleced = languageIndex
            }
            catch (e: java.lang.Exception) {
                Log.d("", e.localizedMessage, e)
            }
        }

        dataBinding.mainCameraControlView.setBackgroundColor(Color.TRANSPARENT)

        dataBinding.buttonPhoto.setOnClickListener {
            navigateToPage(R.id.savedPhotoFragment)
        }
        dataBinding.apply {
            layoutContentTranslated.isClickable = true
            layoutContentTranslated.setOnClickListener {
                viewmodel.showTranslatedLayout = !viewmodel.showTranslatedLayout
                graphicOverlay.isVisible = viewmodel.showTranslatedLayout
                transparentView.isVisible = viewmodel.showTranslatedLayout
                btnSave.isVisible = viewmodel.showTranslatedLayout
            }

            mainBtnChooseLangFrom.setOnClickListener {
//                val chooseLangToTransActivity : Intent = Intent(Intent(this, LanguageTransActivity::class.java))
//                chooseLangToTransActivity.putExtra("typeOfLang", "from")
//                startActivity(chooseLangToTransActivity)
                SingletonLanguageTrans.getInstance().languageType = "from"
                navigateToPage(R.id.languageTransFragment)
            }

            mainBtnChooseLangTo.setOnClickListener {
//                val chooseLangToTransActivity : Intent = Intent(Intent(this, LanguageTransActivity::class.java))
//                chooseLangToTransActivity.putExtra("typeOfLang", "to")
//                startActivity(chooseLangToTransActivity)
                SingletonLanguageTrans.getInstance().languageType = "to"
                navigateToPage(R.id.languageTransFragment)
            }
            btnSave.setOnClickListener {
                if (viewmodel.mSelectedImage != null) {
                    getSaveImageFilePath()
                }
            }

            buttonCamera.setOnClickListener {
                ImagePicker.with(this@HomeFragment)
                        .crop(1080f, 1080f)                    //Crop image(Optional), Check Customization for more option
                        //.compress(1920)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(
                                1080,
                                1080
                        )    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start()
            }
            if (viewmodel.isFristTime) {
                ImagePicker.with(this@HomeFragment)
                        .cameraOnly()
                        .crop(1080f, 1080f)  	//Crop image(Optional), Check Customization for more option
                        //.compress(1920)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start()
                viewmodel.isFristTime = false
            }
            imageView.setImageBitmap(viewmodel.mSelectedImage)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            val uri: Uri = data?.data!!
            Log.i("Phuc", uri.toString())
            viewmodel.mSelectedImage = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, uri)

            if (viewmodel.mSelectedImage != null) {
                // Get the dimensions of the View
                val targetedSize: Pair<Int, Int> = getTargetedWidthHeight()

                val targetWidth = targetedSize.first
                val maxHeight = targetedSize.second

                // Determine how much to scale down the image

                // Determine how much to scale down the image
                val scaleFactor: Float = Math.max(
                        viewmodel.mSelectedImage!!.getWidth().toFloat() / targetWidth.toFloat(),
                        viewmodel.mSelectedImage!!.getHeight().toFloat() / maxHeight.toFloat()
                )

                val resizedBitmap = Bitmap.createScaledBitmap(
                        viewmodel.mSelectedImage!!, (viewmodel.mSelectedImage!!.getWidth() / scaleFactor).toInt(), (viewmodel.mSelectedImage!!.getHeight() / scaleFactor).toInt(),
                        true
                )

                dataBinding.imageView.setImageBitmap(resizedBitmap)
                viewmodel.mSelectedImage = resizedBitmap
                runTextRecognition(viewmodel.mSelectedImage!!)
            }

            // Use Uri object instead of File to avoid storage permissions
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getTargetedWidthHeight(): Pair<Int, Int> {
        val targetWidth: Int
        val targetHeight: Int
        val maxWidthForPortraitMode: Int = getImageMaxWidth()
        val maxHeightForPortraitMode: Int = getImageMaxHeight()
        targetWidth = maxWidthForPortraitMode
        targetHeight = maxHeightForPortraitMode
        return Pair(targetWidth, targetHeight)
    }

    private fun getImageMaxWidth(): Int {
        if (viewmodel.mImageMaxWidth == 0) {
            // Calculate the max width in portrait mode. This is done lazily since we need to
            // wait for
            // a UI layout pass to get the right values. So delay it to first time image
            // rendering time.
            viewmodel.mImageMaxWidth = dataBinding.imageView.width
        }
        return viewmodel.mImageMaxWidth
    }

    private fun getImageMaxHeight(): Int {
        if (viewmodel.mImageMaxHeight == 0) {
            // Calculate the max width in portrait mode. This is done lazily since we need to
            // wait for
            // a UI layout pass to get the right values. So delay it to first time image
            // rendering time.
            viewmodel.mImageMaxHeight = dataBinding.imageView.height
        }
        return viewmodel.mImageMaxHeight
    }

    fun getSaveImageFilePath(): String {
        val YOUR_FOLDER_NAME = DbConstants.getAppLable(requireContext())
        val mediaStorageDir = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), YOUR_FOLDER_NAME
        )
        // Create a storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(YOUR_FOLDER_NAME, "Failed to create directory")
            }
        }

        // Create a media file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageName = "${YOUR_FOLDER_NAME}_$timeStamp.jpg"
        val selectedOutputPath = mediaStorageDir.path + File.separator + imageName
        Log.d(YOUR_FOLDER_NAME, "selected camera path $selectedOutputPath")
        dataBinding.layoutContentTranslated.setDrawingCacheEnabled(true)
        dataBinding.layoutContentTranslated.buildDrawingCache()
        var bitmap: Bitmap = Bitmap.createBitmap(dataBinding.layoutContentTranslated.getDrawingCache())
        val maxSize = 1080
        val bWidth = bitmap.width
        val bHeight = bitmap.height
        bitmap = if (bWidth > bHeight) {
            val imageHeight = Math.abs(maxSize * (bitmap.width.toFloat() / bitmap.height.toFloat())).toInt()
            Bitmap.createScaledBitmap(bitmap, maxSize, imageHeight, true)
        } else {
            val imageWidth = Math.abs(maxSize * (bitmap.width.toFloat() / bitmap.height.toFloat())).toInt()
            Bitmap.createScaledBitmap(bitmap, imageWidth, maxSize, true)
        }
        dataBinding.layoutContentTranslated.setDrawingCacheEnabled(false)
        dataBinding.layoutContentTranslated.destroyDrawingCache()
        var fOut: OutputStream? = null
        try {
            val file = File(selectedOutputPath)
            fOut = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut)
            fOut.flush()
            fOut.close()
            showToast(getString(R.string.saved_successfully))
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
        return selectedOutputPath
    }

    private fun runTextRecognition(bitmap: Bitmap?) {
        if (bitmap == null) {return}
        val image = InputImage.fromBitmap(bitmap, 0)
        dataBinding.imageView.setImageBitmap(bitmap)
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        recognizer.process(image)
                .addOnSuccessListener { texts ->
                    processTextRecognitionResult(texts)
                }
                .addOnFailureListener { e -> // Task failed with an exception
                    e.printStackTrace()
                }
    }

    private fun processTextRecognitionResult(texts: Text) {
        dataBinding.graphicOverlay.clear()
        val blocks = texts.textBlocks
        if (blocks.size == 0) {
            showToast("No text found")
            return
        }
        viewmodel.showTranslatedLayout = true

        dataBinding.graphicOverlay.isVisible = viewmodel.showTranslatedLayout
        dataBinding.transparentView.isVisible = viewmodel.showTranslatedLayout
        dataBinding.btnSave.isVisible = viewmodel.showTranslatedLayout
        for (i in blocks.indices) {
            val lines = blocks[i].lines
            for (j in lines.indices) {
//                val elements = lines[j].elements
                //for (k in elements.indices) {
                SingletonLanguageTrans.getInstance().translator?.translate(lines[j].text)
                        ?.addOnSuccessListener { translatedText ->
                            val textGraphic: GraphicOverlay.Graphic = TextGraphic(dataBinding.graphicOverlay, lines[j], lines[j].boundingBox, translatedText)
                            dataBinding.graphicOverlay.add(textGraphic)   // Translation successful.
                        }
                        ?.addOnFailureListener { exception ->
                            // Error.
                            // ...
                        }
                //}
            }
        }
    }

    private fun showToast(s: String) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show()
    }


}