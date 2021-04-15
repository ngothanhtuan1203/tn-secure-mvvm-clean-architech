package com.example.tnsecuremvvm.ui.screen.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tnsecuremvvm.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_detail.*

/**
 * Detail activity, show the content of news.
 */
@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

       val urls = intent.extras?.get("url").toString();
        setupWebView()


        repo_web_view.loadUrl(urls)
    }

    private fun setupWebView() {
        repo_web_view.setInitialScale(1)
        val webSettings = repo_web_view.settings
        webSettings.setAppCacheEnabled(false)
        webSettings.builtInZoomControls = true
        webSettings.displayZoomControls = false
        webSettings.javaScriptEnabled = true
        webSettings.useWideViewPort = true
        webSettings.domStorageEnabled = true

    }
}