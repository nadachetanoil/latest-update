package com.fracappzstudios.oilfieldnotifications;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fracappzstudios.oilfieldnotifications.fragment.ContactUsFragment;
import com.fracappzstudios.oilfieldnotifications.fragment.HomeFragment;
import com.fracappzstudios.oilfieldnotifications.fragment.NotificationFragment;
import com.fracappzstudios.oilfieldnotifications.fragment.SettingsFragment;
import com.fracappzstudios.oilfieldnotifications.fragment.TermsAndConditionsFragment;
import com.fracappzstudios.oilfieldnotifications.util.NetworkManager;
import com.fracappzstudios.oilfieldnotifications.util.Share;
import com.fracappzstudios.oilfieldnotifications.util.SharedPrefs;
import com.fracappzstudios.oilfieldnotifications.webservice.ApiClient;
import com.fracappzstudios.oilfieldnotifications.webservice.ApiService;
import com.fracappzstudios.oilfieldnotifications.webservice.response.DeleteRespone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Kathiriya Harshad on 6/28/2017.
 */

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static  final String TAG = HomeActivity.class.getSimpleName();
    public static final String mBroadcastNotificationAction = "com.fracappzstudios.oilfieldnotifications.NotificationBroadcast";

    @BindView(R.id.rv_side_menu) RecyclerView rv_side_menu;
    @BindView(R.id.ll_notification) LinearLayout ll_notification;
    @BindView(R.id.tv_title_home) TextView tv_title_home;
    @BindView(R.id.cl_main) CoordinatorLayout cl_main;

    SideMenuAdapter sideMenuAdapter;
    String menu_title[] = new String[]{"Home","Contact Us","Terms and Conditions","Settings","Log Out"};
    int menu_icons[] = new int[]{R.drawable.ic_home,
                                 R.drawable.ic_contact_us,
                                 R.drawable.ic_terms_conditions,
                                 R.drawable.ic_settings,
                                 R.drawable.ic_log_out};

    private IntentFilter mIntentFilter;
    NotificationReceiver notificationReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        try
        {
            notificationReceiver = new NotificationReceiver();

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("");
            ButterKnife.bind(this);

            setSupportActionBar(toolbar);

            final DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
                @Override
                public void onDrawerSlide(View drawerView, float slideOffset) {
                    super.onDrawerSlide(drawerView, slideOffset);
                    cl_main.setTranslationX(slideOffset * drawerView.getWidth());
                    mDrawerLayout.bringChildToFront(drawerView);
                    mDrawerLayout.requestLayout();
                    //below line used to remove shadow of drawer
                    mDrawerLayout.setScrimColor(Color.TRANSPARENT);
                }//this method helps you to aside menu drawer
            };

            Drawable drawable = ResourcesCompat.getDrawable(getResources(),   R.drawable.ic_arrow_right,getTheme());
            drawerToggle.setHomeAsUpIndicator(drawable);
            mDrawerLayout.setDrawerListener(drawerToggle);
            drawerToggle.syncState();

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_menu);

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

            if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP)
            {
                View headerView = navigationView.getHeaderView(0);
                View v_line_nav = (View)headerView.findViewById(R.id.v_line_nav);
                v_line_nav.setVisibility(View.GONE);
            }


            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            rv_side_menu.setLayoutManager(linearLayoutManager);
            sideMenuAdapter = new SideMenuAdapter();
            rv_side_menu.setAdapter(sideMenuAdapter);
            rv_side_menu.setSelected(true);
            selectFragment(0);

            mIntentFilter = new IntentFilter();
            mIntentFilter.addAction(mBroadcastNotificationAction);

            if(getIntent().getExtras() != null && getIntent().getExtras().containsKey("push_id"))
            {
                if(NetworkManager.isInternetConnected(this))
                    decreaseBadgeCount(getIntent().getExtras().getString("push_id"));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setMessage("Are you sure to exit?");
            alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    finish();
                }
            });
            alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            alert.show();
        }
    }


    @OnClick(R.id.ll_notification)
    public void onClickNotification(View view)
    {
        try
        {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            tv_title_home.setText(""+getResources().getString(R.string.notification));
            setFragment(NotificationFragment.newInstance(this));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public class SideMenuAdapter extends RecyclerView.Adapter<SideMenuAdapter.ViewHolder>
    {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_side_menu,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {

            holder.tv_side_menu_text.setText(menu_title[position]);
            holder.img_side_menu.setImageResource(menu_icons[position]);

            holder.tv_side_menu_text.setTypeface(Share.Font.regular);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(GravityCompat.START);
                    selectFragment(position);
                }
            });
        }

        @Override
        public int getItemCount() {
            return menu_title.length;
        }

        public class ViewHolder extends RecyclerView.ViewHolder
        {
            @BindView(R.id.tv_side_menu_text)TextView tv_side_menu_text;
            @BindView(R.id.img_side_menu)ImageView img_side_menu;

            View itemView;

            public ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this,itemView);
                this.itemView = itemView;
            }
        }

    }

    private void selectFragment(int position)
    {
        try
        {
            switch (position)
            {
                case 0:
                    tv_title_home.setText("");
                    setFragment(HomeFragment.newInstance(this));
                    break;
                case 1:
                    tv_title_home.setText(""+getResources().getString(R.string.contact_us));
                    setFragment(ContactUsFragment.newInstance(this));
                    break;
                case 2:
                    tv_title_home.setText(""+getResources().getString(R.string.terms_condition));
                    setFragment(TermsAndConditionsFragment.newInstance(this));
                    break;
                case 3:
                    tv_title_home.setText(""+getResources().getString(R.string.settings));
                    setFragment(SettingsFragment.newInstance(this));
                    break;
                case 4:  //Logout
                    onClickLogout();
                    break;
                default:
                    setFragment(HomeFragment.newInstance(this));
                    break;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void setFragment(Fragment fragment)
    {
        getSupportFragmentManager().beginTransaction().replace(R.id.frm_container,fragment).commit();
    }

    private void onClickLogout()
    {
        try
        {
            final Dialog dialog = new Dialog(this);
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

            tv_msg.setTypeface(Share.Font.regular);
            tv_msg_1.setTypeface(Share.Font.regular);
            btn_yes.setTypeface(Share.Font.bold);
            btn_no.setTypeface(Share.Font.bold);

            btn_yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    SharedPrefs.clearPrefs(HomeActivity.this);
                    dialog.cancel();

                    Intent loginIntent = new Intent(HomeActivity.this,LoginScreenActivity.class);
                    startActivity(loginIntent);
                    finish();
                }
            });
            btn_no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.cancel();
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(notificationReceiver, mIntentFilter);
    }
    @Override
    protected void onPause() {
        unregisterReceiver(notificationReceiver);
        super.onPause();
    }

//    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context,final Intent intent) {
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    try
//                    {
//                        if (intent.getAction().equals(mBroadcastNotificationAction))
//                        {
//                            Log.e(TAG,intent.getStringExtra("Data") + "\n\n");
//                            AlertDialog.Builder b = new AlertDialog.Builder(HomeActivity.this);
//                            b.setMessage("BroadcastReceiver "+intent.getStringExtra("dataJsonObject"));
//                            b.create().show();
//                        }
//                    }
//                    catch (Exception e){e.printStackTrace();}
//                }
//            });
//        }
//    };

    /*------------------------------------- Webservice --------------------------*/
    private void decreaseBadgeCount(String push_id)
    {
        try{
            Call<DeleteRespone> decreaseBadgeCountResponeCall = ApiClient.getClient().create(ApiService.class).decrease_badge_count(SharedPrefs.getString(this,SharedPrefs.USER_ID),push_id);
            decreaseBadgeCountResponeCall.enqueue(new Callback<DeleteRespone>() {
                @Override
                public void onResponse(Call<DeleteRespone> call, Response<DeleteRespone> response) {
                    Log.e(TAG,""+response.body().getDeleteuberAlpha().toString());
                }

                @Override
                public void onFailure(Call<DeleteRespone> call, Throwable t) {

                }
            });
        }
        catch (Exception e){e.printStackTrace();}
    }
}
