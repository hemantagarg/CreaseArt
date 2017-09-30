package com.app.creaseart.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.app.creaseart.R;
import com.app.creaseart.aynctask.CommonAsyncTaskHashmap;
import com.app.creaseart.interfaces.ApiResponse;
import com.app.creaseart.interfaces.JsonApiHelper;
import com.app.creaseart.utils.AppConstant;
import com.app.creaseart.utils.AppUtils;

import org.json.JSONObject;

public class PickUpLoginActivity extends AppCompatActivity implements ApiResponse {

    private Activity mActivity;
    private EditText edtEmail, edtPassword;
    private Button btn_login;
    private TextView createAccount, forgot_password, signup,pickuplogin;
    private ImageView image_facebook, image_twitter;
    String latitude = "0.0", longitude = "0.0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pickuplogin);
        mActivity = PickUpLoginActivity.this;
        initViews();
        setListener();

    }

    private void setListener() {
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!edtEmail.getText().toString().equalsIgnoreCase("") && !edtPassword.getText().toString().equalsIgnoreCase("")) {

                    if (AppUtils.isEmailValid(edtEmail.getText().toString())) {

                        loginUser();
                    } else {
                        edtEmail.setError(getString(R.string.enter_valid_emailid));
                        edtEmail.requestFocus();
                    }
                } else {
                    if (edtEmail.getText().toString().equalsIgnoreCase("")) {
                        edtEmail.setError(getString(R.string.enter_email));
                        edtEmail.requestFocus();
                    } else if (edtPassword.getText().toString().equalsIgnoreCase("")) {
                        edtPassword.setError(getString(R.string.enter_password));
                        edtPassword.requestFocus();
                    }
                }
            }
        });

        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(mActivity, PickUpForgotPassword.class));
            }
        });
      pickuplogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(mActivity, LoginActivity.class));
            }
        });

    }


    private void loginUser() {

        if (AppUtils.isNetworkAvailable(mActivity)) {

            String url = JsonApiHelper.BASEURL + JsonApiHelper.LOGIN + "mobile=" + edtEmail.getText().toString() + "&password=" + edtPassword.getText().toString()
                    + "&device_type=" + AppConstant.DEVICE_TYPE + "&gcm=" + AppUtils.getGcmRegistrationKey(mActivity) + "&user_type=" + AppConstant.PICKUPBOYTYPE;

            //String url = JsonApiHelper.BASEURL + JsonApiHelper.LOGIN;
            new CommonAsyncTaskHashmap(1, mActivity, this).getqueryJsonNoProgress(url, null, Request.Method.GET);
        } else {
            Toast.makeText(mActivity, mActivity.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
        }
    }


    private void initViews() {

        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        btn_login = (Button) findViewById(R.id.btn_login);
        createAccount = (TextView) findViewById(R.id.createAccount);
        pickuplogin = (TextView) findViewById(R.id.pickuplogin);

        forgot_password = (TextView) findViewById(R.id.forgot_password);


    }


    @Override
    public void onPostSuccess(int method, JSONObject response) {

        try {
            if (method == 1) {

                JSONObject commandResult = response.getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {

                    JSONObject data = commandResult.getJSONObject("data");
                    AppUtils.setUserId(mActivity, data.getString("UserId"));
                    AppUtils.setUserMobile(mActivity, data.getString("Mobile"));
                    AppUtils.setUserRole(mActivity, data.getString("UserType"));
                    AppUtils.setUniqueToken(mActivity, data.getString("Token"));
                    AppUtils.setUserCode(mActivity, data.getString("ReferCode"));
                    AppUtils.setZoneId(mActivity, data.getString("ZoneId"));
                    AppUtils.setUserVerificationCode(mActivity, data.getString("unique_code"));
                    AppUtils.setUserName(mActivity, data.getString("Name"));
                    AppUtils.setUseremail(mActivity, data.getString("Email"));
                    AppUtils.setUserImage(mActivity, data.getString("ProfilePic"));
                    startActivity(new Intent(mActivity, Dashboard.class));
                    finish();
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
}
