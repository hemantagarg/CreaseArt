package com.app.creaseart.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.app.creaseart.R;
import com.app.creaseart.activities.Dashboard;
import com.app.creaseart.adapter.AdapterNotification;
import com.app.creaseart.aynctask.CommonAsyncTaskHashmap;
import com.app.creaseart.interfaces.ApiResponse;
import com.app.creaseart.interfaces.ConnectionDetector;
import com.app.creaseart.interfaces.JsonApiHelper;
import com.app.creaseart.interfaces.OnCustomItemClicListener;
import com.app.creaseart.models.ModelNotification;
import com.app.creaseart.utils.AppUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by admin on 06-01-2016.
 */
public class Fragment_ChangePassword extends BaseFragment implements ApiResponse, OnCustomItemClicListener {



    private Bundle b;
    private Context context;


    private ConnectionDetector cd;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    private LinearLayoutManager layoutManager;
    private int skipCount = 0;
    private boolean loading = true;
    private String maxlistLength = "";
    private EditText edtold_password,edt_newpassword,edtconfirmpassword ;
    private Button btnSubmit;
    public static Fragment_ChangePassword fragment_changePassword;
    private final String TAG = Fragment_ChangePassword.class.getSimpleName();

    public static Fragment_ChangePassword getInstance() {
        if (fragment_changePassword == null)
            fragment_changePassword = new Fragment_ChangePassword();
        return fragment_changePassword;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this com.app.justclap.fragment

        View view_about = inflater.inflate(R.layout.activity_change_password, container, false);
        context = getActivity();

        b = getArguments();

        return view_about;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        edtold_password=(EditText)view.findViewById(R.id.edtold_password);
        edt_newpassword=(EditText)view.findViewById(R.id.edt_newpassword);
        edtconfirmpassword=(EditText)view.findViewById(R.id.edtconfirmpassword);
        btnSubmit=(Button) view.findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!edtold_password.getText().toString().equalsIgnoreCase("") && !edt_newpassword.getText().toString().equalsIgnoreCase("") && !edtconfirmpassword.getText().toString().equalsIgnoreCase("")) {

                    if (edt_newpassword.getText().toString().equals(edtconfirmpassword.getText().toString())) {
                        submitRequest();
                    } else {
                        Toast.makeText(context, "Password does not match", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    if (edtold_password.getText().toString().equalsIgnoreCase("")) {
                        edtold_password.requestFocus();
                        edtold_password.setError("Enter Old Password");
                    } else if (edt_newpassword.getText().toString().equalsIgnoreCase("")) {
                        edt_newpassword.requestFocus();
                        edt_newpassword.setError("Enter New Password");
                    } else if (edtconfirmpassword.getText().toString().equalsIgnoreCase("")) {
                        edtconfirmpassword.requestFocus();
                        edtconfirmpassword.setError("Confirm password");
                    }
                }
            }
        });
        setlistener();


    }

    private void setlistener() {




    }

    private void submitRequest() {

        if (AppUtils.isNetworkAvailable(context)) {

            // http://dev.stackmindz.com/trendi/api/change-password.php?user_id=199&current_pwd=admin&new_pwd=123456&confirm_pwd=123456
            String url = JsonApiHelper.BASEURL + JsonApiHelper.CHANGEPASSWORD + "user_id=1"  + "&current_pwd=" + edtold_password.getText().toString()
                    + "&new_pwd=" + edt_newpassword.getText().toString() + "&confirm_pwd=" + edtconfirmpassword.getText().toString();

            new CommonAsyncTaskHashmap(1, context, this).getqueryNoProgress(url);

        } else {
            Toast.makeText(context, context.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
        }
    }




    @Override
    public void onPostSuccess(int method, JSONObject response) {

    }

    @Override
    public void onPostFail(int method, String response) {
        if (context != null && isAdded())
            Toast.makeText(getActivity(), getResources().getString(R.string.problem_server), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClickListener(int position, int flag) {

    }
}

