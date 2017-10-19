package com.fracappzstudios.oilfieldnotifications;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fracappzstudios.oilfieldnotifications.adapter.SelectMemberAdapter;
import com.fracappzstudios.oilfieldnotifications.database.DatabaseHandler;
import com.fracappzstudios.oilfieldnotifications.model.PeopelDirdetail;
import com.fracappzstudios.oilfieldnotifications.util.Share;
import com.fracappzstudios.oilfieldnotifications.util.SharedPrefs;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
/**
 * Created by Kathiriya Harshad on 6/28/2017.
 */

public class SelectMemberActivity extends AppCompatActivity {

    private static final String TAG = SelectMemberActivity.class.getSimpleName();

    @BindView(R.id.iv_back)ImageView iv_back;
    @BindView(R.id.tv_done)TextView tv_done;
    @BindView(R.id.rv_select_member)RecyclerView rv_select_member;
    @BindView(R.id.tv_title_select_member)TextView tv_title_select_member;

    LinearLayoutManager linearLayoutManager;
    SelectMemberAdapter selectMemberAdapter;
    List<PeopelDirdetail> peopelDirs = new ArrayList<>();

    String is_update="",user_id="";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_member);
        ButterKnife.bind(this);
        initView();
        setFont();
    }

    private void setFont()
    {
        tv_title_select_member.setTypeface(Share.Font.bold);
        tv_done.setTypeface(Share.Font.thin_bold);
    }

    private void initView()
    {
        try
        {
            linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            rv_select_member.setLayoutManager(linearLayoutManager);

            user_id = SharedPrefs.getString(this,SharedPrefs.USER_ID);
            peopelDirs.clear();

            if(getIntent().getExtras() != null && getIntent().getExtras().containsKey("eventMember"))
            {
                peopelDirs = Share.groupdetail.getPeopelDirdetail();
            }
            else
            {
                if(getIntent().getExtras() != null && getIntent().getExtras().containsKey("is_update"))
                {
                    is_update = getIntent().getExtras().getString("is_update");
                }
                DatabaseHandler db = new DatabaseHandler(this);
                db.open();
                Cursor cursor = db.getAllPeople(SharedPrefs.getString(this,SharedPrefs.USER_ID));
                if (cursor.getCount()>0)
                {
                    while (cursor.moveToNext())
                    {
                        PeopelDirdetail peopelDirdetail = new PeopelDirdetail();
                        peopelDirdetail.setId(""+cursor.getInt(0));
                        peopelDirdetail.setUser_id(""+cursor.getString(1));
                        peopelDirdetail.setMemberId(""+cursor.getString(2));
                        peopelDirdetail.setFirstName(""+cursor.getString(3));
                        peopelDirdetail.setLastName(""+cursor.getString(4));
                        peopelDirdetail.setEmail(""+cursor.getString(5));
                        peopelDirdetail.setProfileImage(cursor.getString(6));
                        peopelDirdetail.setPhone(cursor.getString(7));
                        peopelDirdetail.setCompanyName(cursor.getString(8));
                        peopelDirdetail.setIs_delete(cursor.getString(9));

                        if(Share.selectedMember.containsKey(peopelDirdetail.getMemberId()) && Share.selectedMember.get(peopelDirdetail.getMemberId()))
                        {
                            peopelDirdetail.setIs_select(true);
                        }
                        else
                        {
                            peopelDirdetail.setIs_select(false);
                        }

                        if(!peopelDirdetail.getMemberId().equals(user_id))
                            peopelDirs.add(peopelDirdetail);
                        Log.e(TAG,"initView "+peopelDirdetail.toString());
                    }
                }
                cursor.close();
                db.close();
            }


            selectMemberAdapter = new SelectMemberAdapter(this,peopelDirs,Share.selectedMember);
            rv_select_member.setAdapter(selectMemberAdapter);
        }
        catch (Exception e)
        {
            e.getMessage();
        }
    }

    @OnClick(R.id.iv_back)
    public void onClickMyGroup(View view)
    {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent();
        setResult(RESULT_OK,intent);
        finish();
    }

    @OnClick(R.id.tv_done)
    public void onClickDone(View view)
    {
       try
       {
           Intent intent=new Intent();
           if(selectMemberAdapter != null)
           {
               intent.putExtra("user_list",""+selectMemberAdapter.getSelectedMembers());
               Share.selectedMember = selectMemberAdapter.getItemList();
               Log.e(TAG,"selectMember => "+selectMemberAdapter.getItemList());
           }
           setResult(RESULT_OK,intent);
           finish();
       }
       catch (Exception e)
       {
           e.printStackTrace();
       }
    }
}
