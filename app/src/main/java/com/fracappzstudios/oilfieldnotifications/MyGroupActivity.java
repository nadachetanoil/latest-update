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
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.fracappzstudios.oilfieldnotifications.adapter.GroupListAdapter;
import com.fracappzstudios.oilfieldnotifications.database.DatabaseHandler;
import com.fracappzstudios.oilfieldnotifications.model.Groupdetail;
import com.fracappzstudios.oilfieldnotifications.model.PeopelDirdetail;
import com.fracappzstudios.oilfieldnotifications.util.NetworkManager;
import com.fracappzstudios.oilfieldnotifications.util.Share;
import com.fracappzstudios.oilfieldnotifications.util.SharedPrefs;
import com.fracappzstudios.oilfieldnotifications.webservice.ApiClient;
import com.fracappzstudios.oilfieldnotifications.webservice.ApiService;
import com.fracappzstudios.oilfieldnotifications.webservice.response.DeleteRespone;
import com.fracappzstudios.oilfieldnotifications.webservice.response.GroupMemberRrespone;
import com.fracappzstudios.oilfieldnotifications.webservice.response.PeopleDIRrespone;

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


public class MyGroupActivity extends AppCompatActivity {

    public static final String TAG = MyGroupActivity.class.getSimpleName();

    @BindView(R.id.tv_title_mygroup)TextView tv_title_mygroup;
    @BindView(R.id.iv_back)ImageView iv_back;
    @BindView(R.id.tv_add_group)TextView tv_add_group;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.rv_group_list)RecyclerView rv_group_list;

    LinearLayoutManager linearLayoutManager;
    GroupListAdapter groupListAdapter;
    String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_group);

        ButterKnife.bind(this);
        initView();
    }

    private void initView()
    {
        try {
            tv_title_mygroup.setTypeface(Share.Font.bold);
            setSupportActionBar(toolbar);

            user_id = SharedPrefs.getString(this,SharedPrefs.USER_ID);

            if(SharedPrefs.getString(this, SharedPrefs.SUPER_USER).equals("yes"))
                tv_add_group.setVisibility(View.VISIBLE);
            else
                tv_add_group.setVisibility(View.GONE);


            linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            rv_group_list.setLayoutManager(linearLayoutManager);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(NetworkManager.isInternetConnected(this))
            requestGetAllGroupDetails();
    }

    @OnClick(R.id.iv_back)
    public void onClickMyGroup(View view)
    {
        finish();
    }

    public void onClickAddGroup(View view)
    {
        try
        {
            Share.selectedMember.clear();
            Intent newGroupIntent = new Intent(this,NewGroupActivity.class);
            startActivity(newGroupIntent);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /*--------------------------------------- WebService ---------------------------------*/
    private void requestGetAllGroupDetails()
    {
        try
        {
            final ProgressDialog pd = ProgressDialog.show(this,"","Loading...",true,false);
            pd.show();

            Call<GroupMemberRrespone> groupMemberRresponeCall = ApiClient.getClient().create(ApiService.class).getAllGroupsList(user_id);
            groupMemberRresponeCall.enqueue(new Callback<GroupMemberRrespone>() {
                @Override
                public void onResponse(Call<GroupMemberRrespone> call, Response<GroupMemberRrespone> response) {

                    try
                    {
                        if(response.body().getGroupUberAlpha().getStatus().equals("success"))
                        {
                            final List<Groupdetail> groupdetailList = response.body().getGroupUberAlpha().getGroupdetails();
                             groupListAdapter = new GroupListAdapter(MyGroupActivity.this,groupdetailList);
                            rv_group_list.setAdapter(groupListAdapter);

                            if(SharedPrefs.getString(MyGroupActivity.this,SharedPrefs.SUPER_USER).toLowerCase().equals("no"))
                                groupListAdapter.setIs_visible_delete(false);
                            else
                            {
                                groupListAdapter.setIs_visible_delete(true);
                                groupListAdapter.setmListener(new GroupListAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(View view, final int position) {
                                        try{
                                            final Dialog dialog = new Dialog(MyGroupActivity.this);
                                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                                            dialog.setContentView(R.layout.dlg_group);
                                            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                                            dialog.setCancelable(true);
                                            dialog.setCanceledOnTouchOutside(false);
                                            dialog.show();

                                            Button btn_ok = (Button)dialog.findViewById(R.id.btn_ok);
                                            Button btn_cancel = (Button)dialog.findViewById(R.id.btn_cancel);

                                            btn_ok.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    dialog.cancel();
                                                    Log.e(TAG,"getGroupId  :-> "+groupdetailList.get(position).getGroupId());
                                                    deleteGroupRequest(groupdetailList.get(position).getGroupId(),position);
                                                }
                                            });
                                            btn_cancel.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    dialog.cancel();
                                                }
                                            });
                                        }catch (Exception e){e.printStackTrace();}
                                    }
                                });
                            }

                            Log.e(TAG,"groupdetailList :-> "+rv_group_list.getAdapter().getItemCount());




                            if(groupdetailList!= null && groupdetailList.size() > 0)
                            {
                                DatabaseHandler db = new DatabaseHandler(MyGroupActivity.this);
                                db.open();
                                db.removeTempPeopleDir(user_id);
                                for (int index=0;index<groupdetailList.size();index++)
                                {
                                    List<PeopelDirdetail> tempPeopelDirdetailList = groupdetailList.get(index).getPeopelDirdetail();
                                    for (PeopelDirdetail peopelDirdetail:tempPeopelDirdetailList) {
                                        peopelDirdetail.setIs_delete(Share.TEMP_PEOPLEDIR_IS_DELETE);
                                        if(!user_id.equals(peopelDirdetail.getMemberId()))
                                            db.addPeopelDir(peopelDirdetail,user_id);
                                    }
                                }
                                db.close();
                            }
                        }
                        else
                        {
                            final Dialog dialog = new Dialog(MyGroupActivity.this);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                            dialog.setContentView(R.layout.dlg_signup);
                            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                            dialog.setCancelable(true);
                            dialog.setCanceledOnTouchOutside(false);

                            ImageView fab = (ImageView)dialog.findViewById(R.id.fab);
                            fab.setImageResource(R.drawable.ic_hand_circle);
                            Button btn_ok = (Button)dialog.findViewById(R.id.btn_ok);
                            btn_ok.setTypeface(Share.Font.regular);
                            btn_ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.cancel();
                                }
                            });

                            TextView tv_msg = (TextView)dialog.findViewById(R.id.tv_msg);
                            tv_msg.setVisibility(View.GONE);
                            TextView tv_msg_1 = (TextView)dialog.findViewById(R.id.tv_msg_1);
                            tv_msg_1.setText(""+response.body().getGroupUberAlpha().getMessage());

                            tv_msg.setTypeface(Share.Font.regular);
                            tv_msg_1.setTypeface(Share.Font.regular);
                            dialog.show();
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    finally {
                        pd.cancel();
                    }
                }

                @Override
                public void onFailure(Call<GroupMemberRrespone> call, Throwable t) {
                    t.printStackTrace();
                    pd.cancel();
                }
            });

            if(NetworkManager.isInternetConnected(this))
                requestGetAllPeople();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public  void requestGetAllPeople()
    {
        try
        {
            final ProgressDialog pd = ProgressDialog.show(this,"","Loading...",true,false);
            pd.show();
            Call<PeopleDIRrespone> peopleDIRresponeCall = ApiClient.getClient().create(ApiService.class).getPeopleDirList(user_id);
            peopleDIRresponeCall.enqueue(new Callback<PeopleDIRrespone>() {
                @Override
                public void onResponse(Call<PeopleDIRrespone> call, Response<PeopleDIRrespone> response) {
                    try
                    {
                        if(response.body().getPeopelDirUberAlpha().getStatus().equals("success"))
                        {
                            List<PeopelDirdetail> peopelDirdetailList = response.body().getPeopelDirUberAlpha().getPeopelDirdetails();

                            if(peopelDirdetailList.size() != 0)
                            {
                                DatabaseHandler db = new DatabaseHandler(MyGroupActivity.this);
                                db.open();
                                for (int index=0;index<peopelDirdetailList.size();index++)
                                {
                                    peopelDirdetailList.get(index).setIs_delete("false");
                                    db.addPeopelDir(peopelDirdetailList.get(index),user_id);
                                }
                                db.close();
                            }
                        }
                        pd.cancel();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<PeopleDIRrespone> call, Throwable t) {
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

    public void deleteGroupRequest(String group_id,final int position)
    {
        try
        {
            final ProgressDialog pd = ProgressDialog.show(this,"","Loading...",true,false);
            pd.show();
            Call<DeleteRespone> deleteGroupResponeCall = ApiClient.getClient().create(ApiService.class).delete_group(SharedPrefs.getString(this,SharedPrefs.USER_ID),group_id);
            deleteGroupResponeCall.enqueue(new Callback<DeleteRespone>() {
                @Override
                public void onResponse(Call<DeleteRespone> call, Response<DeleteRespone> response) {
                    try
                    {
                        pd.cancel();
                        final Dialog dialog = new Dialog(MyGroupActivity.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        dialog.setContentView(R.layout.dlg_signup);
                        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                        dialog.setCancelable(true);
                        dialog.setCanceledOnTouchOutside(false);
                        ImageView fab = (ImageView)dialog.findViewById(R.id.fab);
                        Button btn_ok = (Button)dialog.findViewById(R.id.btn_ok);
                        TextView tv_msg = (TextView)dialog.findViewById(R.id.tv_msg);
                        TextView tv_msg_1 = (TextView)dialog.findViewById(R.id.tv_msg_1);
                        tv_msg.setTypeface(Share.Font.regular);
                        tv_msg_1.setTypeface(Share.Font.regular);

                        if(response.body().getDeleteuberAlpha().getStatus().equals("success"))
                        {
                            groupListAdapter.remove(position);
                            fab.setImageResource(R.drawable.ic_right_circle);
                            btn_ok.setTypeface(Share.Font.regular);
                            btn_ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.cancel();
                                }
                            });
                            tv_msg.setText("Success!");
                            tv_msg_1.setText(""+response.body().getDeleteuberAlpha().getMessage());
                            dialog.show();
                        }
                        else {
                            fab.setImageResource(R.drawable.ic_hand_circle);
                            btn_ok.setTypeface(Share.Font.regular);
                            btn_ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.cancel();
                                }
                            });
                            tv_msg.setVisibility(View.GONE);
                            tv_msg_1.setText(""+response.body().getDeleteuberAlpha().getMessage());
                            dialog.show();
                        }
                    }
                    catch (Exception e){e.printStackTrace();}
                }

                @Override
                public void onFailure(Call<DeleteRespone> call, Throwable t) {
                    t.printStackTrace();
                    pd.cancel();
                }
            });
        }
        catch (Exception e){e.printStackTrace();}
    }
}
