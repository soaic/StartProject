package com.soaic.libcommon.weight;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.webkit.*;
import android.widget.EditText;
import com.soaic.libcommon.utils.InstallUtil;

import java.lang.reflect.Field;

/**
 * WebView不能与CoordinatorLayout和NestedScrollView使用否则会出现js失效
 */
public class SWebView extends WebView {

    private OnWebViewListener onWebViewListener;

    public SWebView(Context context) {
        this(context, null);
    }

    public SWebView(Context context, AttributeSet attrs) {
        this(context, attrs, Resources.getSystem().getIdentifier("webViewStyle","attr","android"));
    }

    public SWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (!isInEditMode()) {
            initWebView();
        }
    }

    @SuppressLint({"AddJavascriptInterface", "SetJavaScriptEnabled"})
    public void initWebView() {
        WebSettings webSettings = getSettings();
        // 支持JS
        webSettings.setJavaScriptEnabled(true);
        // 调整到适合webView的大小
        webSettings.setUseWideViewPort(true);
        // 缩放至屏幕的大小
        webSettings.setLoadWithOverviewMode(true);
        // 设置不出现缩放工具
        webSettings.setBuiltInZoomControls(false);
        // 设置可以支持缩放
        webSettings.setSupportZoom(true);
        // 关闭缓存
        webSettings.setAppCacheEnabled(false);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        // 不显示webView缩放按钮
        webSettings.setDisplayZoomControls(false);
        // 支持自动加载图片
        webSettings.setLoadsImagesAutomatically(true);
        // 设置允许JS弹窗
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        // 取消滚动条
        setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        setHorizontalScrollBarEnabled(false);
        setVerticalScrollBarEnabled(true);
        setInitialScale(100);
        // 触摸焦点起作用
        requestFocus();
        // 设置监听进度
        setWebChromeClient(new SWebChromeClient());
        // 设置不调用系统浏览器
        setWebViewClient(new SWebViewClient());
        // TestJavaScriptInterface类对象映射到js的test对象 js调用 test.callJS('title')
        //addJavascriptInterface(new TestJavaScriptInterface(), "test");
    }

    /**
     * 原生调用js代码
     */
    public void callJS() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            evaluateJavascript("javascript:callJS()", new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String value) {
                    //js 返回的值
                }
            });
        } else {
            loadUrl("javascript:callJS()");
        }
    }

    /**
     * js调用原生
     */
    class TestJavaScriptInterface {
        @JavascriptInterface
        public void callJS(String title) {
            // js传递过来的值
        }
    }

    /**
     * 监听网页进度和接收标题内容通知
     */
    class SWebChromeClient extends WebChromeClient {

        @Override
        public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
            new AlertDialog.Builder(view.getContext())
                    .setTitle(getTitle())
                    .setMessage(message)
                    .setNegativeButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            result.cancel();
                        }
                    }).setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    result.confirm();
                }
            }).create().show();
            return true;
        }

        @Override
        public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, final JsPromptResult result) {
            final EditText et = new EditText(view.getContext());
            et.setSingleLine();
            et.setText(defaultValue);
            new AlertDialog.Builder(view.getContext())
                    .setTitle(getTitle())
                    .setView(et)
                    .setMessage(message)
                    .setNegativeButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            result.cancel();
                        }
                    }).setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    result.confirm(et.getText().toString());
                }
            }).create().show();
            return true;
        }

        public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
            new AlertDialog.Builder(view.getContext())
                .setTitle(getTitle())
                .setMessage(message)
                .setNegativeButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.cancel();
                    }
                }).setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.confirm();
                    }
                }).create().show();
            result.confirm();// 因为没有绑定事件，需要强行confirm,否则页面会变黑显示不了内容。
            return true;
        }

        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if (onWebViewListener != null) {
                onWebViewListener.onProgressChanged(newProgress);
            }
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            if (onWebViewListener != null) {
                onWebViewListener.onTitleChanged(title);
            }
        }
    }

    /**
     * 设置不调用系统浏览器，只在当前页面跳转
     */
    class SWebViewClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (!InstallUtil.handleWebUrl(getContext(), url)) {
                view.loadUrl(url);
            }
            if (onWebViewListener != null) {
                onWebViewListener.onUrlChanged(url);
            }
            return true;
        }

        public void onReceivedSslError(WebView view, SslErrorHandler handler, android.net.http.SslError error) {
            handler.proceed();
        }

        public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
            return super.shouldOverrideKeyEvent(view, event);
        }

        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
        }

        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }
    }

    @Override
    protected void onScrollChanged(int left, int top, int oldLeft, int oldTop) {
        super.onScrollChanged(left, top, oldLeft, oldTop);
        if (onWebViewListener != null) {
            onWebViewListener.onScrollChanged(left, top, oldLeft, oldTop);
        }
    }

    public void setOnWebViewListener(OnWebViewListener listener) {
        this.onWebViewListener = listener;
    }

    public interface OnWebViewListener {
        void onScrollChanged(int left, int top, int oldLeft, int oldTop);
        void onProgressChanged(int process);
        void onTitleChanged(String title);
        void onUrlChanged(String url);
    }

    /**
     * 彻底关闭WebView 防止WebView 造成OOM
     */
    private void setConfigCallback() {
        try {
            Field field = WebView.class.getDeclaredField("mWebViewCore");
            field = field.getType().getDeclaredField("mBrowserFrame");
            field = field.getType().getDeclaredField("sConfigCallback");
            field.setAccessible(true);
            Object configCallback = field.get(null);
            if (null == configCallback) {
                return;
            }
            field = field.getType().getDeclaredField("mWindowManager");
            field.setAccessible(true);
            field.set(configCallback, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 销毁，释放资源
     */
    public void onDestroy() {
        setConfigCallback();
        removeAllViews();
        destroy();
    }
}
