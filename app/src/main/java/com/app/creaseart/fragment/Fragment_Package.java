package com.app.creaseart.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.app.creaseart.R;
import com.app.creaseart.activities.AppEnvironment;
import com.app.creaseart.activities.Dashboard;
import com.app.creaseart.adapter.AdapterPackages;
import com.app.creaseart.aynctask.CommonAsyncTaskHashmap;
import com.app.creaseart.iclasses.HeaderViewManager;
import com.app.creaseart.interfaces.ApiResponse;
import com.app.creaseart.interfaces.ConnectionDetector;
import com.app.creaseart.interfaces.GlobalConstants;
import com.app.creaseart.interfaces.HeaderViewClickListener;
import com.app.creaseart.interfaces.JsonApiHelper;
import com.app.creaseart.interfaces.OnCustomItemClicListener;
import com.app.creaseart.models.ModelPackage;
import com.app.creaseart.utils.AppUtils;
import com.payumoney.core.PayUmoneyConfig;
import com.payumoney.core.PayUmoneyConstants;
import com.payumoney.core.PayUmoneySdkInitializer;
import com.payumoney.core.entity.TransactionResponse;
import com.payumoney.sdkui.ui.utils.PayUmoneyFlowManager;
import com.payumoney.sdkui.ui.utils.ResultModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import static android.app.Activity.RESULT_OK;

/**
 * Created by admin on 06-01-2016.
 */
public class Fragment_Package extends BaseFragment implements ApiResponse, OnCustomItemClicListener {


    private RecyclerView list_request;
    private Bundle b;
    private Activity context;
    private AdapterPackages adapterPackages;
    private ModelPackage modelPackage;
    private ArrayList<ModelPackage> arrayList;
    private static final int REQUEST_CODE_PAYUMONEY = 11;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ConnectionDetector cd;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    private LinearLayoutManager layoutManager;
    private int skipCount = 0;
    private boolean loading = true;
    private String maxlistLength = "", finalPrice = "", promocode = "";
    boolean isPromoApplied = false;
    View view_about;
    private RelativeLayout rl_price;
    private TextView text_price, text_paynow, text_promocode;
    private int totalPrice = 0;
    private PayUmoneySdkInitializer.PaymentParam mPaymentParams;

    public static Fragment_Package fragmentPackage;
    private final String TAG = Fragment_Package.class.getSimpleName();
    private String mStrTransId = "";
    private String hashGenerated = "";

    public static Fragment_Package getInstance() {
        if (fragmentPackage == null)
            fragmentPackage = new Fragment_Package();
        return fragmentPackage;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this com.app.justclap.fragment

        view_about = inflater.inflate(R.layout.fragment_package, container, false);
        context = getActivity();
        arrayList = new ArrayList<>();
        b = getArguments();

        return view_about;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout1);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark);
        list_request = (RecyclerView) view.findViewById(R.id.list_request);
        rl_price = (RelativeLayout) view.findViewById(R.id.rl_price);
        text_paynow = (TextView) view.findViewById(R.id.text_paynow);
        text_price = (TextView) view.findViewById(R.id.text_price);
        text_promocode = (TextView) view.findViewById(R.id.text_promocode);

        layoutManager = new GridLayoutManager(context, 2);
        list_request.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>();
        setlistener();
        mStrTransId = getTxnId();
        manageHeaderView();
        getServicelistRefresh();
    }

    private void applyCode(String text) {
        try {
            if (AppUtils.isNetworkAvailable(context)) {
                // http://dev.stackmindz.com/creaseart/api/applycoupon.php?user_id=1&coupon_code=12345&total_value=100
                String url = JsonApiHelper.BASEURL + JsonApiHelper.APPLYCOUPON + "user_id=" + AppUtils.getUserId(context) + "&coupon_code=" + text.trim() + "&total_value=" + totalPrice;
                new CommonAsyncTaskHashmap(2, context, this).getqueryJsonbject(url, null, Request.Method.GET);

            } else {
                Toast.makeText(context, context.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Open dialog for the add member
     */
    private void openAddDialog() {
        try {
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

            // inflate the layout dialog_layout.xml and set it as contentView
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.promocode_dialog, null, false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setContentView(view);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            RelativeLayout cross_img_rel = (RelativeLayout) view.findViewById(R.id.cross_img_rel);
            final EditText edt_comment = (EditText) view.findViewById(R.id.edt_comment);
            Button btnSubmit = (Button) view.findViewById(R.id.btnSubmit);

            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (!edt_comment.getText().toString().equalsIgnoreCase("")) {
                        promocode = edt_comment.getText().toString();
                        applyCode(edt_comment.getText().toString());

                        dialog.dismiss();
                    } else {
                        edt_comment.setError("Please enter PromoCode");
                        edt_comment.requestFocus();
                    }

                }
            });

            cross_img_rel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            if (dialog != null && !dialog.isShowing()) {
                dialog.show();
            }
        } catch (Exception e) {
            Log.e(TAG, " Exception error : " + e);
        }
    }


    /*******************************************************************
     * Function name - manageHeaderView
     * Description - manage the initialization, visibility and click
     * listener of view fields on Header view
     *******************************************************************/
    private void manageHeaderView() {

        Dashboard.getInstance().manageHeaderVisibitlity(false);
        HeaderViewManager.getInstance().InitializeHeaderView(null, view_about, manageHeaderClick());
        HeaderViewManager.getInstance().setHeading(true, "Packages");
        HeaderViewManager.getInstance().setLeftSideHeaderView(true, R.drawable.left_arrow);
        HeaderViewManager.getInstance().setRightSideHeaderView(false, R.drawable.left_arrow);
        HeaderViewManager.getInstance().setLogoView(false);
        HeaderViewManager.getInstance().setProgressLoader(false, false);

    }

    /*****************************************************************************
     * Function name - manageHeaderClick
     * Description - manage the click on the left and right image view of header
     *****************************************************************************/
    private HeaderViewClickListener manageHeaderClick() {
        return new HeaderViewClickListener() {
            @Override
            public void onClickOfHeaderLeftView() {
                AppUtils.showLog(TAG, "onClickOfHeaderLeftView");
                context.onBackPressed();
            }

            @Override
            public void onClickOfHeaderRightView() {
                //   Toast.makeText(mActivity, "Coming Soon", Toast.LENGTH_SHORT).show();
            }
        };
    }

    private void setlistener() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getServicelistRefresh();
            }
        });

        text_promocode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openAddDialog();
            }
        });
        text_paynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (totalPrice > 0) {
//                    Intent intent = new Intent(context, PayUMoneyActivity.class);
//                    intent.putExtra("totalamount", "1");
//                    intent.putExtra("name", AppUtils.getUserName(context));
//                    intent.putExtra("address", "");
//                    intent.putExtra("emailid", AppUtils.getUseremail(context));
//                    intent.putExtra("mobileno", AppUtils.getUserMobile(context));
//                    intent.putExtra("orderid", AppUtils.getUserId(context));
//                    intent.putExtra("city", "");
//                    intent.putExtra("state", "");
//                    startActivityForResult(intent, REQUEST_CODE_PAYUMONEY);

                    getHash();
                } else {
                    Toast.makeText(context, "Please select package", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private String getTxnId() {
        return ("" + System.currentTimeMillis());
    }

    /**
     * This function prepares the data for payment and launches payumoney plug n play sdk
     */
    private void launchPayUMoneyFlow() {

        PayUmoneyConfig payUmoneyConfig = PayUmoneyConfig.getInstance();

        //Use this to set your custom text on result screen button
        payUmoneyConfig.setDoneButtonText("Done");

        //Use this to set your custom title for the activity
        payUmoneyConfig.setPayUmoneyActivityTitle("Crease Art");

        PayUmoneySdkInitializer.PaymentParam.Builder builder = new PayUmoneySdkInitializer.PaymentParam.Builder();

        double amount = 0;
        try {
            amount = Double.parseDouble("1");

        } catch (Exception e) {
            e.printStackTrace();
        }
        String txnId = mStrTransId;
        String phone = AppUtils.getUserMobile(context);
        String productName = "CreaseArt";
        String firstName = AppUtils.getUserName(context);
        String email = AppUtils.getUseremail(context);
        String udf1 = "";
        String udf2 = "";
        String udf3 = "";
        String udf4 = "";
        String udf5 = "";
        String udf6 = "";
        String udf7 = "";
        String udf8 = "";
        String udf9 = "";
        String udf10 = "";

        AppEnvironment appEnvironment = AppEnvironment.PRODUCTION;
        builder.setAmount(amount)
                .setTxnId(txnId)
                .setPhone(phone)
                .setProductName(productName)
                .setFirstName(firstName)
                .setEmail(email)
                .setsUrl(appEnvironment.surl())
                .setfUrl(appEnvironment.furl())
                .setUdf1(udf1)
                .setUdf2(udf2)
                .setUdf3(udf3)
                .setUdf4(udf4)
                .setUdf5(udf5)
                .setUdf6(udf6)
                .setUdf7(udf7)
                .setUdf8(udf8)
                .setUdf9(udf9)
                .setUdf10(udf10)
                .setIsDebug(appEnvironment.debug())
                .setKey(appEnvironment.merchant_Key())
                .setMerchantId(appEnvironment.merchant_ID());

        try {
            mPaymentParams = builder.build();
            //   generateHashFromServer(mPaymentParams);
            Log.e("mPaymentParams", "&*" + mPaymentParams.getParams());
            mPaymentParams.setMerchantHash(hashGenerated);
            PayUmoneyFlowManager.startPayUMoneyFlow(mPaymentParams, context, R.style.AppTheme_default, false);

        } catch (Exception e) {
            // some exception occurred
            e.printStackTrace();
        }
    }

    public void generateHashFromServer(PayUmoneySdkInitializer.PaymentParam paymentParam) {
        //nextButton.setEnabled(false); // lets not allow the user to click the button again and again.

        HashMap<String, String> params = paymentParam.getParams();

        // lets create the post params
        StringBuffer postParamsBuffer = new StringBuffer();
        postParamsBuffer.append(concatParams(PayUmoneyConstants.KEY, params.get(PayUmoneyConstants.KEY)));
        postParamsBuffer.append(concatParams(PayUmoneyConstants.AMOUNT, params.get(PayUmoneyConstants.AMOUNT)));
        postParamsBuffer.append(concatParams(PayUmoneyConstants.TXNID, params.get(PayUmoneyConstants.TXNID)));
        postParamsBuffer.append(concatParams(PayUmoneyConstants.EMAIL, params.get(PayUmoneyConstants.EMAIL)));
        postParamsBuffer.append(concatParams("productinfo", params.get(PayUmoneyConstants.PRODUCT_INFO)));
        postParamsBuffer.append(concatParams("firstname", params.get(PayUmoneyConstants.FIRSTNAME)));
        postParamsBuffer.append(concatParams(PayUmoneyConstants.UDF1, params.get(PayUmoneyConstants.UDF1)));
        postParamsBuffer.append(concatParams(PayUmoneyConstants.UDF2, params.get(PayUmoneyConstants.UDF2)));
        postParamsBuffer.append(concatParams(PayUmoneyConstants.UDF3, params.get(PayUmoneyConstants.UDF3)));
        postParamsBuffer.append(concatParams(PayUmoneyConstants.UDF4, params.get(PayUmoneyConstants.UDF4)));
        postParamsBuffer.append(concatParams(PayUmoneyConstants.UDF5, params.get(PayUmoneyConstants.UDF5)));

        String postParams = postParamsBuffer.charAt(postParamsBuffer.length() - 1) == '&' ? postParamsBuffer.substring(0, postParamsBuffer.length() - 1).toString() : postParamsBuffer.toString();
        Log.e("postParams", postParams);
        // lets make an api call
        GetHashesFromServerTask getHashesFromServerTask = new GetHashesFromServerTask();
        getHashesFromServerTask.execute(postParams);
    }


    protected String concatParams(String key, String value) {
        return key + "=" + value + "&";
    }

    /**
     * This AsyncTask generates hash from server.
     */
    private class GetHashesFromServerTask extends AsyncTask<String, String, String> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... postParams) {

            String merchantHash = "";
            try {
                //TODO Below url is just for testing purpose, merchant needs to replace this with their server side hash generation url
                URL url = new URL("https://payu.herokuapp.com/get_hash");

                String postParam = postParams[0];

                byte[] postParamsByte = postParam.getBytes("UTF-8");

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestProperty("Content-Length", String.valueOf(postParamsByte.length));
                conn.setDoOutput(true);
                conn.getOutputStream().write(postParamsByte);

                InputStream responseInputStream = conn.getInputStream();
                StringBuffer responseStringBuffer = new StringBuffer();
                byte[] byteContainer = new byte[1024];
                for (int i; (i = responseInputStream.read(byteContainer)) != -1; ) {
                    responseStringBuffer.append(new String(byteContainer, 0, i));
                }

                JSONObject response = new JSONObject(responseStringBuffer.toString());

                Iterator<String> payuHashIterator = response.keys();
                while (payuHashIterator.hasNext()) {
                    String key = payuHashIterator.next();
                    switch (key) {
                        /**
                         * This hash is mandatory and needs to be generated from merchant's server side
                         *
                         */
                        case "payment_hash":
                            merchantHash = response.getString(key);
                            break;
                        default:
                            break;
                    }
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return merchantHash;
        }

        @Override
        protected void onPostExecute(String merchantHash) {
            super.onPostExecute(merchantHash);

            progressDialog.dismiss();
            if (merchantHash.isEmpty() || merchantHash.equals("")) {
                Toast.makeText(context, "Could not generate hash", Toast.LENGTH_SHORT).show();
            } else {

                Log.e("merchantHash", "**" + merchantHash);
                mPaymentParams.setMerchantHash(merchantHash);

                PayUmoneyFlowManager.startPayUMoneyFlow(mPaymentParams, context, R.style.AppTheme_default, false);
            }

        }
    }

    @Override
    public void onItemClickListener(int position, int flag) {
        if (flag == 1) {
            Fragment_Bundle fragment_bundle = new Fragment_Bundle();
            Bundle bundle = new Bundle();
            bundle.putString("array", arrayList.get(position).getJsonArray());
            fragment_bundle.setArguments(bundle);
            Dashboard.getInstance().pushFragments(GlobalConstants.TAB_HOME_BAR, fragment_bundle, true);
        } else if (flag == 2) {
            if (arrayList.get(position).isSelected()) {
                arrayList.get(position).setSelected(false);
                addPackagePrice(arrayList.get(position).getPackagePrice(), false);
            } else {
                addPackagePrice(arrayList.get(position).getPackagePrice(), true);
                arrayList.get(position).setSelected(true);
            }
            adapterPackages.notifyDataSetChanged();

            if (rl_price.getVisibility() == View.GONE) {
                rl_price.setVisibility(View.VISIBLE);
            }
        }
    }


    private void addPackagePrice(String price, boolean add) {
        if (isPromoApplied) {
            text_promocode.setText("Have a Promocode ?");
            isPromoApplied = false;
        }
        if (add) {
            totalPrice = totalPrice + Integer.parseInt(price);
            text_price.setText(totalPrice + "");
            finalPrice = text_price.getText().toString();
        } else {
            totalPrice = totalPrice - Integer.parseInt(price);
            text_price.setText(totalPrice + "");
            finalPrice = text_price.getText().toString();
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PAYUMONEY) {
            if (data != null) {
                Log.e("payumoneyresponse", "**" + data);
                makePayment(data.getStringExtra("success"), data.getStringExtra("paymentId"));
            }

        }
        if (requestCode == PayUmoneyFlowManager.REQUEST_CODE_PAYMENT && resultCode == RESULT_OK && data !=
                null) {


            TransactionResponse transactionResponse = data.getParcelableExtra(PayUmoneyFlowManager
                    .INTENT_EXTRA_TRANSACTION_RESPONSE);

            ResultModel resultModel = data.getParcelableExtra(PayUmoneyFlowManager.ARG_RESULT);

            // Check which object is non-null
            if (transactionResponse != null && transactionResponse.getPayuResponse() != null) {
                if (transactionResponse.getTransactionStatus().equals(TransactionResponse.TransactionStatus.SUCCESSFUL)) {

                    makePayment("success", "");
                    //Success Transaction
                } else {
                    makePayment("failed", "");
                    //Failure Transaction
                }

                // Response from Payumoney
                String payuResponse = transactionResponse.getPayuResponse();

                // Response from SURl and FURL
                String merchantResponse = transactionResponse.getTransactionDetails();
                Log.e("payumoneyresponse", "**" + data.getData() + merchantResponse);


            } else if (resultModel != null && resultModel.getError() != null) {
                Log.e(TAG, "Error response : " + resultModel.getError().getTransactionResponse());
            } else {
                Log.e(TAG, "Both objects are null!");
            }
        }
    }

    private void makePayment(String payment_status, String transaction_id) {
        try {
            if (AppUtils.isNetworkAvailable(context)) {
                String packages = "";
                for (int i = 0; i < arrayList.size(); i++) {
                    if (arrayList.get(i).isSelected()) {
                        if (packages.equalsIgnoreCase("")) {
                            packages = arrayList.get(i).getPackageId();
                        } else {
                            packages = packages + "," + arrayList.get(i).getPackageId();
                        }
                    }

                }

                //http://dev.stackmindz.com/creaseart/api/payment.php?user_id=1&transaction_id=AG565JH078
                // &total_value=100&promo_code=TEST&package_id=2&payment_status=
                String url = JsonApiHelper.BASEURL + JsonApiHelper.PAYMENT + "user_id=" + AppUtils.getUserId(context) + "&transaction_id="
                        + mStrTransId + "&total_value=" + finalPrice + "&promo_code=" + promocode + "&package_id=" + packages + "&payment_status=" + payment_status;
                new CommonAsyncTaskHashmap(11, context, this).getqueryJsonbject(url, null, Request.Method.GET);
            } else {
                Toast.makeText(context, context.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void getHash() {
        try {
            if (AppUtils.isNetworkAvailable(context)) {
                //   http://dev.stackmindz.com/creaseart/api/getHash.php?txnid=1&amount=10&productinfo=Test
                // &firstname=test&email=test@gmail.com&udf1=&udf2=&udf3=&udf4=&udf5=
                String url = JsonApiHelper.BASEURL + JsonApiHelper.GET_HASH + "txnid=" + mStrTransId + "&amount=" + finalPrice +
                        "&productinfo=CreaseArt" + "&firstname=" + AppUtils.getUserName(context) + "&email=" + AppUtils.getUseremail(context) +
                        "&udf1=&udf2=&udf3=&udf4=&udf5=";
                url=url.replace(" ","%20");
                new CommonAsyncTaskHashmap(21, context, this).getqueryJsonbject(url, null, Request.Method.GET);

            } else {
                Toast.makeText(context, context.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void getServicelistRefresh() {
        Dashboard.getInstance().setProgressLoader(true);
        try {
            skipCount = 0;
            if (AppUtils.isNetworkAvailable(context)) {

                String url = JsonApiHelper.BASEURL + JsonApiHelper.PACKAGES;
                new CommonAsyncTaskHashmap(1, context, this).getqueryJsonbject(url, null, Request.Method.GET);

            } else {
                Toast.makeText(context, context.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showPaymentPopup() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                context);

        alertDialog.setMessage("Payment is Successful");

        alertDialog.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Dashboard.getInstance().pushFragments(GlobalConstants.TAB_HOME_BAR, new Fragment_Home(), true);
                    }

                });


        alertDialog.show();


    }


    @Override
    public void onPostSuccess(int position, JSONObject jObject) {
        try {
            if (position == 1) {
                Dashboard.getInstance().setProgressLoader(false);
                JSONObject commandResult = jObject.getJSONObject("commandResult");

                if (commandResult.getString("success").equalsIgnoreCase("1")) {

                    JSONObject data = commandResult.getJSONObject("data");
                    JSONArray array = data.getJSONArray("packages");
                    arrayList.clear();

                    for (int i = 0; i < array.length(); i++) {

                        JSONObject jo = array.getJSONObject(i);
                        ModelPackage serviceDetail = new ModelPackage();

                        serviceDetail.setJsonArray(jo.toString());
                        serviceDetail.setPackageId(jo.getString("packageId"));
                        serviceDetail.setPackageName(jo.getString("packageName"));
                        serviceDetail.setSelected(false);
                        serviceDetail.setPackagePrice(jo.getString("packagePrice"));
                        serviceDetail.setDiscountPrice(jo.getString("discountPrice"));
                        serviceDetail.setIsDiscount(jo.getString("isDiscount"));
                        serviceDetail.setDiscount(jo.getString("discount"));
                        serviceDetail.setRowType(1);

                        arrayList.add(serviceDetail);
                    }
                    adapterPackages = new AdapterPackages(getActivity(), this, arrayList);
                    list_request.setAdapter(adapterPackages);

                    if (mSwipeRefreshLayout != null) {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }

                } else {
                    if (mSwipeRefreshLayout != null) {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }

            } else if (position == 11) {

                JSONObject commandResult = jObject
                        .getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {

                    showPaymentPopup();
                } else {
                    Toast.makeText(context,
                            commandResult.getString("message"),
                            Toast.LENGTH_LONG).show();

                }

            } else if (position == 2) {

                JSONObject commandResult = jObject.getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {

                    JSONObject data = commandResult.getJSONObject("data");
                    finalPrice = data.getString("TotalValue");
                    isPromoApplied = true;
                    text_promocode.setText("Promo code Applied Sucessfully");
                    String total = totalPrice + "";
                    SpannableString spannable = new SpannableString(total + finalPrice);
                    spannable.setSpan(new StrikethroughSpan(), 0, total.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                    text_price.setText(spannable);

                } else {
                    Toast.makeText(context, commandResult.getString("message"), Toast.LENGTH_SHORT).show();
                }

            } else if (position == 4) {
                JSONObject commandResult = jObject.getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {

                    JSONObject data = commandResult.getJSONObject("data");
                    JSONArray array = data.getJSONArray("packages");

                    arrayList.remove(arrayList.size() - 1);
                    for (int i = 0; i < array.length(); i++) {

                        JSONObject jo = array.getJSONObject(i);
                        ModelPackage serviceDetail = new ModelPackage();

                        serviceDetail.setPackageId(jo.getString("packageId"));
                        serviceDetail.setPackageName(jo.getString("packageName"));
                        serviceDetail.setPackagePrice(jo.getString("packagePrice"));

                        modelPackage.setRowType(1);

                        arrayList.add(modelPackage);
                    }
                    adapterPackages.notifyDataSetChanged();
                    loading = true;
                    if (data.length() == 0) {
                        skipCount = skipCount - 10;
                        //  return;
                    }
                } else {
                    adapterPackages.notifyDataSetChanged();
                    skipCount = skipCount - 10;
                    loading = true;
                }
            } else if (position == 21) {
                JSONObject commandResult = jObject.getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {

                    hashGenerated = commandResult.getString("data");
                    launchPayUMoneyFlow();

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPostFail(int method, String response) {
        if (context != null && isAdded())
            Toast.makeText(getActivity(), getResources().getString(R.string.problem_server), Toast.LENGTH_SHORT).show();
    }
}

