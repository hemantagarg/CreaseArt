package com.app.creaseart.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.app.creaseart.R;
import com.app.creaseart.activities.Dashboard;
import com.app.creaseart.iclasses.HeaderViewManager;
import com.app.creaseart.interfaces.HeaderViewClickListener;
import com.app.creaseart.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;


public class PickUpBookingFragment extends BaseFragment implements View.OnClickListener {


    public static PickUpBookingFragment userBookingFragment;
    private Activity mActivity;
    private View view;
    private final String TAG = PickUpBookingFragment.class.getSimpleName();
    private TabLayout tabLayout;
    private ViewPager viewPager;

    public static PickUpBookingFragment getInstance() {
        if (userBookingFragment == null)
            userBookingFragment = new PickUpBookingFragment();
        return userBookingFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.vendor_booking_fragment, container, false);
        mActivity = getActivity();
        userBookingFragment = this;
        initViews();
        getBundle();
        manageHeaderView();
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
        return view;
    }


    private void getBundle() {

        Bundle bundle = getArguments();
        if (bundle != null) {

            //      vendorId = bundle.getString(AppConstant.VENDORID);

        }
    }


    private void initViews() {
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        tabLayout = (TabLayout) view.findViewById(R.id.tablayout);

    }

    private void setupTabIcons() {

        tabLayout.getTabAt(0).setText("New Order");
        tabLayout.getTabAt(1).setText("Ongoing Order");

        tabLayout.setTabTextColors(getResources().getColor(R.color.textcolordark), getResources().getColor(R.color.appcolor));

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());

        FragementPickUpNewBooking tab2 = new FragementPickUpNewBooking();
        adapter.addFrag(tab2, "New Order");

        FragementPickUpConvertedBooking tab1 = new FragementPickUpConvertedBooking();
        adapter.addFrag(tab1, "Ongoing Order");

        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
/*
        if (UserDashboard.currentFragment instanceof UserBookingFragment) {

        }*/
    }


    /*******************************************************************
     * Function name - manageHeaderView
     * Description - manage the initialization, visibility and click
     * listener of view fields on Header view
     *******************************************************************/
    private void manageHeaderView() {
        Dashboard.getInstance().manageHeaderVisibitlity(false);
        HeaderViewManager.getInstance().InitializeHeaderView(null, view, manageHeaderClick());
        HeaderViewManager.getInstance().setHeading(true, mActivity.getResources().getString(R.string.bokings));
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
                mActivity.onBackPressed();
            }

            @Override
            public void onClickOfHeaderRightView() {
                Toast.makeText(mActivity, "Coming Soon", Toast.LENGTH_SHORT).show();
            }
        };
    }


    protected void setFragment(Fragment fragment) {
        // UserDashboard.getInstance().pushFragments(GlobalConstants.TAB_HOME_BAR, fragment, true);
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {

        }

    }

}
