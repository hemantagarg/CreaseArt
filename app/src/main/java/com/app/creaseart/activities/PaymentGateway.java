package com.app.creaseart.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.app.creaseart.R;
import com.app.creaseart.utils.AppConstant;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PaymentGateway extends Activity {

    //private Button button;

    private static final String TAG = "PaymentGateway";
    WebView webviewPayment;
    String orderid;
    String amount, fullname, address, emailid, mobileno, city, state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_gateway);

        Intent intent = getIntent();
        amount = intent.getStringExtra("totalamount");
        fullname = intent.getStringExtra("name");
        address = intent.getStringExtra("address");
        emailid = intent.getStringExtra("emailid");
        mobileno = intent.getStringExtra("mobileno");
        orderid = intent.getStringExtra("orderid");
        city = intent.getStringExtra("city");
        state = intent.getStringExtra("state");

        webviewPayment = (WebView) findViewById(R.id.webView1);
        webviewPayment.getSettings().setJavaScriptEnabled(true);
        webviewPayment.getSettings().setDomStorageEnabled(true);
        webviewPayment.getSettings().setLoadWithOverviewMode(true);
        webviewPayment.getSettings().setUseWideViewPort(true);
        webviewPayment.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webviewPayment.getSettings().setSupportMultipleWindows(true);
        webviewPayment.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webviewPayment.addJavascriptInterface(new PayUJavaScriptInterface(), "PayUMoney");

        StringBuilder url_s = new StringBuilder();
        url_s.append("https://secure.payu.in/_payment");

        Log.e(TAG, "call url " + url_s);

        //	webviewPayment.postUrl(url_s.toString(),EncodingUtils.getBytes(getPostString(), "utf-8"));

        webviewPayment.postUrl(url_s.toString(), getPostString().getBytes(Charset.forName("UTF-8")));

        webviewPayment.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @SuppressWarnings("unused")
            public void onReceivedSslError(WebView view, SslErrorHandler handler) {
                Log.e("Error", "Exception caught!");
                handler.cancel();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

        });
    }

    private final class PayUJavaScriptInterface {
        PayUJavaScriptInterface() {
        }

        @JavascriptInterface
        public void success(long id, final String paymentId) {
            runOnUiThread(new Runnable() {
                public void run() {

                    Log.e("paymentId", "*8" + paymentId);
                    Intent returnFromGalleryIntent = new Intent();
                    returnFromGalleryIntent.putExtra("success", "true");
                    returnFromGalleryIntent.putExtra("paymentId", paymentId);
                    setResult(RESULT_OK, returnFromGalleryIntent);
                    finish();
                }
            });
        }

        @JavascriptInterface
        public void failure(long id, final String paymentId) {
            runOnUiThread(new Runnable() {
                public void run() {
                    Intent returnFromGalleryIntent = new Intent();
                    returnFromGalleryIntent.putExtra("success", "false");
                    returnFromGalleryIntent.putExtra("paymentId", paymentId);
                    setResult(RESULT_OK, returnFromGalleryIntent);
                    finish();

                }
            });
        }

    }


    private String getPostString() {
        String key = AppConstant.PayuKey;
        String salt = AppConstant.Payusalt;
        String txnid = orderid;
        String amount1 = amount;
        String firstname = fullname;
        String email = emailid;
        String productInfo = "Product1";

        StringBuilder post = new StringBuilder();
        post.append("key=");
        post.append(key);
        post.append("&");
        post.append("txnid=");
        post.append(txnid);
        post.append("&");
        post.append("amount=");
        post.append(amount1);
        post.append("&");
        post.append("productinfo=");
        post.append(productInfo);
        post.append("&");
        post.append("firstname=");
        post.append(firstname);
        post.append("&");
        post.append("email=");
        post.append(email);
        post.append("&");
        post.append("phone=");
        post.append(mobileno);
        post.append("&");
        post.append("surl=");
        post.append("https://creaseart.org/response.php?status=success");
        post.append("&");
        post.append("furl=");
        post.append("https://creaseart.org/response.php?status=failed");
        post.append("&");

        StringBuilder checkSumStr = new StringBuilder();
        /* =sha512(key|txnid|amount|productinfo|firstname|email|udf1|udf2|udf3|udf4|udf5||||||salt) */
        MessageDigest digest = null;
        String hash;
        try {
            digest = MessageDigest.getInstance("SHA-512");// MessageDigest.getInstance("SHA-256");

            checkSumStr.append(key);
            checkSumStr.append("|");
            checkSumStr.append(txnid);
            checkSumStr.append("|");
            checkSumStr.append(amount);
            checkSumStr.append("|");
            checkSumStr.append(productInfo);
            checkSumStr.append("|");
            checkSumStr.append(firstname);
            checkSumStr.append("|");
            checkSumStr.append(email);
            checkSumStr.append("|||||||||||");
            checkSumStr.append(salt);

            digest.update(checkSumStr.toString().getBytes());

            hash = bytesToHexString(digest.digest());
            post.append("hash=");
            post.append(hash);
            post.append("&");
            Log.i(TAG, "SHA result is " + hash);
        } catch (NoSuchAlgorithmException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        post.append("service_provider=");
        post.append("payu_paisa");
        return post.toString();
    }

    private static String bytesToHexString(byte[] bytes) {
        // http://stackoverflow.com/questions/332079
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

}
