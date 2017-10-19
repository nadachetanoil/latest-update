package com.fracappzstudios.oilfieldnotifications.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.fracappzstudios.oilfieldnotifications.EventDetailsActivity;
import com.fracappzstudios.oilfieldnotifications.R;
import com.fracappzstudios.oilfieldnotifications.adapter.NotificationAdapter;
import com.fracappzstudios.oilfieldnotifications.model.Eventdetail;
import com.fracappzstudios.oilfieldnotifications.model.NotificationDetail;
import com.fracappzstudios.oilfieldnotifications.util.NetworkManager;
import com.fracappzstudios.oilfieldnotifications.util.Share;
import com.fracappzstudios.oilfieldnotifications.util.SharedPrefs;
import com.fracappzstudios.oilfieldnotifications.webservice.ApiClient;
import com.fracappzstudios.oilfieldnotifications.webservice.ApiService;
import com.fracappzstudios.oilfieldnotifications.webservice.response.DeleteRespone;
import com.fracappzstudios.oilfieldnotifications.webservice.response.GetNotificationResponse;
import com.google.gson.annotations.Expose;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Kathiriya Harshad on 6/22/2017.
 */

public class NotificationFragment extends Fragment
{
    private static final String TAG = NotificationFragment.class.getSimpleName();

    Activity activity;

    @BindView(R.id.rv_notification_feed)RecyclerView rv_notification_feed;

    LinearLayoutManager linearLayoutManager;
    NotificationAdapter notificationAdapter;
    MenuItem menu_action_delete;

    public static NotificationFragment newInstance(Activity activity) {
        Bundle args = new Bundle();
        NotificationFragment fragment = new NotificationFragment();
        fragment.setArguments(args);
        fragment.activity = activity;
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_notification,container,false);
        ButterKnife.bind(this,view);
        initView();
        return view;
    }
    private void initView()
    {
        try {
            linearLayoutManager = new LinearLayoutManager(getActivity());
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            rv_notification_feed.setLayoutManager(linearLayoutManager);
            if(NetworkManager.isInternetConnected(getActivity()))
                getAllNotificationRequest();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.notification, menu);

        try{
            menu_action_delete = menu.findItem(R.id.menu_action_delete);

            if(SharedPrefs.getString(getActivity(),SharedPrefs.SUPER_USER).equals("yes"))
                menu_action_delete.setVisible(true);
            else
                menu_action_delete.setVisible(false);
        }catch (Exception e){e.printStackTrace();}

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.menu_action_delete)
        {
            if(NetworkManager.isInternetConnected(getActivity()))
            {
                deleteAllNotification();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    /*--------------------------------------- Webservice ----------------------------------------*/

    private void getAllNotificationRequest()
    {

        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        pd.setMessage("Loading...");
        pd.show();

        Call<GetNotificationResponse> notificationResponseCall = ApiClient.getClient().create(ApiService.class).getNotification(SharedPrefs.getString(getActivity(),SharedPrefs.USER_ID));
        notificationResponseCall.enqueue(new Callback<GetNotificationResponse>() {
            @Override
            public void onResponse(Call<GetNotificationResponse> call, Response<GetNotificationResponse> response) {
                try {
                    if(response.body().getNotificationUberAlpha().getStatus().equals("success"))
                    {
                        final List<NotificationDetail> notificationDetails = response.body().getNotificationUberAlpha().getNotificationDetails();
                        notificationAdapter = new NotificationAdapter(getActivity(),notificationDetails);
                        rv_notification_feed.setAdapter(notificationAdapter);
                        if(notificationDetails.size() != 0)
                        {
                            notificationAdapter.setOnItemClickListener(new NotificationAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {
                                    try{
                                        Intent eventDetailsIntent = new Intent(getActivity(), EventDetailsActivity.class);
                                        eventDetailsIntent.putExtra("event_id",notificationDetails.get(position).getEvent_id());
                                        startActivity(eventDetailsIntent);
                                    }catch (Exception e){e.printStackTrace();}
                                }
                            });
                        }

                        if(menu_action_delete != null)
                            menu_action_delete.setVisible(true);
                    }
                    else if(response.body().getNotificationUberAlpha().getStatus().equals("fail"))
                    {
                        if(menu_action_delete != null)
                            menu_action_delete.setVisible(false);

                        final Dialog dialog = new Dialog(getActivity());
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        dialog.setContentView(R.layout.dlg_signup);
                        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                        dialog.setCancelable(true);
                        dialog.setCanceledOnTouchOutside(false);
                        TextView tv_msg = (TextView)dialog.findViewById(R.id.tv_msg);
                        tv_msg.setText("Error !");
                        TextView tv_msg_1 = (TextView)dialog.findViewById(R.id.tv_msg_1);
                        tv_msg_1.setText(response.body().getNotificationUberAlpha().getMessage());

                        tv_msg.setTypeface(Share.Font.regular);
                        tv_msg_1.setTypeface(Share.Font.regular);
                        Button btn_ok = (Button)dialog.findViewById(R.id.btn_ok);
                        btn_ok.setTypeface(Share.Font.regular);
                        btn_ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.cancel();
                            }
                        });
                        dialog.show();
                    }
                    pd.cancel();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<GetNotificationResponse> call, Throwable t) {
                t.printStackTrace();
                pd.cancel();
            }
        });
    }

    private void deleteAllNotification()
    {
        try {
            final ProgressDialog pd = new ProgressDialog(getActivity());
            pd.setIndeterminate(true);
            pd.setCancelable(false);
            pd.setMessage("Loading...");
            pd.show();

            Call<DeleteRespone> deleteAllNotificationResponeCall = ApiClient.getClient().create(ApiService.class).deleteAllNotification(SharedPrefs.getString(getActivity(),SharedPrefs.USER_ID));
            deleteAllNotificationResponeCall.enqueue(new Callback<DeleteRespone>() {
                @Override
                public void onResponse(Call<DeleteRespone> call, Response<DeleteRespone> response) {
                    pd.cancel();
                    try
                    {
                        final Dialog dialog = new Dialog(getActivity());
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        dialog.setContentView(R.layout.dlg_signup);
                        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                        dialog.setCancelable(true);
                        dialog.setCanceledOnTouchOutside(false);
                        TextView tv_msg = (TextView)dialog.findViewById(R.id.tv_msg);
                        TextView tv_msg_1 = (TextView)dialog.findViewById(R.id.tv_msg_1);
                        tv_msg.setTypeface(Share.Font.regular);
                        tv_msg_1.setTypeface(Share.Font.regular);
                        Button btn_ok = (Button)dialog.findViewById(R.id.btn_ok);
                        btn_ok.setTypeface(Share.Font.regular);
                        btn_ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.cancel();
                                rv_notification_feed.removeAllViews();
                            }
                        });
                        dialog.show();

                        if(response.body().getDeleteuberAlpha().getStatus().equals("success"))
                        {
                            rv_notification_feed.removeAllViews();
                            tv_msg.setText("Notification");
                            if(menu_action_delete != null)
                                menu_action_delete.setVisible(false);
                        }
                        else
                            tv_msg.setText("Error !");
                        tv_msg_1.setText(response.body().getDeleteuberAlpha().getMessage());

                    }
                    catch (Exception e){e.printStackTrace();}
                }

                @Override
                public void onFailure(Call<DeleteRespone> call, Throwable t) {
                    pd.cancel();
                }
            });
        }
        catch (Exception e){e.printStackTrace();}
    }
}
