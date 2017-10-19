package com.fracappzstudios.oilfieldnotifications;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fracappzstudios.oilfieldnotifications.adapter.EventListAdapter;
import com.fracappzstudios.oilfieldnotifications.model.Eventdetail;
import com.fracappzstudios.oilfieldnotifications.model.Groupdetail;
import com.fracappzstudios.oilfieldnotifications.util.NetworkManager;
import com.fracappzstudios.oilfieldnotifications.util.Share;
import com.fracappzstudios.oilfieldnotifications.util.SharedPrefs;
import com.fracappzstudios.oilfieldnotifications.util.Urls;
import com.fracappzstudios.oilfieldnotifications.webservice.ApiClient;
import com.fracappzstudios.oilfieldnotifications.webservice.ApiService;
import com.fracappzstudios.oilfieldnotifications.webservice.response.DeleteRespone;
import com.fracappzstudios.oilfieldnotifications.webservice.response.EventListRrespone;
import com.squareup.picasso.Picasso;

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


public class GroupViewActivity extends AppCompatActivity {

    private static final String TAG = GroupViewActivity.class.getSimpleName();

    @BindView(R.id.toolbar)Toolbar toolbar;
    @BindView(R.id.rv_events_list)RecyclerView rv_events_list;
    @BindView(R.id.ll_empty)LinearLayout ll_empty;
    @BindView(R.id.img_group)ImageView iv_group;
    @BindView(R.id.tv_group_name)TextView tv_group_name;

    LinearLayoutManager linearLayoutManager;
    EventListAdapter eventListAdapter;

    String user_id="",group_id="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_view);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initView();
    }

    private void initView()
    {
        try {
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            Share.eventdetail = null;
            user_id = SharedPrefs.getString(this,SharedPrefs.USER_ID);

            linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            rv_events_list.setLayoutManager(linearLayoutManager);

            if(Share.groupdetail != null)
            {
                group_id = Share.groupdetail.getGroupId();
                tv_group_name.setText(""+Share.groupdetail.getGroupName());
                tv_group_name.setTypeface(Share.Font.bold);
                Picasso.with(this)
                        .load(Urls.GROUP_IMAGE_URL+Share.groupdetail.getGroupImage())
                        .placeholder(R.drawable.ic_group)
                        .error(R.drawable.ic_group)
                        .into(iv_group);

            }
            if(!group_id.equals("") && NetworkManager.isInternetConnected(this))
                getAllEventListRequest();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.iv_back)
    public void onClickMyGroup(View view)
    {
        finish();
    }

    @OnClick(R.id.iv_create_event)
    public void onClickCreateEvent(View view)
    {
        try
        {
            final Dialog createEventDialog = new Dialog(this);
            createEventDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            createEventDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            createEventDialog.setContentView(R.layout.dlg_create_event);
            createEventDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            createEventDialog.setCancelable(true);
            createEventDialog.setCanceledOnTouchOutside(false);
            createEventDialog.show();

            Button btn_cancel = (Button)createEventDialog.findViewById(R.id.btn_cancel);
            btn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createEventDialog.cancel();
                }
            });
            TextView tv_group_details = (TextView)createEventDialog.findViewById(R.id.tv_group_details);
            TextView tv_create_event = (TextView)createEventDialog.findViewById(R.id.tv_create_event);

            btn_cancel.setTypeface(Share.Font.bold);
            tv_create_event.setTypeface(Share.Font.regular);
            tv_group_details.setTypeface(Share.Font.regular);

            tv_create_event.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createEventDialog.cancel();
                    Intent createEventIntent = new Intent(GroupViewActivity.this,CreateEventActivity.class);
                    startActivity(createEventIntent);
                }
            });

            tv_group_details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createEventDialog.cancel();
                    Intent groupDetailsIntent = new Intent(GroupViewActivity.this,GroupDetailActivity.class);
                    startActivity(groupDetailsIntent);
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /*-----------------------------------webserices-----------------------*/
    private void getAllEventListRequest()
    {
        try
        {
            final ProgressDialog pd = ProgressDialog.show(this,"","Loadig...",true,false);
            pd.show();
            Call<EventListRrespone> eventListRresponeCall = ApiClient.getClient().create(ApiService.class).getEvent(user_id,group_id);
            eventListRresponeCall.enqueue(new Callback<EventListRrespone>() {
                @Override
                public void onResponse(Call<EventListRrespone> call, Response<EventListRrespone> response) {

                    if(response.body().getEventuberAlpha().getStatus().equals("success"))
                    {
                        ll_empty.setVisibility(View.GONE);
                        rv_events_list.setVisibility(View.VISIBLE);
                        Log.e(TAG,"===> "+response.body().getEventuberAlpha().getEventdetails().toString());
                        final List<Eventdetail> eventdetailList = response.body().getEventuberAlpha().getEventdetails();
                        eventListAdapter = new EventListAdapter(GroupViewActivity.this,eventdetailList);
                        rv_events_list.setAdapter(eventListAdapter);
                        if(SharedPrefs.getString(GroupViewActivity.this,SharedPrefs.SUPER_USER).toLowerCase().equals("no"))
                            eventListAdapter.setIs_visible_delete(false);
                        else
                        {
                            eventListAdapter.setIs_visible_delete(true);
                            eventListAdapter.setmListener(new EventListAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {
                                    if(NetworkManager.isInternetConnected(GroupViewActivity.this))
                                        deleteEventRequest(eventdetailList.get(position).getEventId(),position);
                                }
                            });
                        }
                    }
                    else
                    {
                        ll_empty.setVisibility(View.VISIBLE);
                        rv_events_list.setVisibility(View.GONE);
                    }

                    pd.cancel();
                }

                @Override
                public void onFailure(Call<EventListRrespone> call, Throwable t) {
                    t.printStackTrace();
                    pd.cancel();
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void deleteEventRequest(String event_id, final int position)
    {
        try
        {
            final ProgressDialog pd = ProgressDialog.show(this,"","Loadig...",true,false);
            pd.show();

            Call<DeleteRespone> deleteEventResponeCall = ApiClient.getClient().create(ApiService.class).delete_event(SharedPrefs.getString(this,SharedPrefs.USER_ID),event_id);
            deleteEventResponeCall.enqueue(new Callback<DeleteRespone>() {
                @Override
                public void onResponse(Call<DeleteRespone> call, Response<DeleteRespone> response) {
                    try{
                        pd.cancel();
                        if(response.body().getDeleteuberAlpha().getStatus().equals("success"))
                        {
                            eventListAdapter.remove(position);
                        }
                        else
                            Toast.makeText(GroupViewActivity.this,""+response.body().getDeleteuberAlpha().getMessage(),Toast.LENGTH_SHORT).show();
                    }catch (Exception e){e.printStackTrace();}
                }

                @Override
                public void onFailure(Call<DeleteRespone> call, Throwable t) {
                    pd.cancel();
                    t.printStackTrace();
                }
            });
        }
        catch (Exception e){e.printStackTrace();}
    }
}
