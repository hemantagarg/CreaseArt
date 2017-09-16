package com.app.creaseart.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.lang.reflect.Field;

/****************************************************************
 * BaseFragment.java

 ****************************************************************/
public class BaseFragment extends Fragment {
    // instance of main tab activity to be used globally into the inner classes
    // public static AppMainTabActivity mActivity;
//    public static MainSliderView mActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public boolean onBackPressed() {
        return false;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    }

    @Override
    public void onDetach() {
        super.onDetach();
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
