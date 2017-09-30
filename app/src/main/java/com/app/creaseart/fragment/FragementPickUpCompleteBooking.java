package com.app.creaseart.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.app.creaseart.R;
import com.app.creaseart.adapter.AdapterPickUpCompleteBookings;

import com.app.creaseart.aynctask.CommonAsyncTaskHashmap;
import com.app.creaseart.interfaces.ApiResponse;
import com.app.creaseart.interfaces.JsonApiHelper;
import com.app.creaseart.interfaces.OnCustomItemClicListener;
import com.app.creaseart.models.ModelCategory;
import com.app.creaseart.utils.AppUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class FragementPickUpCompleteBooking extends BaseFragment implements OnCustomItemClicListener, ApiResponse {

    private RecyclerView recycler_service;
    private ArrayList<ModelCategory> imagelist;
    private AdapterPickUpCompleteBookings adapterPickUpCompleteBookings;
    private Activity mActivity;
    private SwipeRefreshLayout swipeRefreshLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.freelancer_ongoing_booking, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mActivity = getActivity();
        imagelist = new ArrayList<>();
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimaryDark);
        recycler_service = (RecyclerView) view.findViewById(R.id.recycler_services);
        recycler_service.setLayoutManager(new LinearLayoutManager(mActivity));
        getData();
        setListener();
    }

    private void setListener() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();

            }
        });

    }

    private void refreshData() {
        //   http://dev.stackmindz.com/trendi/api/mycompletebooking.php?user_id=201&user_role=2
        if (AppUtils.isNetworkAvailable(mActivity)) {

            //String url = JsonApiHelper.BASEURL + JsonApiHelper.PICKUPBOYCOMPLETED + "user_id="+ AppUtils.getUserId(mActivity);
            String url = JsonApiHelper.BASEURL + JsonApiHelper.PICKUPBOYCOMPLETED + "user_id="+ 3;
            new CommonAsyncTaskHashmap(1, mActivity, this).getqueryNoProgress(url);

        } else {
            Toast.makeText(mActivity, mActivity.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
        }
    }


   private void getData() {
        //  http://dev.stackmindz.com/trendi/api/mybooking.php?user_id=200&user_role=3


        if (AppUtils.isNetworkAvailable(mActivity)) {

           // String url = JsonApiHelper.BASEURL + JsonApiHelper.PICKUPBOYCOMPLETED + "user_id="+ AppUtils.getUserId(mActivity);
            String url = JsonApiHelper.BASEURL + JsonApiHelper.PICKUPBOYCOMPLETED + "user_id="+ 3;
            new CommonAsyncTaskHashmap(1, mActivity, this).getqueryNoProgress(url);

        } else {
            Toast.makeText(mActivity, mActivity.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onItemClickListener(int position, int flag) {

    }

    @Override
    public void onPostSuccess(int method, JSONObject response) {
        try {
            if (method == 1) {
                JSONObject commandResult = response.getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {

                    JSONObject data = commandResult.getJSONObject("data");
                    JSONArray array = data.getJSONArray("Booking");
                    imagelist.clear();
                    for (int i = 0; i < array.length(); i++) {

                        JSONObject jo = array.getJSONObject(i);

                        ModelCategory serviceDetail = new ModelCategory();

                        serviceDetail.setOrderNo(jo.getString("OrderNo"));
                        serviceDetail.setQuantity(jo.getString("Quantity"));
                        serviceDetail.setAddress(jo.getString("Address"));
                        serviceDetail.setStatus(jo.getString("Status"));
                        serviceDetail.setDate(jo.getString("Date"));
                        serviceDetail.setUserName(jo.getString("userName"));
                        serviceDetail.setUserImage(jo.getString("userImage"));
                        serviceDetail.setZone(jo.getString("Zone"));



                        imagelist.add(serviceDetail);

                    }
                    adapterPickUpCompleteBookings = new AdapterPickUpCompleteBookings(mActivity, FragementPickUpCompleteBooking.this, imagelist);
                    recycler_service.setAdapter(adapterPickUpCompleteBookings);
                    if (swipeRefreshLayout != null) {
                        swipeRefreshLayout.setRefreshing(false);
                    }

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
