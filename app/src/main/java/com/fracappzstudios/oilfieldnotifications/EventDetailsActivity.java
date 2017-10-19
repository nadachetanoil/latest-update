package com.fracappzstudios.oilfieldnotifications;

import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fracappzstudios.oilfieldnotifications.adapter.CommentListAdapter;
import com.fracappzstudios.oilfieldnotifications.model.AllCommentdetail;
import com.fracappzstudios.oilfieldnotifications.model.Commentdetails;
import com.fracappzstudios.oilfieldnotifications.util.NetworkManager;
import com.fracappzstudios.oilfieldnotifications.util.Share;
import com.fracappzstudios.oilfieldnotifications.util.SharedPrefs;
import com.fracappzstudios.oilfieldnotifications.util.Urls;
import com.fracappzstudios.oilfieldnotifications.webservice.ApiClient;
import com.fracappzstudios.oilfieldnotifications.webservice.ApiService;
import com.fracappzstudios.oilfieldnotifications.webservice.response.AllCommentRespone;
import com.fracappzstudios.oilfieldnotifications.webservice.response.DeleteRespone;
import com.fracappzstudios.oilfieldnotifications.webservice.response.EventListRrespone;
import com.fracappzstudios.oilfieldnotifications.webservice.response.GetEventDetailsRespone;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
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


public class EventDetailsActivity extends AppCompatActivity {

    private static final String TAG = EventDetailsActivity.class.getSimpleName();

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.tv_member_name) TextView tv_member_name;
    @BindView(R.id.tv_company_name) TextView tv_company_name;
    @BindView(R.id.tv_email)TextView tv_email;
    @BindView(R.id.iv_user_profile) ImageView iv_user_profile;
    @BindView(R.id.tv_event_name) TextView tv_event_name;
    @BindView(R.id.tv_event_date) TextView tv_event_date;
    @BindView(R.id.tv_event_descriptions)TextView tv_event_descriptions;
    @BindView(R.id.viewPager) ViewPager viewPager;
    @BindView(R.id.tv_comment) TextView tv_comment;
    @BindView(R.id.tv_event_details) TextView tv_event_details;
    @BindView(R.id.rv_comment) RecyclerView rv_comment;
    @BindView(R.id.ll_document) LinearLayout ll_document;

    List<String> images = new ArrayList<>();
    List<String> doc = new ArrayList<>();
    MyCustomPagerAdapter myCustomPagerAdapter;
    CommentListAdapter commentListAdapter;
    LinearLayoutManager linearLayoutManager;


    boolean is_loadcomment = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        ButterKnife.bind(this);
        initView();
    }

    private void initView()
    {
        try
        {
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            setFont();

            linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            rv_comment.setLayoutManager(linearLayoutManager);

            if(getIntent().getExtras() != null && getIntent().getExtras().containsKey("event_id"))
            {
                is_loadcomment = false;
                if(NetworkManager.isInternetConnected(this))
                    getEventDetails(getIntent().getExtras().getString("event_id"));
            }
            else
                setDetails();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void setDetails()
    {
        try
        {
            if(Share.eventdetail != null)
            {
//                Log.e(TAG,""+Share.eventdetail.toString());
                tv_member_name.setText(""+Share.eventdetail.getCreateEventUserId().get(0).getFirstName()+" "+Share.eventdetail.getCreateEventUserId().get(0).getLastName());
                tv_company_name.setText(""+Share.eventdetail.getCreateEventUserId().get(0).getCompanyName().toString());
                tv_email.setText(""+Share.eventdetail.getCreateEventUserId().get(0).getEmail().toString());
                Picasso.with(this)
                        .load(Urls.PEOPLE_IMAGE_URL+Share.eventdetail.getCreateEventUserId().get(0).getProfileImage())
                        .placeholder(R.drawable.ic_group)
                        .error(R.drawable.ic_group)
                        .into(iv_user_profile);

                tv_event_name.setText(""+Share.eventdetail.getEventName());
                tv_event_date.setText(""+Share.eventdetail.getCreateDate());
                tv_event_descriptions .setText(""+Share.eventdetail.getEventDescription());

                Log.e(TAG,"EventAttachFile :-> "+Share.eventdetail.getEventAttachFile());
                Log.e(TAG,"EventAttachFile :-> "+Share.eventdetail.getEventAttachFile());
                if(Share.eventdetail.getEventAttachFile() != null && !Share.eventdetail.getEventAttachFile().equals(""))
                {
                    List<String> imagesDocs  = Arrays.asList(Share.eventdetail.getEventAttachFile().split("\\s*,\\s*"));
                    images.clear();
                    doc.clear();
                    for (int index=0;index<imagesDocs.size();index++)
                    {
                        String ext = getFileExt(imagesDocs.get(index));
                        Log.e(TAG," extendsion :-----> "+ext);
                        if(ext.toUpperCase().equals("JPEG") ||
                                ext.toUpperCase().equals("PNG") ||
                                ext.toUpperCase().equals("GIF") ||
                                ext.toUpperCase().equals("BMP") ||
                                ext.toUpperCase().equals("JPG"))
                        {
                            images.add(imagesDocs.get(index));
                        }
                        else
                        {
                            doc.add(imagesDocs.get(index));
                        }
                    }

                    if(images.size() != 0)
                    {
                        myCustomPagerAdapter = new MyCustomPagerAdapter(this);
                        viewPager.setAdapter(myCustomPagerAdapter);
                        viewPager.setVisibility(View.VISIBLE);
                    }
                    else
                        viewPager.setVisibility(View.GONE);
                }

                if(doc.size() == 0)
                    ll_document.setVisibility(View.GONE);
                else
                {
                    ll_document.setVisibility(View.VISIBLE);
                    addDoc();
                }
            }
        }
        catch (Exception e){e.printStackTrace();}
    }

    private void addDoc()
    {
        try
        {
            for(int index=0;index<doc.size();index++)
            {
                View view = LayoutInflater.from(this).inflate(R.layout.row_doc,null,false);

                TextView tv_file_name = (TextView)view.findViewById(R.id.tv_file_name);
                ImageView iv_file_ext = (ImageView)view.findViewById(R.id.iv_file_ext);

                String ext = getFileExt(doc.get(index));
                if(ext.toLowerCase().equals("doc") ||
                        ext.toLowerCase().equals("dot") ||
                        ext.toLowerCase().equals("wbk") ||
                        ext.toLowerCase().equals("docx") ||
                        ext.toLowerCase().equals("docm") ||
                        ext.toLowerCase().equals("dotx") ||
                        ext.toLowerCase().equals("dotm") ||
                        ext.toLowerCase().equals("docb"))
                {
                    iv_file_ext.setImageResource(R.drawable.ic_word);
                }
                else if(ext.toLowerCase().equals("xls ") ||
                        ext.toLowerCase().equals("xlt") ||
                        ext.toLowerCase().equals("xlm ") ||
                        ext.toLowerCase().equals("xlsx") ||
                        ext.toLowerCase().equals("xlsm") ||
                        ext.toLowerCase().equals("xltx") ||
                        ext.toLowerCase().equals("xltm"))
                    iv_file_ext.setImageResource(R.drawable.ic_excel);
                else if(ext.toLowerCase().equals("ppt")||
                        ext.toLowerCase().equals("pot") ||
                        ext.toLowerCase().equals("pps") ||
                        ext.toLowerCase().equals("pptx") ||
                        ext.toLowerCase().equals("pptm") ||
                        ext.toLowerCase().equals("potx") ||
                        ext.toLowerCase().equals("potm") ||
                        ext.toLowerCase().equals("ppsx") ||
                        ext.toLowerCase().equals("ppsm") ||
                        ext.toLowerCase().equals("sldx") ||
                        ext.toLowerCase().equals("sldm"))
                    iv_file_ext.setImageResource(R.drawable.ic_ppt);
                else if(ext.toLowerCase().equals("pdf"))
                    iv_file_ext.setImageResource(R.drawable.ic_pdf);
                else if(ext.toLowerCase().equals("txt"))
                    iv_file_ext.setImageResource(R.drawable.ic_text);
                else if(ext.toLowerCase().equals("csv"))
                    iv_file_ext.setImageResource(R.drawable.ic_csv);
                else if(ext.toLowerCase().equals("zip"))
                    iv_file_ext.setImageResource(R.drawable.ic_zip);

                tv_file_name.setText(doc.get(index).substring(15,doc.get(index).length()));
                final String name = doc.get(index);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent showDocIntent = new Intent(EventDetailsActivity.this,ShowDocActivity.class);
                        showDocIntent.putExtra("docname",""+name);
                        startActivity(showDocIntent);
                    }
                });
                ll_document.addView(view);
            }
        }
        catch (Exception e){e.printStackTrace();}
    }

    public static String getFileExt(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
    }
    private void setFont()
    {
        try
        {
            tv_member_name.setTypeface(Share.Font.bold);
            tv_company_name.setTypeface(Share.Font.regular);
            tv_email.setTypeface(Share.Font.regular);
            tv_event_name.setTypeface(Share.Font.bold);
            tv_event_date.setTypeface(Share.Font.regular);
            tv_event_descriptions.setTypeface(Share.Font.regular);
            tv_event_details.setTypeface(Share.Font.bold);
        }
        catch (Exception e){e.printStackTrace();}
    }
    @OnClick(R.id.iv_back)
    public void onClickBack(View view)
    {
        finish();
    }

    @OnClick(R.id.tv_comment)
    public void onClickComment(View view)
    {
        try
        {
            Share.selectedMember.clear();
            Intent selectMemberIntent = new Intent(EventDetailsActivity.this,CreateCommentActivity.class);
            startActivity(selectMemberIntent);
        }
        catch (Exception e){e.printStackTrace();}
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(NetworkManager.isInternetConnected(this) && is_loadcomment)
            getAllComment();
    }


    public void showImageDownloadDialog(final String imgPath, final String docname)
    {
        try
        {
            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dlg_img_download);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();

            ImageView iv_back = (ImageView)dialog.findViewById(R.id.iv_back);
            ImageView iv_download = (ImageView)dialog.findViewById(R.id.iv_download);
            ImageView iv_doc_image = (ImageView)dialog.findViewById(R.id.iv_doc_image);

            Picasso.with(this).load(imgPath).into(iv_doc_image);

            iv_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.cancel();
                }
            });
            iv_download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                        String servicestring = Context.DOWNLOAD_SERVICE;
                        DownloadManager downloadmanager;
                        downloadmanager = (DownloadManager) getSystemService(servicestring);
                        Uri uri = Uri.parse(imgPath);
                        DownloadManager.Request request = new DownloadManager.Request(uri);
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,docname.substring(15,docname.length()));
                        Long reference = downloadmanager.enqueue(request);
                        Toast.makeText(EventDetailsActivity.this,"Start Downloading...",Toast.LENGTH_SHORT).show();
                    }catch (Exception e){e.printStackTrace();}
                }
            });

        }
        catch (Exception e){e.printStackTrace();}
    }

    /*-------------------------Show ImageView ------------------------------------*/
    public class MyCustomPagerAdapter extends PagerAdapter {
        Context context;
        LayoutInflater layoutInflater;


        public MyCustomPagerAdapter(Context context) {
            this.context = context;
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return images != null?images.size():0;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((LinearLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View itemView = layoutInflater.inflate(R.layout.row_event_image, container, false);
            try
            {
                ImageView imageView = (ImageView) itemView.findViewById(R.id.iv_event_image);
                final String fileName = Urls.Event_FILE_URL+images.get(position);
                Log.e(TAG,"Images Path :-> "+fileName);
                Picasso.with(EventDetailsActivity.this)
                        .load(Urls.Event_FILE_URL+images.get(position))
                        .resize(Share.screenWidth,Share.screenHeight/2)
                        .error(R.drawable.ic_group)
                        .into(imageView);
                imageView.getLayoutParams().height = Share.screenHeight/2;

                container.addView(itemView);

                //listening to image click
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(EventDetailsActivity.this, "you clicked image " + (position + 1), Toast.LENGTH_LONG).show();
                        showImageDownloadDialog(fileName,images.get(position));
                    }
                });

            }catch (Exception e){e.printStackTrace();}

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }
    }

    /*---------------------------------------- WebService ---------------------------------*/
    private void getAllComment()
    {
        try
        {
            final ProgressDialog pd = ProgressDialog.show(this,"","Loading...",true,false);
            pd.show();

            Call<AllCommentRespone> allCommentResponeCall = ApiClient.getClient().create(ApiService.class).allCommentResponse(SharedPrefs.getString(this,SharedPrefs.USER_ID),Share.eventdetail.getGroupId(),Share.eventdetail.getEventId());
            allCommentResponeCall.enqueue(new Callback<AllCommentRespone>() {
                @Override
                public void onResponse(Call<AllCommentRespone> call, Response<AllCommentRespone> response) {
                    try
                    {
                        pd.cancel();
                        if(response.body().getAllCommentuberAlpha().getStatus().equals("success"))
                        {

                            final List<AllCommentdetail> allCommentdetailList = response.body().getAllCommentuberAlpha().getAllCommentdetails();
                            Log.e(TAG,"allCommentdetailList :-> "+allCommentdetailList.size());
                            commentListAdapter = new CommentListAdapter(EventDetailsActivity.this,allCommentdetailList);
                            rv_comment.setAdapter(commentListAdapter);
                            rv_comment.setVisibility(View.VISIBLE);
                            Log.e(TAG,"rv_comment :-> "+rv_comment.getChildCount());
                            commentListAdapter.setOnItemClickListener(new CommentListAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view,final int position) {

                                    final Dialog dialog = new Dialog(EventDetailsActivity.this);
                                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                                    dialog.setContentView(R.layout.dlg_group);
                                    dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                                    dialog.setCancelable(true);
                                    dialog.setCanceledOnTouchOutside(false);
                                    dialog.show();

                                    Button btn_ok = (Button)dialog.findViewById(R.id.btn_ok);
                                    Button btn_cancel = (Button)dialog.findViewById(R.id.btn_cancel);
                                    TextView tv_msg = (TextView)dialog.findViewById(R.id.tv_msg);
                                    TextView tv_msg_1 = (TextView)dialog.findViewById(R.id.tv_msg);

                                    tv_msg_1.setText("You want to delete this comment.");
                                    tv_msg.setTypeface(Share.Font.bold);
                                    tv_msg_1.setTypeface(Share.Font.regular);
                                    btn_ok.setTypeface(Share.Font.bold);
                                    btn_cancel.setTypeface(Share.Font.bold);

                                    btn_ok.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.cancel();
                                            if(NetworkManager.isInternetConnected(EventDetailsActivity.this))
                                                deleteCommentRequest(allCommentdetailList.get(position).getCommentId(),position);
                                        }
                                    });
                                    btn_cancel.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.cancel();
                                        }
                                    });
                                }
                            });
                        }
                        else
                        {
                            rv_comment.setVisibility(View.GONE);
                        }
                    }
                    catch (Exception e){e.printStackTrace();}
                }

                @Override
                public void onFailure(Call<AllCommentRespone> call, Throwable t) {
                    pd.cancel();
                    t.printStackTrace();
                }
            });
        }
        catch (Exception e){e.printStackTrace();}
    }

    private void deleteCommentRequest(String comment_id,final int position)
    {
        try
        {
            final ProgressDialog pd = ProgressDialog.show(this,"","Loading...",true,false);
            pd.show();

            Call<DeleteRespone> deleteResponeCall = ApiClient.getClient().create(ApiService.class).delete_comment(SharedPrefs.getString(this,SharedPrefs.USER_ID),comment_id);
            deleteResponeCall.enqueue(new Callback<DeleteRespone>() {
                @Override
                public void onResponse(Call<DeleteRespone> call, Response<DeleteRespone> response) {
                    try{
                        pd.cancel();
                        if(response.body().getDeleteuberAlpha().getStatus().equals("success"))
                        {
                            commentListAdapter.removeItem(position);
                        }
                        else
                            Toast.makeText(EventDetailsActivity.this,""+response.body().getDeleteuberAlpha().getMessage(),Toast.LENGTH_SHORT).show();
                    }catch (Exception e){e.printStackTrace();}
                }

                @Override
                public void onFailure(Call<DeleteRespone> call, Throwable t) {

                }
            });
        }
        catch (Exception e){e.printStackTrace();}
    }

    public void getEventDetails(String event_id)
    {
        try
        {
            final ProgressDialog pd = ProgressDialog.show(this,"","Loading...",true,false);
            pd.show();
            Call<GetEventDetailsRespone> eventListRresponeCall = ApiClient.getClient().create(ApiService.class).getEventDetails(event_id);
            eventListRresponeCall.enqueue(new Callback<GetEventDetailsRespone>() {
                @Override
                public void onResponse(Call<GetEventDetailsRespone> call, Response<GetEventDetailsRespone> response) {
                    try{
                        pd.cancel();
                        if(response.body().getEventDetailuberAlpha().getStatus().equals("success"))
                        {
                            Share.groupdetail = response.body().getEventDetailuberAlpha().getGroupEventdetails().get(0).getGroupdetail().get(0);
                            Share.eventdetail = response.body().getEventDetailuberAlpha().getGroupEventdetails().get(0).getEventdetail().get(0);
                            Log.e(TAG,"================> \n Share.eventdetail => "+Share.eventdetail.toString());
                            Log.e(TAG,"================> \n Share.groupdetail => "+Share.groupdetail.toString());
                            setDetails();
                            getAllComment();
                        }
                        else
                            Toast.makeText(EventDetailsActivity.this,""+response.body().getEventDetailuberAlpha().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                    catch (Exception e){pd.cancel();e.printStackTrace();}
                }

                @Override
                public void onFailure(Call<GetEventDetailsRespone> call, Throwable t) {
                    t.printStackTrace();
                    pd.cancel();
                }
            });
        }
        catch (Exception e){e.printStackTrace();}
    }
}
