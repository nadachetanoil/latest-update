package com.fracappzstudios.oilfieldnotifications;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.fracappzstudios.oilfieldnotifications.adapter.PeopelDirAdapter;
import com.fracappzstudios.oilfieldnotifications.adapter.SearchPeopleDirAdapter;
import com.fracappzstudios.oilfieldnotifications.database.DatabaseHandler;
import com.fracappzstudios.oilfieldnotifications.model.PeopelDirdetail;
import com.fracappzstudios.oilfieldnotifications.util.NetworkManager;
import com.fracappzstudios.oilfieldnotifications.util.Share;
import com.fracappzstudios.oilfieldnotifications.util.SharedPrefs;
import com.fracappzstudios.oilfieldnotifications.webservice.ApiClient;
import com.fracappzstudios.oilfieldnotifications.webservice.ApiService;
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

public class PeopleDirectoryActivity extends AppCompatActivity {

    private static final String TAG = PeopleDirectoryActivity.class.getSimpleName();

    @BindView(R.id.toolbar)Toolbar toolbar;
    @BindView(R.id.tv_title_people_directory)TextView tv_title_people_directory;
    @BindView(R.id.iv_back)ImageView iv_back;
    @BindView(R.id.et_search_people)EditText et_search_people;
    @BindView(R.id.tbl_rw_search_people)TableRow tbl_rw_search_people;

    @BindView(R.id.rv_people_list)RecyclerView rv_people_list;
    @BindView(R.id.rv_search_people)RecyclerView rv_search_people;
    @BindView(R.id.iv_clear_search)ImageView iv_clear_search;

    LinearLayoutManager linearLayoutManager;
    LinearLayoutManager searchLinearLayoutManager;
    PeopelDirAdapter peopelDirAdapter;
    SearchPeopleDirAdapter searchPeopleDirAdapter;

    String user_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people_directory);

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        initView();
    }

    private void initView()
    {
        try
        {
            user_id = SharedPrefs.getString(this,SharedPrefs.USER_ID);

            tv_title_people_directory.setTypeface(Share.Font.bold);
            linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            rv_people_list.setLayoutManager(linearLayoutManager);

            searchLinearLayoutManager = new LinearLayoutManager(this);
            searchLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            rv_search_people.setLayoutManager(searchLinearLayoutManager);

            if(NetworkManager.isInternetConnected(this))
                requestGetAllPeople();

            et_search_people.setTypeface(Share.Font.regular);
            et_search_people.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) { }
                @Override
                public void afterTextChanged(Editable s) {

                    if (s.length() == 0)
                        loadDBPeopelDir();
                    else
                    {
                        rv_search_people.setVisibility(View.VISIBLE);
                        rv_people_list.setVisibility(View.GONE);
                        if(isValidEmail(s.toString()) && NetworkManager.isInternetConnected(PeopleDirectoryActivity.this))
                        {
                            requestSearchPeople(s.toString());
                        }
                    }
                }
            });

            et_search_people.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
                {
                    Share.hideKeyboard(PeopleDirectoryActivity.this);
                    if(actionId == EditorInfo.IME_ACTION_SEARCH)
                    {
                        if(et_search_people.getText().toString().length() == 0)
                        {
                            Toast.makeText(PeopleDirectoryActivity.this,"Enter search text",Toast.LENGTH_SHORT).show();
                        }
                        else if(NetworkManager.isInternetConnected(PeopleDirectoryActivity.this))
                            requestSearchPeople(et_search_people.getText().toString().trim());
                    }
                    return false;
                }
            });

            tbl_rw_search_people.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    et_search_people.requestFocus();
                    InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.toggleSoftInputFromWindow( et_search_people.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public final static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }
    @OnClick(R.id.iv_back)
    public void onClickBack(View view)
    {
        finish();
    }

    @OnClick(R.id.iv_clear_search)
    public void onClickClearSearch(View view)
    {
        et_search_people.getText().clear();
        loadDBPeopelDir();
    }

    private void loadDBPeopelDir()
    {
        try {
            DatabaseHandler db = new DatabaseHandler(PeopleDirectoryActivity.this);
            db.open();
            List<PeopelDirdetail> peopelDirdetailList = db.getAllPeopleDir(user_id);
            peopelDirAdapter = new PeopelDirAdapter(PeopleDirectoryActivity.this,peopelDirdetailList);
            rv_people_list.setAdapter(peopelDirAdapter);
            peopelDirAdapter.setOnItemClickListener(new PeopelDirAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position, final PeopelDirdetail peopelDirdetail)
                {
                    final Dialog dialog = new Dialog(PeopleDirectoryActivity.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    dialog.setContentView(R.layout.dlg_logout);
                    dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    dialog.setCancelable(true);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();

                    TextView tv_msg = (TextView)dialog.findViewById(R.id.tv_msg);
                    TextView tv_msg_1 = (TextView)dialog.findViewById(R.id.tv_msg_1);
                    Button btn_yes = (Button)dialog.findViewById(R.id.btn_yes);
                    Button btn_no = (Button)dialog.findViewById(R.id.btn_no);

                    tv_msg.setText("Delete !");
                    tv_msg_1.setText("you want to delete "+peopelDirdetail.getFirstName()+" "+peopelDirdetail.getLastName()+" ?");
                    tv_msg.setTypeface(Share.Font.regular);
                    tv_msg_1.setTypeface(Share.Font.regular);
                    btn_yes.setTypeface(Share.Font.bold);
                    btn_no.setTypeface(Share.Font.bold);

                    btn_yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SharedPrefs.clearPrefs(PeopleDirectoryActivity.this);
                            dialog.cancel();

                            DatabaseHandler db = new DatabaseHandler(PeopleDirectoryActivity.this);
                            db.open();
                            db.removePeopleDir(peopelDirdetail.getId());
                            db.close();
                            loadDBPeopelDir();
                        }
                    });
                    btn_no.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.cancel();
                        }
                    });
                }
            });
            rv_search_people.setVisibility(View.GONE);
            rv_people_list.setVisibility(View.VISIBLE);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /*----------------------------------------- WebService ------------------------------*/
    private  void requestSearchPeople(String email)
    {
        try
        {
//            final ProgressDialog pd = ProgressDialog.show(this,"","Loading...",true,false);
//            pd.show();
            Call<PeopleDIRrespone> peopleDIRresponeCall = ApiClient.getClient().create(ApiService.class).getSearchPeopleDirList(user_id,email);
            peopleDIRresponeCall.enqueue(new Callback<PeopleDIRrespone>() {
                @Override
                public void onResponse(Call<PeopleDIRrespone> call, Response<PeopleDIRrespone> response) {
                    try
                    {
                        if(response.body().getPeopelDirUberAlpha().getStatus().equals("success"))
                        {
                            List<PeopelDirdetail> peopelDirdetailList = response.body().getPeopelDirUberAlpha().getPeopelDirdetails();

                            if(peopelDirdetailList.size() !=0)
                            {
                                DatabaseHandler db = new DatabaseHandler(PeopleDirectoryActivity.this);
                                db.open();
                                for (int index=0;index<peopelDirdetailList.size();index++)
                                {
                                    peopelDirdetailList.get(index).setUser_id(user_id);
                                    if(db.isExitsPeopleDir(user_id,peopelDirdetailList.get(index).getMemberId()))
                                    {
                                        peopelDirdetailList.get(index).setIs_delete("true");
                                    }
                                    else
                                    {
                                        peopelDirdetailList.get(index).setIs_delete("false");
                                    }
                                }
                                db.close();
                                searchPeopleDirAdapter = new SearchPeopleDirAdapter(PeopleDirectoryActivity.this,peopelDirdetailList);
                                rv_search_people.setAdapter(searchPeopleDirAdapter);

                                Log.e(TAG,"===============> getItemCount "+searchPeopleDirAdapter.getItemCount());

                                searchPeopleDirAdapter.setOnItemClickListener(new SearchPeopleDirAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(View view, int position, PeopelDirdetail peopelDirdetail)
                                    {
                                        et_search_people.getText().clear();
                                        Share.hideKeyboard(PeopleDirectoryActivity.this);
                                        loadDBPeopelDir();
                                    }
                                });
                            }
                        }
//                        pd.cancel();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<PeopleDIRrespone> call, Throwable t) {
                    t.printStackTrace();
//                    pd.cancel();
                }
            });
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
                                DatabaseHandler db = new DatabaseHandler(PeopleDirectoryActivity.this);
                                db.open();
                                for (int index=0;index<peopelDirdetailList.size();index++)
                                {
                                    peopelDirdetailList.get(index).setIs_delete("false");
                                    if(!user_id.equals(peopelDirdetailList.get(index).getMemberId()))
                                        db.addPeopelDir(peopelDirdetailList.get(index),user_id);
                                }
                                db.close();
                                loadDBPeopelDir();
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
}
