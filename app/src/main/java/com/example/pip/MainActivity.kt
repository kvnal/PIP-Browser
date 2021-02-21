package com.example.pip

import android.annotation.SuppressLint
import android.app.PictureInPictureParams
import android.content.Context
import android.content.res.Configuration
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.util.Rational
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.pip.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var Binding: ActivityMainBinding
    val URL:String="https://www.google.com"
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        Binding= ActivityMainBinding.inflate(layoutInflater)
        val view = Binding.root
        setContentView(view)
        Binding.web.webViewClient = WebViewClient()
        Binding.web.settings.javaScriptEnabled=true
        Binding.web.loadUrl("https://www.google.com")


//        by default google will open

        Binding.search.setOnClickListener {
            webSearch()
        }

        Binding.editText.setOnEditorActionListener {v, actionId, event ->
            if (actionId==EditorInfo.IME_ACTION_SEARCH) webSearch();true
        }

        Binding.floatingBtn.setOnClickListener {
            PIP()
        }

    }

    private fun webSearch() {
        var Search =Binding.editText.getText().toString()
        if (!Search.startsWith("https://") && !Search.startsWith("www.")) {
            val query: String = Search.split(" ").joinToString("+")
            Search = "https://www.google.com/search?q=$query"
        }
        if(Search.startsWith("www.")) Search="https://$Search"
        if (currentFocus!=null)
        {
            val inp=getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inp.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
            Binding.editText.clearFocus()
        }
        WebV(Search.toString())
    }

    override fun onBackPressed() {
        if(Binding.web.canGoBack())
            Binding.web.goBack()
        else
            super.onBackPressed()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun WebV(Search: String) {
        Binding.web.loadUrl(Search)
    }



    @RequiresApi(Build.VERSION_CODES.O)
    private fun PIP() {
            val Pip = PictureInPictureParams.Builder()
            val dimension = windowManager.defaultDisplay
            val points= Point()
            dimension.getSize(points)
            Pip.setAspectRatio(Rational(points.x, points.y))
            enterPictureInPictureMode(Pip.build())
        }

        @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
        override fun onPictureInPictureModeChanged(
                isInPictureInPictureMode: Boolean,
                newConfig: Configuration?
        ) {
            if(isInPictureInPictureMode)
            {
                Binding.floatingBtn.visibility= View.GONE
                Binding.search.visibility=View.GONE
                Binding.editText.visibility=View.GONE
//            requestedOrientation=ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE


            }
            else
            {Binding.floatingBtn.visibility= View.VISIBLE
                Binding.search.visibility=View.VISIBLE
                Binding.editText.visibility=View.VISIBLE
//            newConfig?.orientation=Configuration.ORIENTATION_PORTRAIT
            }
        }}




