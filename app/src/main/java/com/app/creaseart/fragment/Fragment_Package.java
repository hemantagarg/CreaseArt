package com.app.creaseart.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
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
import com.app.creaseart.activities.Dashboard;
import com.app.creaseart.activities.PayUMoneyActivity;
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
import com.app.creaseart.utils.AppConstant;
import com.app.creaseart.utils.AppUtils;
import com.sasidhar.smaps.payumoney.MakePaymentActivity;
import com.sasidhar.smaps.payumoney.PayUMoney_Constants;
import com.sasidhar.smaps.payumoney.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static android.app.Activity.RESULT_CANCELED;
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

    public static Fragment_Package fragmentPackage;
    private final String TAG = Fragment_Package.class.getSimpleName();
    private String mStrTransId = "";

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

                    makePayment();
                } else {
                    Toast.makeText(context, "Please select package", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private String getTxnId() {
        return ("" + System.currentTimeMillis());
    }

    private void makePayment() {

        mStrTransId = getTxnId();

        HashMap params = new HashMap<>();
        params.put(PayUMoney_Constants.KEY, AppConstant.PayuKey); // Get merchant key from PayU Money Account
        params.put(PayUMoney_Constants.TXN_ID, mStrTransId);
        params.put(PayUMoney_Constants.AMOUNT, "1");
        params.put(PayUMoney_Constants.PRODUCT_INFO, "Crease Art");
        params.put(PayUMoney_Constants.FIRST_NAME, AppUtils.getUserName(context));
        params.put(PayUMoney_Constants.EMAIL, AppUtils.getUseremail(context));
        params.put(PayUMoney_Constants.PHONE, AppUtils.getUserMobile(context));
        params.put(PayUMoney_Constants.SURL, "http://dev.stackmindz.com/creaseart/api/response.php?success=success");
        params.put(PayUMoney_Constants.FURL, "http://dev.stackmindz.com/creaseart/api/response.php?success=failure");

// User defined fields are optional (pass empty string)
        params.put(PayUMoney_Constants.UDF1, "");
        params.put(PayUMoney_Constants.UDF2, "");
        params.put(PayUMoney_Constants.UDF3, "");
        params.put(PayUMoney_Constants.UDF4, "");
        params.put(PayUMoney_Constants.UDF5, "");


// generate hash by passing params and salt
        String hash = Utils.generateHash(params, AppConstant.Payusalt); // Get Salt from PayU Money Account
        Log.e("hash", "**" + hash);
        params.put(PayUMoney_Constants.HASH, hash);


// SERVICE PROVIDER VALUE IS ALWAYS "payu_paisa".
        params.put(PayUMoney_Constants.SERVICE_PROVIDER, "payu_paisa");


        Intent intent = new Intent(context, MakePaymentActivity.class);
        intent.putExtra(PayUMoney_Constants.ENVIRONMENT, PayUMoney_Constants.ENV_PRODUCTION);
        intent.putExtra(PayUMoney_Constants.PARAMS, params);
        startActivityForResult(intent, PayUMoney_Constants.PAYMENT_REQUEST);
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
        if (requestCode == PayUMoney_Constants.PAYMENT_REQUEST) {
            if (data != null) {
                Log.e("payumoneyresponse", "**" + data.getData());
                String merchantData = data.getStringExtra("result"); // Data received from surl/furl
                String payuData = data.getStringExtra("payu_response"); // Response received from payu

                Log.e("payumoneyresponse", "**" + data.getData() + "**" + merchantData + "***" + payuData);
                switch (resultCode) {
                    case RESULT_OK:
                        Toast.makeText(context, "Payment Success.", Toast.LENGTH_SHORT).show();
                        break;
                    case RESULT_CANCELED:
                        Toast.makeText(context, "Payment Cancelled | Failed.", Toast.LENGTH_SHORT).show();
                        break;
                }
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
                        + transaction_id + "&total_value=" + finalPrice + "&promo_code=" + promocode + "&package_id=" + packages + "&payment_status=" + payment_status;
                new CommonAsyncTaskHashmap(1, context, this).getqueryJsonbject(url, null, Request.Method.GET);
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
                //    http://sfscoring.betasportzfever.com/getNotifications/155/efc0c68e-8bb5-11e7-8cf8-008cfa5afa52
             /*   HashMap<String, Object> hm = new HashMap<>();*/
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

