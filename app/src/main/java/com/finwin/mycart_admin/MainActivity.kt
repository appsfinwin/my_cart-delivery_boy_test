package com.finwin.mycart_admin

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.ViewTreeObserver.OnScrollChangedListener
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import cn.pedant.SweetAlert.SweetAlertDialog
import com.finwin.mycart_admin.services.Services
import com.finwin.mycart_admin.utils.Constants
import im.delight.android.webview.AdvancedWebView


class MainActivity : AppCompatActivity(), AdvancedWebView.Listener {
    lateinit var webView: WebView
    lateinit var layoutNoInternet: ConstraintLayout
    lateinit var layoutNoErrorConnection: ConstraintLayout
    private var mySwipeRefreshLayout: SwipeRefreshLayout? = null
    private var mOnScrollChangedListener: OnScrollChangedListener? = null
    var loading: SweetAlertDialog? = null
    var sharedPreferences: SharedPreferences? = null
    var edit: SharedPreferences.Editor? = null

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE)
        edit= sharedPreferences?.edit()

        mySwipeRefreshLayout = findViewById<SwipeRefreshLayout>(R.id.swipeContainer)
        webView = findViewById(R.id.webview)
        layoutNoInternet = findViewById(R.id.layout_no_internet)
        layoutNoErrorConnection = findViewById(R.id.layout_error_connection)
        val webSettings: WebSettings = webView.getSettings()
        webSettings.javaScriptEnabled = true

        webSettings.javaScriptEnabled = true // Done above

        webSettings.domStorageEnabled = true // Try

        webSettings.setSupportZoom(false)
        webSettings.allowFileAccess = true
        webSettings.allowContentAccess = true

        if (isNetworkOnline()) {
            initLoading(this)
            layoutNoInternet.visibility = View.GONE
            layoutNoErrorConnection.visibility = View.GONE
            webView.visibility = View.VISIBLE
        } else {
            layoutNoInternet.visibility = View.VISIBLE
            webView.visibility = View.GONE
            //mySwipeRefreshLayout?.visibility= View.GONE
        }

        //cart
        //webView.loadUrl("http://192.168.0.221:149/index.aspx")
        //webView.loadUrl("http://192.168.0.221:224/") //bliss
        //webView.loadUrl("http://pickcartshopy.com/")
        var username=sharedPreferences?.getString(Constants.USERNAME, "");
        var password=sharedPreferences?.getString(Constants.PASSWORD, "");
        //webView.loadUrl("http://testcartmob.digicob.in/login?UserName=$username&Password=$password")
        //webView.loadUrl("http://pickcartshopy.com/login?UserName=$username&Password=$password")
        //webView.loadUrl("http://supermarket.digicob.in/login?UserName=$username&Password=$password")
        //
        //webView.loadUrl("http://softadmin.pickcartshopy.com/login?UserName=$username&Password=$password")
        webView.loadUrl("http://www.organicskerala.com/login?UserName=$username&Password=$password")

        webView.webViewClient = WebViewClient()

        webView.settings.loadWithOverviewMode = true
        webView.settings.useWideViewPort = true


        webView.settings.javaScriptEnabled = true
        webView.settings.javaScriptCanOpenWindowsAutomatically = true;



        webView.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            (scrollY == 0).also { mySwipeRefreshLayout?.isEnabled = it }
        }
        mySwipeRefreshLayout?.setOnRefreshListener {
            if (isNetworkOnline()) {
                layoutNoInternet.visibility = View.GONE
                layoutNoErrorConnection.visibility = View.GONE
                webView.visibility = View.VISIBLE
                webView?.reload();
                initLoading(this)
            } else {
                mySwipeRefreshLayout?.isRefreshing = false
                layoutNoInternet.visibility = View.VISIBLE
                layoutNoErrorConnection.visibility = View.GONE
                webView.visibility = View.GONE
            }
        }
        webView.setWebViewClient(object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                //initLoading(this@MainActivity)
                if (url.startsWith("tel:")) {
                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse(url))
                    startActivity(intent)
                    return true
                }
                return false
            }

            override fun onPageFinished(view: WebView?, url: String?) {

                mySwipeRefreshLayout?.isRefreshing = false
                cancelLoading()
            }

            override fun onReceivedError(
                view: WebView?,
                errorCod: Int,
                description: String,
                failingUrl: String?
            ) {
                layoutNoInternet.visibility = View.GONE
                layoutNoErrorConnection.visibility = View.VISIBLE
                webView.visibility = View.VISIBLE
            }
        })


    }

    override fun onStop() {
        super.onStop()
        mySwipeRefreshLayout?.getViewTreeObserver()?.removeOnScrollChangedListener(
            mOnScrollChangedListener
        );
    }

    private class MyWebViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            if ("www.example.com" == Uri.parse(url).host) {
                // This is my website, so do not override; let my WebView load the page
                return false
            }
            return if (url.startsWith("tel:")) {
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse(url))
                //startActivity(intent)
                true
            } else {
                // Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                //startActivity(intent)
                true
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (event.action == KeyEvent.ACTION_DOWN) {
            when (keyCode) {
                KeyEvent.KEYCODE_BACK -> {
                    if (webView.canGoBack()) {
                        webView.goBack()
                    } else {
                        logoutDialog()
                    }
                    return true
                }
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onPageStarted(url: String?, favicon: Bitmap?) {
        TODO("Not yet implemented")
    }

    override fun onPageFinished(url: String?) {
        TODO("Not yet implemented")
    }

    override fun onPageError(errorCode: Int, description: String?, failingUrl: String?) {
        TODO("Not yet implemented")
    }

    override fun onDownloadRequested(
        url: String?,
        suggestedFilename: String?,
        mimeType: String?,
        contentLength: Long,
        contentDisposition: String?,
        userAgent: String?
    ) {
        TODO("Not yet implemented")
    }

    override fun onExternalPageRequest(url: String?) {
        TODO("Not yet implemented")
    }


    fun initLoading(context: Context?) {
        loading = Services.showProgressDialog(context)
    }

    fun cancelLoading() {
        if (loading != null) {
            loading!!.cancel()
            loading = null
        }
    }

    fun isNetworkOnline(): Boolean {
        var status = false
        try {
            val cm = (getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager)
            var netInfo = cm.getNetworkInfo(0)
            if (netInfo != null && netInfo.state == NetworkInfo.State.CONNECTED) {
                status = true
            } else {
                netInfo = cm.getNetworkInfo(1)
                if (netInfo != null && netInfo.state == NetworkInfo.State.CONNECTED) status = true
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
        return status
    }

    private fun logoutDialog() {


            AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Logout?")
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes",
                    DialogInterface.OnClickListener { dialog, which ->
//
//                        val intent = Intent(applicationContext, LoginActivity::class.java)
//                        intent.addFlags(
//                            Intent.FLAG_ACTIVITY_CLEAR_TOP
//                            //or Intent.FLAG_ACTIVITY_NEW_TASK
//
//                        )
//                        edit?.putBoolean(Constants.IS_LOGIN, false)
//                        edit?.commit()
//                        startActivity(intent)
                        finish()
                        finishAffinity()
                        val a = Intent(Intent.ACTION_MAIN)
                        a.addCategory(Intent.CATEGORY_HOME)
                        a.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    })
                .setNegativeButton("No", null)
                .show()


        }

}