package com.app.creaseart.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.creaseart.R;
import com.app.creaseart.activities.Dashboard;
import com.app.creaseart.aynctask.CommonAsyncTaskHashmap;
import com.app.creaseart.iclasses.HeaderViewManager;
import com.app.creaseart.interfaces.ApiResponse;
import com.app.creaseart.interfaces.GlobalConstants;
import com.app.creaseart.interfaces.HeaderViewClickListener;
import com.app.creaseart.interfaces.JsonApiHelper;
import com.app.creaseart.interfaces.OnCustomItemClicListener;
import com.app.creaseart.utils.AppConstant;
import com.app.creaseart.utils.AppUtils;
import com.app.creaseart.utils.GPSTracker;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by admin on 06-01-2016.
 */
public class Fragment_ServiceRequest extends BaseFragment implements ApiResponse, OnCustomItemClicListener, TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    private Bundle b;
    private Activity context;
    private TextView text_date, text_time, text_delivery_type;
    private EditText edt_quantity, edt_address;
    private Button btnSubmit;
    String latitude = "0.0", longitude = "0.0";
    public static Fragment_ServiceRequest fragment_changePassword;
    private final String TAG = Fragment_ServiceRequest.class.getSimpleName();
    View view_about;
    private Spinner spinner_deliverytype;
    ArrayAdapter<String> adapterDeliveryType;
    ArrayList<String> listDeliveryName = new ArrayList<>();
    ArrayList<String> listDeliveryId = new ArrayList<>();
    private String selectedDeliveryType = "";


    public static Fragment_ServiceRequest getInstance() {
        if (fragment_changePassword == null)
            fragment_changePassword = new Fragment_ServiceRequest();
        return fragment_changePassword;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this com.app.justclap.fragment

        view_about = inflater.inflate(R.layout.fragment_service_request, container, false);
        context = getActivity();

        b = getArguments();

        return view_about;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        edt_address = (EditText) view.findViewById(R.id.edt_address);
        edt_quantity = (EditText) view.findViewById(R.id.edt_quantity);
        spinner_deliverytype = (Spinner) view.findViewById(R.id.spinner_deliverytype);
        text_date = (TextView) view.findViewById(R.id.text_date);
        text_delivery_type = (TextView) view.findViewById(R.id.text_delivery_type);
        text_time = (TextView) view.findViewById(R.id.text_time);
        btnSubmit = (Button) view.findViewById(R.id.btnSubmit);
        manageHeaderView();
        setlistener();
        GPSTracker gps = new GPSTracker(context);
        if (gps.canGetLocation()) {
            latitude = "" + gps.getLatitude();
            longitude = "" + gps.getLongitude();
        } else {
            showSettingsAlert();
        }
        setCurrentLocation();
        getDeliveryType();
    }

    private void getDeliveryType() {
        Dashboard.getInstance().setProgressLoader(true);
        try {
            if (AppUtils.isNetworkAvailable(context)) {
                //       http://dev.stackmindz.com/creaseart/api/delivery_type.php
                String url = JsonApiHelper.BASEURL + JsonApiHelper.DELIVERY_TYPE;
                new CommonAsyncTaskHashmap(2, context, this).getqueryNoProgress(url);

            } else {
                Toast.makeText(context, context.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void showSettingsAlert() {
        try {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

            // Setting Dialog Title
            alertDialog.setTitle("GPS is settings");

            // Setting Dialog Message
            alertDialog
                    .setMessage("GPS is not enabled. Do you want to go to settings menu?");

            // On pressing Settings button
            alertDialog.setPositiveButton("Settings",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            Intent intent = new Intent(
                                    Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(intent);

                        }
                    });

            // on pressing cancel button
            alertDialog.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();

                        }
                    });

            // Showing Alert Message
            alertDialog.show();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    private void setCurrentLocation() {

        // TODO Auto-generated method stub
        GPSTracker gps = new GPSTracker(context);
        if (gps.canGetLocation) {
            latitude = "" + gps.getLatitude();
            longitude = "" + gps.getLongitude();

            GetAddressFromURLTask1 task1 = new GetAddressFromURLTask1();
            task1.execute(new String[]{latitude, longitude});

        } else {
            /*Toast.makeText(context, "Could not found lat long",
                    Toast.LENGTH_LONG).show();*/
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
        HeaderViewManager.getInstance().setHeading(true, "Service Request");
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

        text_delivery_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinner_deliverytype.performClick();

            }
        });
        spinner_deliverytype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                if (position != 0) {
                    selectedDeliveryType = listDeliveryId.get(position);
                    text_delivery_type.setText(listDeliveryName.get(position));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        text_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog date = DatePickerDialog.newInstance(
                        Fragment_ServiceRequest.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DATE)
                );
                date.show(context.getFragmentManager(), "startTime");
                date.setMinDate(now);

            }
        });

        text_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar now = Calendar.getInstance();
                TimePickerDialog tpd = TimePickerDialog.newInstance(
                        Fragment_ServiceRequest.this,
                        now.get(Calendar.HOUR_OF_DAY),
                        now.get(Calendar.MINUTE),
                        false
                );
                tpd.show(context.getFragmentManager(), "startTime");
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!AppUtils.getZoneId(context).equalsIgnoreCase("")) {
                    if (!text_time.getText().toString().equalsIgnoreCase("") && !text_date.getText().toString().equalsIgnoreCase("")
                            && !selectedDeliveryType.equalsIgnoreCase("") && !edt_address.getText().toString().equalsIgnoreCase("") && !edt_quantity.getText().toString().equalsIgnoreCase("")) {

                        submitRequest();
                    } else {
                        if (selectedDeliveryType.equalsIgnoreCase("")) {
                            text_delivery_type.requestFocus();
                            text_delivery_type.setError("Please select Delivery type");
                        } else if (text_date.getText().toString().equalsIgnoreCase("")) {
                            text_date.requestFocus();
                            text_date.setError("Please select date");
                        } else if (text_time.getText().toString().equalsIgnoreCase("")) {
                            text_time.requestFocus();
                            text_time.setError("Please select time");
                        } else if (edt_quantity.getText().toString().equalsIgnoreCase("")) {
                            edt_quantity.requestFocus();
                            edt_quantity.setError("Please enter quantity");
                        } else if (edt_address.getText().toString().equalsIgnoreCase("")) {
                            edt_address.requestFocus();
                            edt_address.setError("Please enter address");
                        }
                    }
                } else {
                    Toast.makeText(context, "Please update profile", Toast.LENGTH_SHORT).show();
                    Dashboard.getInstance().pushFragments(GlobalConstants.TAB_HOME_BAR, new UserProfileFragment(), true);
                }
            }
        });


    }

    private void submitRequest() {

        if (AppUtils.isNetworkAvailable(context)) {

            //  http://dev.stackmindz.com/creaseart/api/sendServiceRequest.php?user_id=1&zone_id=16
            // &quantity=10&date=2017-10-9&address=sdaddasd
            String url = JsonApiHelper.BASEURL + JsonApiHelper.SENDSERVICEREQUEST + "user_id=" + AppUtils.getUserId(context) + "&zone_id=" + AppUtils.getZoneId(context)
                    + "&address=" + edt_address.getText().toString() + "&quantity=" + edt_quantity.getText().toString() + "&date=" + text_date.getText().toString()
                    + "&time=" + text_time.getText().toString() + "&delivery_type=" + selectedDeliveryType;
            url = url.replace(" ", "%20");
            new CommonAsyncTaskHashmap(1, context, this).getqueryNoProgress(url);

        } else {
            Toast.makeText(context, context.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
        }
    }
 /*  private void signupUser() {

       //  http://dev.stackmindz.com/creaseart/api/register.php?name=test&mobile=1234567890&address=adadas
       // &password=123456&email=amitg@gmail.com&gcm=23123&imei=dasda&device_type=1&user_type=&state=&locality=&city=&zone=

       if (AppUtils.isNetworkAvailable(context)) {

           String url = JsonApiHelper.BASEURL + JsonApiHelper.SENDSERVICEREQUEST + "name=" + edtFirstname.getText().toString()
                   + "&mobile=" + edtMobileno.getText().toString() + "&password=" + edtPassword.getText().toString()
                   + "&email=" + edtEmailId.getText().toString() + "&gcm=" + AppUtils.getGcmRegistrationKey(mActivity)
                   + "&device_type=" + AppConstant.DEVICE_TYPE + "&imei=" + "";

           url = url.replace(" ", "%20");

           new CommonAsyncTaskHashmap(1, mActivity, this).getqueryNoProgress(url);

       } else {
           Toast.makeText(mActivity, mActivity.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
       }

   }*/

    @Override
    public void onPostSuccess(int method, JSONObject response) {
        try {
            if (method == 1) {
                JSONObject commandResult = response.getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {
                    Toast.makeText(context, commandResult.getString("message"), Toast.LENGTH_SHORT).show();
                    context.onBackPressed();
                } else {
                    Toast.makeText(context, commandResult.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } else if (method == 2) {
                listDeliveryId.clear();
                listDeliveryName.clear();

                JSONObject commandResult = response
                        .getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {

                    JSONObject data = commandResult.getJSONObject("data");
                    JSONArray array = data.getJSONArray("delivery_type");

                    listDeliveryName.add("Select Delivery type");
                    listDeliveryId.add("-1");

                    for (int i = 0; i < array.length(); i++) {

                        JSONObject jsonObject = array.getJSONObject(i);
                        listDeliveryName.add(jsonObject.getString("DeliveryName") + " (" + jsonObject.getString("DeliveryTime") + ")");
                        listDeliveryId.add(jsonObject.getString("DeliveryId"));
                    }

                    adapterDeliveryType = new ArrayAdapter<String>(context, R.layout.row_spinner, R.id.text_view, listDeliveryName);
                    spinner_deliverytype.setAdapter(adapterDeliveryType);
                } else {
                    listDeliveryName.add("Select Delivery type");
                    listDeliveryId.add("-1");
                    adapterDeliveryType.notifyDataSetChanged();
                    Toast.makeText(context, commandResult.getString("message"), Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPostFail(int method, String response) {
        if (context != null && isAdded())
            Toast.makeText(getActivity(), getResources().getString(R.string.problem_server), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClickListener(int position, int flag) {

    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        int month = monthOfYear + 1;
        text_date.setText(year + "-" + month + "-" + dayOfMonth);

    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {

        text_time.setText(hourOfDay + ":" + minute);
    }

    private class GetAddressFromURLTask1 extends AsyncTask<String, Void, String> {
        protected void onPreExecute() {
            super.onPreExecute();

        }

        protected String doInBackground(String... urls) {

            String response = "";
            HttpResponse response2 = null;
            StringBuilder stringBuilder = new StringBuilder();

            try {

                HttpGet httpGet = new HttpGet("http://maps.google.com/maps/api/geocode/json?latlng=" + urls[0] + "," + urls[1] + "&ln=en");

                HttpClient client = new DefaultHttpClient();
                Log.e("Url ", "http://maps.google.com/maps/api/geocode/json?ln=en&latlng=" + urls[0] + "," + urls[1]);


                try {
                    response2 = client.execute(httpGet);

                    HttpEntity entity = response2.getEntity();

                    char[] buffer = new char[2048];
                    Reader reader = new InputStreamReader(entity.getContent(), "UTF-8");

                    while (true) {
                        int n = reader.read(buffer);
                        if (n < 0) {
                            break;
                        }
                        stringBuilder.append(buffer, 0, n);
                    }

                    Log.e("Url response1", stringBuilder.toString());

                } catch (ClientProtocolException e) {
                } catch (IOException e) {
                }

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject = new JSONObject(stringBuilder.toString());

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            } catch (Exception e) {

                e.printStackTrace();
                Log.e("Error 2 :>>", "error in doINBackground OUTER");
                //infowindow.setText("Error in connecting to Google Server... try again later");
            }
            return stringBuilder.toString();
            //return jsonObject;
        }


        protected void onPostExecute(String result) {

            try {
                if (result != null) {
                    //result=	Html.fromHtml(result).toString();
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray resultsObject = jsonObject.getJSONArray("results");
                    JSONObject formattedAddress = (JSONObject) resultsObject.get(0);
                    String formatted_address = formattedAddress.getString("formatted_address");

                    Log.e("formatted Adss from>>", formatted_address);
                    // edt_address.setText(formatted_address);

                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }


    }
}

