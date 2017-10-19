package com.fracappzstudios.oilfieldnotifications.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;

import com.fracappzstudios.oilfieldnotifications.ProfileActivity;
import com.fracappzstudios.oilfieldnotifications.R;
import com.fracappzstudios.oilfieldnotifications.TutorialActivity;
import com.fracappzstudios.oilfieldnotifications.util.Share;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Kathiriya Harshad on 6/20/2017.
 */

public class SettingsFragment extends Fragment
{
    private static final String TAG = SettingsFragment.class.getSimpleName();

    @BindView(R.id.tbl_rw_ed_notification) TableRow tbl_rw_ed_notification;
    @BindView(R.id.tbl_rw_user_profile) TableRow tbl_rw_user_profile;
    @BindView(R.id.tbl_rw_tutorial) TableRow tbl_rw_tutorial;

    @BindView(R.id.tv_enable_disable_noti)TextView tv_enable_disable_noti;
    @BindView(R.id.tv_user_profile)TextView tv_user_profile;
    @BindView(R.id.tv_tutorial)TextView tv_tutorial;


    Activity activity;

    public static SettingsFragment newInstance(Activity activity) {
        Bundle args = new Bundle();
        SettingsFragment fragment = new SettingsFragment();
        fragment.setArguments(args);
        fragment.activity = activity;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings,container,false);
        ButterKnife.bind(this,view);
        setFont();
        return view;
    }


    private void setFont()
    {
        tv_enable_disable_noti.setTypeface(Share.Font.regular);
        tv_tutorial.setTypeface(Share.Font.regular);
        tv_user_profile.setTypeface(Share.Font.regular);
    }

    @OnClick(R.id.tbl_rw_ed_notification)
    public void onClickEnableDisableNotification(View view)
    {
        try
        {
            Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
            startActivity(intent);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.tbl_rw_user_profile)
    public void onClickUserProfile(View view)
    {
        try
        {
            Intent profileIntent = new Intent(activity, ProfileActivity.class);
            activity.startActivity(profileIntent);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.tbl_rw_tutorial)
    public void onClickTutorial(View view)
    {
        try{
            Intent tutorialIntent = new Intent(getActivity(), TutorialActivity.class);
            startActivity(tutorialIntent);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
