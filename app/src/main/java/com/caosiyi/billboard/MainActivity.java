package com.caosiyi.billboard;

import android.Manifest;
import android.graphics.Bitmap;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.webkit.ConsoleMessage;
import android.webkit.GeolocationPermissions;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.amap.api.location.AMapLocationClient;


public class MainActivity extends AppCompatActivity {

    WebView webView;
    AMapLocationClient mLocationClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLocationClient = new AMapLocationClient(getApplicationContext());

        webView = findViewById(R.id.web_view);
        mLocationClient.startAssistantLocation(webView);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.loadUrl("http://101.132.156.110:6324");
        webView.setWebViewClient(new WebViewClient() {

            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            // 处理javascript中的alert
            public boolean onJsAlert(WebView view, String url, String message,
                                     final JsResult result) {
                return true;
            }

            // 处理javascript中的confirm
            public boolean onJsConfirm(WebView view, String url,
                                       String message, final JsResult result) {
                return true;
            }

            public boolean onConsoleMessage(ConsoleMessage cm){
                Log.d("WEB_VIEW", "message : " + cm.message() + "---from line " + cm.lineNumber() + " of " + cm.sourceId());
                return true;
            }

            // 处理定位权限请求
            @Override
            public void onGeolocationPermissionsShowPrompt(String origin,
                                                           GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
                super.onGeolocationPermissionsShowPrompt(origin, callback);
            }
            @Override
            // 设置网页加载的进度条
            public void onProgressChanged(WebView view, int newProgress) {
                MainActivity.this.getWindow().setFeatureInt(
                        Window.FEATURE_PROGRESS, newProgress * 100);
                super.onProgressChanged(view, newProgress);
            }

            // 设置应用程序的标题title
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }
        });

        requestPermissions();
    }

    private void requestPermissions(){
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.stopAssistantLocation();
    }
}
