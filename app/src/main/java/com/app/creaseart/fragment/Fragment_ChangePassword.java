package com.app.creaseart.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.app.creaseart.R;
import com.app.creaseart.activities.Dashboard;
import com.app.creaseart.aynctask.CommonAsyncTaskHashmap;
import com.app.creaseart.iclasses.HeaderViewManager;
import com.app.creaseart.interfaces.ApiResponse;
import com.app.creaseart.interfaces.HeaderViewClickListener;
import com.app.creaseart.interfaces.JsonApiHelper;
import com.app.creaseart.interfaces.OnCustomItemClicListener;
import com.app.creaseart.utils.AppUtils;

import org.json.JSONObject;

/**
 * Created by admin on 06-01-2016.
 */
public class Fragment_ChangePassword extends BaseFragment implements ApiResponse, OnCustomItemClicListener {


    private Bundle b;
    private Activity context;
    private EditText edtold_password, edt_newpassword, edtconfirmpassword;
    private Button btnSubmit;
    public static Fragment_ChangePassword fragment_changePassword;
    private final String TAG = Fragment_ChangePassword.class.getSimpleName();
    View view_about;

    public static Fragment_ChangePassword getInstance() {
        if (fragment_changePassword == null)
            fragment_changePassword = new Fragment_ChangePassword();
        return fragment_changePassword;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this com.app.justclap.fragment

        view_about = inflater.inflate(R.layout.activity_change_password, container, false);
        context = getActivity();

        b = getArguments();

        return view_about;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        edtold_password = (EditText) view.findViewById(R.id.edtold_password);
        edt_newpassword = (EditText) view.findViewById(R.id.edt_newpassword);
        edtconfirmpassword = (EditText) view.findViewById(R.id.edtconfirmpassword);
        btnSubmit = (Button) view.findViewById(R.id.btnSubmit);
        manageHeaderView();
        setlistener();

    }

    /*******************************************************************
     * Function name - manageHeaderView
     * Description - manage the initialization, visibility and click
     * listener of view fields on Header view
     *******************************************************************/
    private void manageHeaderView() {

        Dashboard.getInstance().manageHeaderVisibitlity(false);
        HeaderViewManager.getInstance().InitializeHeaderView(null, view_about, manageHeaderClick());
        HeaderViewManager.getInstance().setHeading(true, "Change Password");
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


    }

    private void submitRequest() {

        if (AppUtils.isNetworkAvailable(context)) {

            // http://dev.stackmindz.com/trendi/api/change-password.php?user_id=199&current_pwd=admin&new_pwd=123456&confirm_pwd=123456
            String url = JsonApiHelper.BASEURL + JsonApiHelper.CHANGEPASSWORD + "user_id=" + AppUtils.getUserId(context)+ "&current_pwd=" + edtold_password.getText().toString()
                    + "&new_pwd=" + edt_newpassword.getText().toString() + "&confirm_pwd=" + edtconfirmpassword.getText().toString()+"&user_type=" +AppUtils.getUserRole(context) ;

            new CommonAsyncTaskHashmap(1, context, this).getqueryNoProgress(url);

        } else {
            Toast.makeText(context, context.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
        }
    }


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
}

