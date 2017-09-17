package com.app.creaseart.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.app.creaseart.R;
import com.app.creaseart.activities.Dashboard;
import com.app.creaseart.adapter.AdapterSearchFamilyList;
import com.app.creaseart.aynctask.CommonAsyncTaskHashmap;
import com.app.creaseart.iclasses.HeaderViewManager;
import com.app.creaseart.interfaces.ApiResponse;
import com.app.creaseart.interfaces.ConnectionDetector;
import com.app.creaseart.interfaces.HeaderViewClickListener;
import com.app.creaseart.interfaces.JsonApiHelper;
import com.app.creaseart.interfaces.OnCustomItemClicListener;
import com.app.creaseart.models.ModelFamiyMember;
import com.app.creaseart.utils.AppUtils;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by admin on 06-01-2016.
 */
public class SearchFamilyMember extends BaseFragment implements ApiResponse, OnCustomItemClicListener {


    private RecyclerView list_request;
    private Bundle b;
    private Activity context;
    private AdapterSearchFamilyList adapterFamilyList;
    private ModelFamiyMember modelFamiyMember;
    private ArrayList<ModelFamiyMember> arrayList;
    private ConnectionDetector cd;
    private EditText edtSearch;
    private LinearLayoutManager layoutManager;
    private boolean loading = true;
    private ImageView headerRightImage;
    public static SearchFamilyMember fragment_familyAccuntLIst;
    private Toolbar toolbar;
    private final String TAG = SearchFamilyMember.class.getSimpleName();
    View view_about;

    public static SearchFamilyMember getInstance() {
        if (fragment_familyAccuntLIst == null)
            fragment_familyAccuntLIst = new SearchFamilyMember();
        return fragment_familyAccuntLIst;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this com.app.justclap.fragment

        view_about = inflater.inflate(R.layout.search_family_member, container, false);
        context = getActivity();
        arrayList = new ArrayList<>();
        b = getArguments();
        manageHeaderView();
        return view_about;
    }


    /*******************************************************************
     * Function name - manageHeaderView
     * Description - manage the initialization, visibility and click
     * listener of view fields on Header view
     *******************************************************************/
    private void manageHeaderView() {

        Dashboard.getInstance().manageHeaderVisibitlity(false);
        HeaderViewManager.getInstance().InitializeHeaderView(null, view_about, manageHeaderClick());
        HeaderViewManager.getInstance().setHeading(true, "Search Family Members");
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


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        headerRightImage = (ImageView) view.findViewById(R.id.headerRightImage);
        list_request = (RecyclerView) view.findViewById(R.id.list_request);
        layoutManager = new LinearLayoutManager(context);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        list_request.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>();
        edtSearch = (EditText) view.findViewById(R.id.edtSearch);

        setlistener();

    }

    private void setlistener() {
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                getServicelistRefresh(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {


            }
        });
    }

    @Override
    public void onItemClickListener(int position, int flag) {

        if (flag == 1) {
            openAddDialog(arrayList.get(position).getMemberId());
        }
    }

    /**
     * Open dialog for the add member
     */
    private void openAddDialog(final String id) {
        try {
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

            // inflate the layout dialog_layout.xml and set it as contentView
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.add_member_dialog, null, false);
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
                        addFamilyMember(edt_comment.getText().toString(), id);
                        dialog.dismiss();
                    } else {
                        edt_comment.setError("Please enter Code");
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


    private void getServicelistRefresh(String text) {
        try {
            if (AppUtils.isNetworkAvailable(context)) {
                //   http://dev.stackmindz.com/creaseart/api/searchMember.php?user_id=1&search_data=88

                String url = JsonApiHelper.BASEURL + JsonApiHelper.SEARCHMEMBER + "user_id=" + AppUtils.getUserId(context) + "&search_data=" + text;
                new CommonAsyncTaskHashmap(1, context, this).getqueryNoProgress(url);

            } else {
                Toast.makeText(context, context.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void addFamilyMember(String text, String id) {
        try {
            if (AppUtils.isNetworkAvailable(context)) {
                // http://dev.stackmindz.com/creaseart/api/addMemeber.php?unique_code=CO080071&member_id=2&user_id=1
                String url = JsonApiHelper.BASEURL + JsonApiHelper.ADDMEMBER + "user_id=" + AppUtils.getUserId(context) + "&unique_code=" + text.trim() + "&member_id=" + id;
                new CommonAsyncTaskHashmap(2, context, this).getqueryJsonbject(url, null, Request.Method.GET);

            } else {
                Toast.makeText(context, context.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onPostSuccess(int position, JSONObject jObject) {
        try {
            if (position == 1) {
                Dashboard.getInstance().setProgressLoader(false);
                JSONObject commandResult = jObject.getJSONObject("commandResult");
                Gson gson = new Gson();
                if (commandResult.getString("success").equalsIgnoreCase("1")) {

                    JSONArray array = commandResult.getJSONArray("data");
                    arrayList.clear();
                    for (int i = 0; i < array.length(); i++) {

                        JSONObject jo = array.getJSONObject(i);

                        ModelFamiyMember modelFamiyMember = null;
                        try {
                            modelFamiyMember = gson.fromJson(array.getJSONObject(i).toString(), ModelFamiyMember.class);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        modelFamiyMember.setRowType(1);

                        arrayList.add(modelFamiyMember);
                    }
                    adapterFamilyList = new AdapterSearchFamilyList(getActivity(), this, arrayList);
                    list_request.setAdapter(adapterFamilyList);


                } else {
                    arrayList.clear();
                    if (adapterFamilyList != null)
                        adapterFamilyList.notifyDataSetChanged();
                }

            } else if (position == 2) {
                JSONObject commandResult = jObject.getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {
                    Toast.makeText(context, commandResult.getString("message"), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, commandResult.getString("message"), Toast.LENGTH_SHORT).show();
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
