package com.fracappzstudios.oilfieldnotifications.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fracappzstudios.oilfieldnotifications.InvitePeopleActivity;
import com.fracappzstudios.oilfieldnotifications.MyGroupActivity;
import com.fracappzstudios.oilfieldnotifications.NotificationFeedActivity;
import com.fracappzstudios.oilfieldnotifications.PeopleDirectoryActivity;
import com.fracappzstudios.oilfieldnotifications.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Harshad Kathiriya on 6/19/2017.
 */

public class HomeFragment extends Fragment
{
    private static final String TAG = HomeFragment.class.getSimpleName();

    Activity  activity;

    public static HomeFragment newInstance(Activity activity) {
        Bundle args = new Bundle();
        HomeFragment fragment = new HomeFragment();
        fragment.activity = activity;
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);
        ButterKnife.bind(this,view);
        return view;
    }


    @OnClick(R.id.iv_mygroup)
    public void onClickMyGroup(View view)
    {
        Intent myGroupIntent = new Intent(getActivity(), MyGroupActivity.class);
        getActivity().startActivity(myGroupIntent);
    }

    @OnClick(R.id.iv_notification_feed)
    public void onClickNotificationFeed(View view)
    {
        Intent notificationFeedsIntent = new Intent(getActivity(), NotificationFeedActivity.class);
        getActivity().startActivity(notificationFeedsIntent);

    }

    @OnClick(R.id.iv_invite_people)
    public void onClickInvitePeople(View view)
    {
        Intent invitePeopleIntent = new Intent(getActivity(), InvitePeopleActivity.class);
        getActivity().startActivity(invitePeopleIntent);
    }

    @OnClick(R.id.iv_people_directory)
    public void onClickInviteDirectory(View view)
    {
        Intent peopleDirectoryIntent = new Intent(getActivity(), PeopleDirectoryActivity.class);
        getActivity().startActivity(peopleDirectoryIntent);
    }


}
