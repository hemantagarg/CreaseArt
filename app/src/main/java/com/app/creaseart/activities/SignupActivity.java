package com.app.creaseart.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.app.creaseart.R;
import com.app.creaseart.aynctask.CommonAsyncTaskHashmap;
import com.app.creaseart.interfaces.ApiResponse;
import com.app.creaseart.interfaces.JsonApiHelper;
import com.app.creaseart.utils.AppConstant;
import com.app.creaseart.utils.AppUtils;
import com.app.creaseart.utils.GPSTracker;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

public class SignupActivity extends AppCompatActivity implements ApiResponse {

    private Activity mActivity;
    private TextView text_freelancer, text_user;
    private EditText edtFirstname, edtLastname, edtEmailId, edtMobileno, edtPassword;

    private boolean isFreelancer = false;
    private String selectedGender = "", selectedServiceId = "", selectedserviceName = "";
    private RelativeLayout rl_category;
    private Button btnSignup;
    private EditText edtAddress;
    private Spinner text_select_category;
    String latitude = "0.0", longitude = "0.0";
    CheckBox checkbox_terms;

    TextView text_terms;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mActivity = SignupActivity.this;
        initViews();
        setListener();
        clickableText();
        GPSTracker gps = new GPSTracker(mActivity);
        if (gps.canGetLocation()) {
            latitude = "" + gps.getLatitude();
            longitude = "" + gps.getLongitude();
        } else {
            showSettingsAlert();
        }

        setCurrentLocation();
    }

    public void showSettingsAlert() {
        try {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(mActivity);

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
                            finish();
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
        GPSTracker gps = new GPSTracker(mActivity);
        if (gps.canGetLocation) {
            latitude = "" + gps.getLatitude();
            longitude = "" + gps.getLongitude();

            /*GetAddressFromURLTask1 task1 = new GetAddressFromURLTask1();
            task1.execute(new String[]{latitude, longitude});*/

        } else {
            /*Toast.makeText(context, "Could not found lat long",
                    Toast.LENGTH_LONG).show();*/
        }

    }

    private void initViews() {

        text_select_category = (Spinner) findViewById(R.id.text_select_category);
        text_terms = (TextView) findViewById(R.id.text_terms);

        edtFirstname = (EditText) findViewById(R.id.edtFirstname);
        edtAddress = (EditText) findViewById(R.id.edtAddress);

        edtPassword = (EditText) findViewById(R.id.edtPassword);
        edtEmailId = (EditText) findViewById(R.id.edtEmailId);
        edtMobileno = (EditText) findViewById(R.id.edtMobileno);

        rl_category = (RelativeLayout) findViewById(R.id.rl_category);
        btnSignup = (Button) findViewById(R.id.btnSignup);
        checkbox_terms = (CheckBox) findViewById(R.id.checkbox_terms);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setListener() {



        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               /* if (isValidLoginDetails()) {
                    if (isFreelancer) {
                        if (!text_select_category.getText().toString().equalsIgnoreCase("")) {
                            if (checkbox_terms.isChecked()) {
                                signupUser();
                            } else {
                                Toast.makeText(mActivity, "Please agree to our terms & conditions", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(getApplicationContext(), R.string.select_city, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        if (checkbox_terms.isChecked()) {
                            signupUser();
                        } else {
                            Toast.makeText(mActivity, "Please agree to our terms & conditions", Toast.LENGTH_SHORT).show();
                        }

                    }

                }*/
            }
        });
        text_select_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                //selectedServiceId = serviceId.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        rl_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              /*  Intent intent = new Intent(mActivity, SelctCategoryListActivity.class);
                startActivityForResult(intent, 511);*/
            }
        });

    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 511 && resultCode == 52) {
            selectedServiceId = data.getStringExtra("serviceId");
            selectedserviceName = data.getStringExtra("servicename");
            text_select_category.setText(selectedserviceName);
        }
    }*/
    private void clickableText() {
        SpannableString ss = new SpannableString("I agree with terms and condition and privacy policy");
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                // startActivity(new Intent(SignUp.this, Login.class));

                Intent in = new Intent(SignupActivity.this, PrivacyPolicy.class);
                in.putExtra("title", "Terms and Conditions");
                startActivity(in);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(true);
            }
        };
        ClickableSpan clickableSpan1 = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                Intent in = new Intent(SignupActivity.this, PrivacyPolicy.class);
                in.putExtra("title", "Privacy Policy");
                startActivity(in);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(true);
            }
        };

        ss.setSpan(clickableSpan, 13, 32, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(clickableSpan1, 37, 51, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        text_terms.setText(ss);
        text_terms.setMovementMethod(LinkMovementMethod.getInstance());
        text_terms.setHighlightColor(Color.TRANSPARENT);

    }
    private void signupUser() {

        //  http://onlineworkpro.com/trendi/api/register.php?name=aa&mobile=7895689562&password=123456&user_type=1
        // &country_id=3&category_id=2,3&address=aaa&email=aaa@gmail.com&gcm=sdasd&device_type=1&imei=sss&gen_type=male
        //  user_type 2 for user and 3 for freelancer




        if (AppUtils.isNetworkAvailable(mActivity)) {

            String url = JsonApiHelper.BASEURL + JsonApiHelper.REGISTER + "name=" + edtFirstname.getText().toString() + edtLastname.getText().toString()
                    + "&mobile=" + edtMobileno.getText().toString() + "&password=" + edtPassword.getText().toString()
                    + "&service_id=" + selectedServiceId + "&address=" + edtAddress.getText().toString() + "&email=" + edtEmailId.getText().toString() + "&gcm=" + AppUtils.getGcmRegistrationKey(mActivity)
                    + "&device_type=" + AppConstant.DEVICE_TYPE + "&imei=" + "" + "&latitude=" + latitude + "&longitude=" + longitude;

            url = url.replace(" ", "%20");

            new CommonAsyncTaskHashmap(1, mActivity, this).getqueryNoProgress(url);

        } else {
            Toast.makeText(mActivity, mActivity.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
        }

    }

    private boolean isValidLoginDetails() {
        boolean isValidLoginDetails = true;

        String emailaddress = edtEmailId.getText().toString();
        String password = edtPassword.getText().toString();
        String first_name = edtFirstname.getText().toString();
        String last_name = edtLastname.getText().toString();
        String mobileno = edtMobileno.getText().toString();


        if (!first_name.equalsIgnoreCase("") && !emailaddress.equalsIgnoreCase("")
                && !last_name.equalsIgnoreCase("") && !mobileno.equalsIgnoreCase("") && !password.equalsIgnoreCase("")
                ) {

            if (!AppUtils.isEmailValid(emailaddress.trim())) {
                isValidLoginDetails = false;
                Toast.makeText(getApplicationContext(), R.string.validEmail, Toast.LENGTH_SHORT).show();
            } else if (mobileno.length() < 10) {
                isValidLoginDetails = false;
                Toast.makeText(getApplicationContext(), R.string.mobileno_Length, Toast.LENGTH_SHORT).show();
            } else {
                isValidLoginDetails = true;
            }

        } else {
            if (first_name.equalsIgnoreCase("")) {
                isValidLoginDetails = false;
                Toast.makeText(getApplicationContext(), R.string.enter_name, Toast.LENGTH_SHORT).show();
            }  else if (emailaddress.equalsIgnoreCase("")) {
                isValidLoginDetails = false;
                Toast.makeText(getApplicationContext(), R.string.enter_email, Toast.LENGTH_SHORT).show();
            } else if (mobileno.equalsIgnoreCase("")) {
                isValidLoginDetails = false;
                Toast.makeText(getApplicationContext(), R.string.enter_mb, Toast.LENGTH_SHORT).show();
            } else if (password.equalsIgnoreCase("")) {
                isValidLoginDetails = false;
                Toast.makeText(getApplicationContext(), R.string.enter_password, Toast.LENGTH_SHORT).show();
            }
        }

        return isValidLoginDetails;
    }


    @Override
    public void onPostSuccess(int method, JSONObject response) {

        try {
            if (method == 1) {

                JSONObject commandResult = response.getJSONObject("commandResult");

                if (commandResult.getString("success").equalsIgnoreCase("1")) {

                    JSONObject data = commandResult.getJSONObject("data");

                    AppUtils.setUserId(mActivity, data.getString("UserId"));
                    AppUtils.setUserRole(mActivity, data.getString("UserType"));
                    AppUtils.setUserName(mActivity, data.getString("Name"));
                    AppUtils.setUseremail(mActivity, data.getString("Email"));
                    AppUtils.setUserMobile(mActivity, data.getString("Mobile"));
                    AppUtils.setUserImage(mActivity, data.getString("ProfilePic"));
                    if (data.has("services")) {
                        AppUtils.setCategories(mActivity, data.getJSONArray("services").toString());
                    }
                    // AppUtils.setCategoriesId(mActivity, data.getString("CategoryId"));
                    //  AppUtils.setCompanyName(mActivity, data.getString("CompanyName"));
                    //AppUtils.setCountryId(mActivity, data.getString("CountryId"));

//                    if (isFreelancer){
//                        Intent intent = new Intent(mActivity, VendorDashboard.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(intent);
//                    }else {
//                        Intent intent = new Intent(mActivity, UserDashboard.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(intent);
//                    }

                } else {
                    Toast.makeText(mActivity, commandResult.getString("message"), Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onPostFail(int method, String response) {

    }


    /*private class GetAddressFromURLTask1 extends AsyncTask<String, Void, String> {
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
                    edtAddress.setText(formatted_address);

                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }


    }*/


}
