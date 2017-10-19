package com.fracappzstudios.oilfieldnotifications;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fracappzstudios.oilfieldnotifications.adapter.NotificationFeedAdapter;
import com.fracappzstudios.oilfieldnotifications.model.Eventdetail;
import com.fracappzstudios.oilfieldnotifications.util.NetworkManager;
import com.fracappzstudios.oilfieldnotifications.util.Share;
import com.fracappzstudios.oilfieldnotifications.util.SharedPrefs;
import com.fracappzstudios.oilfieldnotifications.webservice.ApiClient;
import com.fracappzstudios.oilfieldnotifications.webservice.ApiService;
import com.fracappzstudios.oilfieldnotifications.webservice.response.EventListRrespone;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Kathiriya Harshad on 6/28/2017.
 */

public class NotificationFeedActivity extends AppCompatActivity {

    public static final String TAG = NotificationFeedActivity.class.getSimpleName();
    @BindView(R.id.tv_title_notificationfeeds)TextView tv_title_notificationfeeds;
    @BindView(R.id.iv_back)ImageView iv_back;
    @BindView(R.id.rv_notification_feed)RecyclerView rv_notification_feed;

    NotificationFeedAdapter notificationFeedAdapter;
    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_feed);

        ButterKnife.bind(this);
        initView();
    }

    private void initView()
    {
        try
        {
            tv_title_notificationfeeds.setTypeface(Share.Font.bold);

            linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            rv_notification_feed.setLayoutManager(linearLayoutManager);

            if(NetworkManager.isInternetConnected(this))
                getAllEvents();
        }
        catch (Exception e){e.printStackTrace();}
    }

    @OnClick(R.id.iv_back)
    public void onClickMyGroup(View view)
    {
        finish();
    }





    /*---------------------------------- Web service --------------------------------*/

    private void getAllEvents()
    {
        try {
            final ProgressDialog pd = ProgressDialog.show(this,"","Loading...",true,false);
            pd.show();

            Call<EventListRrespone> eventListRresponeCall = ApiClient.getClient().create(ApiService.class).getAllEvent(SharedPrefs.getString(this,SharedPrefs.USER_ID));
            eventListRresponeCall.enqueue(new Callback<EventListRrespone>() {
                @Override
                public void onResponse(Call<EventListRrespone> call, Response<EventListRrespone> response) {
                    try
                    {
                        pd.cancel();
                        if(response.body().getEventuberAlpha().getStatus().equals("success"))
                        {
                            List<Eventdetail> eventdetailList = response.body().getEventuberAlpha().getEventdetails();
                            notificationFeedAdapter = new NotificationFeedAdapter(NotificationFeedActivity.this,eventdetailList);
                            rv_notification_feed.setAdapter(notificationFeedAdapter);
                        }
                    }
                    catch (Exception e){e.printStackTrace();}
                }

                @Override
                public void onFailure(Call<EventListRrespone> call, Throwable t) {
                    pd.cancel();
                    t.printStackTrace();
                }
            });

        }
        catch (Exception e)
        {e.printStackTrace();}
    }

}
