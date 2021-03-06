/*
 * Copyright (c) 2018 hglf
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.hotstu.jsbridge;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import io.github.hotstu.jsbridge.annotation.JavaInterface;
import io.github.hotstu.jsbridge.annotation.Param;
import io.github.hotstu.jsbridge.annotation.ParamCallback;
import io.github.hotstu.jsbridge.core.NIMJsBridge;
import io.github.hotstu.jsbridge.core.NIMJsBridgeBuilder;
import io.github.hotstu.jsbridge.interfaces.IJavaReplyToJsImageInfo;
import io.github.hotstu.jsbridge.util.LogUtil;
import io.github.hotstu.jsbridge.util.WebViewConfig;
import io.github.hotstu.jsbridge.demo.R;


/**
 * Created by hzliuxuanlin on 2016/10/21.
 */

public class JsBridgeActivity extends AppCompatActivity{

    /**
     * 本地资源页面
     */
    private static final String LOCAL_ASSET_HTML = "file:///android_asset/page.html";
    private NIMJsBridge jsBridge;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.debug = true;
        setContentView(R.layout.js_bridge_activity);
        initWebView();
        initData();
    }

    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.destroy();
        }
        super.onDestroy();
    }


    private void initWebView() {
        webView = findViewById(R.id.webView);
        WebSettings settings = webView.getSettings();
        WebViewConfig.setWebSettings(this, settings, this.getApplicationInfo().dataDir);
        WebViewConfig.removeJavascriptInterfaces(webView);
        WebViewConfig.setWebViewAllowDebug(false);
        WebViewConfig.setAcceptThirdPartyCookies(webView);
        webView.loadUrl(LOCAL_ASSET_HTML);
        webView.setOnLongClickListener(null);
    }

    private void initData() {
        JavaInterfaces javaInterfaces = new JavaInterfaces(this);
        jsBridge = new NIMJsBridgeBuilder()
                .addJavaInterfaceForJS(javaInterfaces)
                .setWebView(webView).create();
    }

    public void pickPicture(IJavaReplyToJsImageInfo cb) {

    }




    public static class JavaInterfaces {
        private JsBridgeActivity context;

        public JavaInterfaces(JsBridgeActivity context) {
            this.context = context;
        }

        @JavaInterface("notification")
        public void notification(@Param("msg") String msg) {
            // toast
            Toast.makeText(context.getApplicationContext(), "发送成功", Toast.LENGTH_LONG).show();
        }

        @JavaInterface("picture")
        public void picture(@ParamCallback IJavaReplyToJsImageInfo jsCallback) {
            context.pickPicture(jsCallback);
        }
    }
}
