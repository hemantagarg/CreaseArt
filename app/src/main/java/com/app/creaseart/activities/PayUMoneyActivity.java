package com.app.creaseart.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.app.creaseart.utils.AppConstant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@SuppressLint("SetJavaScriptEnabled")
public class PayUMoneyActivity extends Activity {

    WebView webView;
  /*  String merchant_key = "JBZaLc";
    String salt = "GQs7yium";
  */  String merchant_key = AppConstant.PayuKey;
    String salt = AppConstant.Payusalt;
    String action1 = "";
    String base_url = "https://secure.payu.in/_payment";
    // int error = 0;
    // String hashString = "";
    // Map<String, String> params;
    String txnid = "TXN8367286482920";
    String amount = "1";
    String productInfo = "";
    String firstName = "Rajesh";
    String emailId = "";
    private String SUCCESS_URL = "https://creaseart.org/response.php?status=success";
    private String FAILED_URL = "https://creaseart.org/response.php?status=failed";
    private String phone = "35465756768";
    private String serviceProvider = "payu_paisa";
    private String hash = "";
    Handler mHandler = new Handler();
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_PROGRESS);
        webView = new WebView(this);
        activity = PayUMoneyActivity.this;
        setContentView(webView);

        JSONObject productInfoObj = new JSONObject();
        JSONArray productPartsArr = new JSONArray();
        JSONObject productPartsObj1 = new JSONObject();
        JSONObject paymentIdenfierParent = new JSONObject();
        JSONArray paymentIdentifiersArr = new JSONArray();
        JSONObject paymentPartsObj1 = new JSONObject();
        JSONObject paymentPartsObj2 = new JSONObject();
        try {
            // Payment Parts
            productPartsObj1.put("name", "abc");
            productPartsObj1.put("description", "abcd");
            productPartsObj1.put("value", "1000");
            productPartsObj1.put("isRequired", "true");
            productPartsObj1.put("settlementEvent", "EmailConfirmation");
            productPartsArr.put(productPartsObj1);
            productInfoObj.put("paymentParts", productPartsArr);

            // Payment Identifiers
            paymentPartsObj1.put("field", "CompletionDate");
            paymentPartsObj1.put("value", "31/10/2012");
            paymentIdentifiersArr.put(paymentPartsObj1);

            paymentPartsObj2.put("field", "TxnId");
            paymentPartsObj2.put("value", txnid);
            paymentIdentifiersArr.put(paymentPartsObj2);

            paymentIdenfierParent.put("paymentIdentifiers",
                    paymentIdentifiersArr);
            productInfoObj.put("", paymentIdenfierParent);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        productInfo = productInfoObj.toString();

        Log.e("TAG", productInfoObj.toString());

        Random rand = new Random();
        String rndm = Integer.toString(rand.nextInt())
                + (System.currentTimeMillis() / 1000L);
        txnid = hashCal("SHA-256", rndm).substring(0, 20);

        hash = hashCal("SHA-512", merchant_key + "|" + txnid + "|" + amount
                + "|" + productInfo + "|" + firstName + "|" + emailId
                + "|||||||||||" + salt);

        action1 = base_url.concat("/_payment");

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                // TODO Auto-generated method stub
                Toast.makeText(activity, "Oh no! " + description,
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onReceivedSslError(WebView view,
                                           SslErrorHandler handler, SslError error) {
                // TODO Auto-generated method stub
                Toast.makeText(activity, "SslError! " + error,
                        Toast.LENGTH_SHORT).show();
                handler.proceed();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Toast.makeText(activity, "Page Started! " + url,
                        Toast.LENGTH_SHORT).show();
                if (url.equals(SUCCESS_URL)) {
                    Toast.makeText(activity, "Success! " + url,
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(activity, "Failure! " + url,
                            Toast.LENGTH_SHORT).show();
                }
                return super.shouldOverrideUrlLoading(view, url);
            }
            //
            // @Override
            // public void onPageFinished(WebView view, String url) {
            // super.onPageFinished(view, url);
            //
            // Toast.makeText(PayMentGateWay.this, "" + url,
            // Toast.LENGTH_SHORT).show();
            // }
        });

        webView.setVisibility(View.VISIBLE);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        webView.getSettings().setDomStorageEnabled(true);
        webView.clearHistory();
        webView.clearCache(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setUseWideViewPort(false);
        webView.getSettings().setLoadWithOverviewMode(false);

        webView.addJavascriptInterface(new PayUJavaScriptInterface(activity),
                "PayUMoney");
        Map<String, String> mapParams = new HashMap<String, String>();
        mapParams.put("key", merchant_key);
        mapParams.put("hash", hash);
        mapParams.put("txnid", txnid);
        mapParams.put("service_provider", "payu_paisa");
        mapParams.put("amount", amount);
        mapParams.put("firstname", firstName);
        mapParams.put("email", emailId);
        mapParams.put("phone", phone);

        mapParams.put("productinfo", productInfo);
        mapParams.put("surl", SUCCESS_URL);
        mapParams.put("furl", FAILED_URL);
        mapParams.put("lastname", "Vijayakumar");

        mapParams.put("address1", "");
        mapParams.put("address2", "");
        mapParams.put("city", "");
        mapParams.put("state", "");

        mapParams.put("country", "");
        mapParams.put("zipcode", "");
        mapParams.put("udf1", "");
        mapParams.put("udf2", "");

        mapParams.put("udf3", "");
        mapParams.put("udf4", "");
        mapParams.put("udf5", "");
        // mapParams.put("pg", (empty(PayMentGateWay.this.params.get("pg"))) ?
        // ""
        // : PayMentGateWay.this.params.get("pg"));
        webview_ClientPost(webView, action1, mapParams.entrySet());

    }

    public class PayUJavaScriptInterface {
        Context mContext;

        /**
         * Instantiate the interface and set the context
         */
        PayUJavaScriptInterface(Context c) {
            mContext = c;
        }

        public void success(long id, final String paymentId) {

            mHandler.post(new Runnable() {

                public void run() {
                    mHandler = null;
                    Intent returnFromGalleryIntent = new Intent();
                    returnFromGalleryIntent.putExtra("success", "true");
                    returnFromGalleryIntent.putExtra("paymentId", paymentId);
                    setResult(RESULT_OK, returnFromGalleryIntent);
                    Toast.makeText(PayUMoneyActivity.this, "Success",
                            Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void webview_ClientPost(WebView webView, String url,
                                   Collection<Map.Entry<String, String>> postData) {
        StringBuilder sb = new StringBuilder();

        sb.append("<html><head></head>");
        sb.append("<body onload='form1.submit()'>");
        sb.append(String.format("<form id='form1' action='%s' method='%s'>",
                url, "post"));
        for (Map.Entry<String, String> item : postData) {
            sb.append(String.format(
                    "<input name='%s' type='hidden' value='%s' />",
                    item.getKey(), item.getValue()));
        }
        sb.append("</form></body></html>");
        Log.d("", "webview_ClientPost called");
        webView.loadData(sb.toString(), "text/html", "utf-8");
    }

    public boolean empty(String s) {
        if (s == null || s.trim().equals(""))
            return true;
        else
            return false;
    }

    public String hashCal(String type, String str) {
        byte[] hashseq = str.getBytes();
        StringBuffer hexString = new StringBuffer();
        try {
            MessageDigest algorithm = MessageDigest.getInstance(type);
            algorithm.reset();
            algorithm.update(hashseq);
            byte messageDigest[] = algorithm.digest();

            for (int i = 0; i < messageDigest.length; i++) {
                String hex = Integer.toHexString(0xFF & messageDigest[i]);
                if (hex.length() == 1)
                    hexString.append("0");
                hexString.append(hex);
            }
        } catch (NoSuchAlgorithmException nsae) {
        }
        return hexString.toString();

    }
}