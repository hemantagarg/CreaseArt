package com.app.creaseart.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.app.creaseart.R;
import com.app.creaseart.activities.Dashboard;
import com.app.creaseart.adapter.AdapterActivePackages;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by admin on 06-01-2016.
 */
public class Fragment_ActivePackage extends BaseFragment implements ApiResponse, OnCustomItemClicListener {


    private RecyclerView list_request;
    private Bundle b;
    private Activity context;
    private AdapterActivePackages adapterPackages;
    private ModelPackage modelPackage;
    private ArrayList<ModelPackage> arrayList;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ConnectionDetector cd;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    private LinearLayoutManager layoutManager;
    private int skipCount = 0;
    private boolean loading = true;
    private String maxlistLength = "";
    View view_about;

    public static Fragment_ActivePackage fragmentPackage;
    private final String TAG = Fragment_ActivePackage.class.getSimpleName();

    public static Fragment_ActivePackage getInstance() {
        if (fragmentPackage == null)
            fragmentPackage = new Fragment_ActivePackage();
        return fragmentPackage;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this com.app.justclap.fragment

        view_about = inflater.inflate(R.layout.fragment_notification, container, false);
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
        layoutManager = new GridLayoutManager(context, 2);
        list_request.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>();
        setlistener();
        manageHeaderView();
        getServicelistRefresh();
    }

    /*******************************************************************
     * Function name - manageHeaderView
     * Description - manage the initialization, visibility and click
     * listener of view fields on Header view
     *******************************************************************/
    private void manageHeaderView() {

        Dashboard.getInstance().manageHeaderVisibitlity(false);
        HeaderViewManager.getInstance().InitializeHeaderView(null, view_about, manageHeaderClick());
        HeaderViewManager.getInstance().setHeading(true, "Active Packages");
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


    }

    @Override
    public void onItemClickListener(int position, int flag) {
        if (flag == 1) {

            Fragment_Bundle fragment_bundle = new Fragment_Bundle();
            Bundle bundle = new Bundle();
            bundle.putString("array", arrayList.get(position).getJsonArray());
            fragment_bundle.setArguments(bundle);
            Dashboard.getInstance().pushFragments(GlobalConstants.TAB_HOME_BAR, fragment_bundle, true);
        }
    }


    private void getServicelistRefresh() {
        Dashboard.getInstance().setProgressLoader(true);
        try {
            skipCount = 0;
            if (AppUtils.isNetworkAvailable(context)) {
                //    http://sfscoring.betasportzfever.com/getNotifications/155/efc0c68e-8bb5-11e7-8cf8-008cfa5afa52
             /*   HashMap<String, Object> hm = new HashMap<>();*/
                String url = JsonApiHelper.BASEURL + JsonApiHelper.ACTIVEPACKAGE+"user_id="+ AppUtils.getUserId(context);
                new CommonAsyncTaskHashmap(1, context, this).getqueryNoProgress(url);

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

                if (commandResult.getString("success").equalsIgnoreCase("1")) {

                    JSONObject data = commandResult.getJSONObject("data");
                    JSONArray array = data.getJSONArray("ActivePackage");
                    arrayList.clear();

                    for (int i = 0; i < array.length(); i++) {

                        JSONObject jo = array.getJSONObject(i);
                        ModelPackage serviceDetail = new ModelPackage();

                        serviceDetail.setJsonArray(jo.toString());
                        serviceDetail.setPackageId(jo.getString("packageId"));
                        serviceDetail.setPackageName(jo.getString("packageName"));
                        serviceDetail.setPackagePrice(jo.getString("packagePrice"));
                        serviceDetail.setRowType(1);

                        arrayList.add(serviceDetail);
                    }
                    adapterPackages = new AdapterActivePackages(getActivity(), this, arrayList);
                    list_request.setAdapter(adapterPackages);

                    if (mSwipeRefreshLayout != null) {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }

                } else {
                    if (mSwipeRefreshLayout != null) {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
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

