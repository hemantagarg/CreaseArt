package com.app.creaseart.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.app.creaseart.R;
import com.app.creaseart.activities.Dashboard;
import com.app.creaseart.activities.PaymentGateway;
import com.app.creaseart.aynctask.CommonAsyncTaskHashmap;
import com.app.creaseart.iclasses.HeaderViewManager;
import com.app.creaseart.interfaces.ApiResponse;
import com.app.creaseart.interfaces.GlobalConstants;
import com.app.creaseart.interfaces.HeaderViewClickListener;
import com.app.creaseart.interfaces.JsonApiHelper;
import com.app.creaseart.interfaces.OnCustomItemClicListener;
import com.app.creaseart.utils.AppUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by admin on 06-01-2016.
 */
public class Fragment_Home extends BaseFragment implements ApiResponse, OnCustomItemClicListener {


    private Bundle b;
    private Activity context;
    public static Fragment_Home fragment_changePassword;
    private final String TAG = Fragment_Home.class.getSimpleName();
    View view_about;
    private ViewPager view_pager;
    private CustomPagerAdapter mCustomPagerAdapter;
    private ArrayList<String> mResources = new ArrayList<>();
    private ImageView[] dots;
    private int dotsCount;
    private LinearLayout pager_indicator;
    private android.support.v7.widget.CardView rlServiceRequest, rlOffers, rlHowItWorks, rlPackages, rlWallet, rlReedem;

    public static Fragment_Home getInstance() {
        if (fragment_changePassword == null)
            fragment_changePassword = new Fragment_Home();
        return fragment_changePassword;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this com.app.justclap.fragment

        view_about = inflater.inflate(R.layout.fragment_home, container, false);
        context = getActivity();

        b = getArguments();

        return view_about;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pager_indicator = (LinearLayout) view.findViewById(R.id.viewPagerCountDots);
        view_pager = (ViewPager) view.findViewById(R.id.view_pager);
        rlServiceRequest = (android.support.v7.widget.CardView) view.findViewById(R.id.rlServiceRequest);
        rlOffers = (android.support.v7.widget.CardView) view.findViewById(R.id.rlOffers);
        rlHowItWorks = (android.support.v7.widget.CardView) view.findViewById(R.id.rlHowItWorks);
        rlPackages = (android.support.v7.widget.CardView) view.findViewById(R.id.rlPackages);
        rlWallet = (android.support.v7.widget.CardView) view.findViewById(R.id.rlWallet);
        rlReedem = (android.support.v7.widget.CardView) view.findViewById(R.id.rlReedem);

        submitRequest();

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

    private void setUiPageViewController() {

        dotsCount = mCustomPagerAdapter.getCount();
        dots = new ImageView[dotsCount];

        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(getActivity());
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            //   params.gravity = Gravity.RIGHT;

            params.setMargins(4, 0, 4, 0);

            pager_indicator.addView(dots[i], params);
        }

        dots[0].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
    }


    private void setlistener() {

        view_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                try {
                    if (position >= dotsCount) {
                        position = (position % dotsCount);
                    }
                    Log.e("position", "*" + position);

                    for (int i = 0; i < dotsCount; i++) {
                        dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));
                    }

                    dots[position].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        rlWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        rlPackages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dashboard.getInstance().pushFragments(GlobalConstants.TAB_HOME_BAR, new Fragment_Package(), true);
            }
        });

    }

    private void submitRequest() {

        if (AppUtils.isNetworkAvailable(context)) {

            //   http://dev.stackmindz.com/creaseart/api/banner.php
            String url = JsonApiHelper.BASEURL + JsonApiHelper.BANNER;

            new CommonAsyncTaskHashmap(1, context, this).getqueryNoProgress(url);

        } else {
            Toast.makeText(context, context.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
        }
    }

    class CustomPagerAdapter extends PagerAdapter {

        Context mContext;
        LayoutInflater mLayoutInflater;

        public CustomPagerAdapter(Context context) {
            mContext = context;
            mLayoutInflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return mResources.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((RelativeLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View itemView = mLayoutInflater.inflate(R.layout.pager_item,
                    container, false);

            ImageView imageView = (ImageView) itemView
                    .findViewById(R.id.imageView);
            //  imageView.setImageResource(mResources.get(position));
            if (!mResources.get(position).equalsIgnoreCase("")) {
                Picasso.with(mContext).load(mResources.get(position)).into(imageView);
            }
            container.addView(itemView);

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((RelativeLayout) object);
        }
    }


    @Override
    public void onPostSuccess(int method, JSONObject response) {
        try {
            if (method == 1) {
                Dashboard.getInstance().setProgressLoader(false);
                JSONObject commandResult = response.getJSONObject("commandResult");

                if (commandResult.getString("success").equalsIgnoreCase("1")) {

                    JSONObject jsonObject = commandResult.getJSONObject("data");
                    JSONArray banners = jsonObject.getJSONArray("banners");

                    for (int i = 0; i < banners.length(); i++) {
                        JSONObject on = banners.getJSONObject(i);

                        mResources.add(on.getString("image"));
                    }
                    mCustomPagerAdapter = new CustomPagerAdapter(context);
                    view_pager.setAdapter(mCustomPagerAdapter);
                    pager_indicator = (LinearLayout) view_about.findViewById(R.id.viewPagerCountDots);
                    setUiPageViewController();
                    view_pager.setCurrentItem(0);
                    setlistener();
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

    @Override
    public void onItemClickListener(int position, int flag) {

    }
}

