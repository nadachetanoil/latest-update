package com.fracappzstudios.oilfieldnotifications;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.fracappzstudios.oilfieldnotifications.util.Share;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TutorialActivity extends AppCompatActivity {

    public static final String TAG = TutorialActivity.class.getSimpleName();

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.tv_index) TextView tv_index;
    @BindView(R.id.iv_all_tutorial) ImageView iv_all_tutorial;
    @BindView(R.id.viewPager)ViewPager viewPager;

    public static String Questions[],questions_ans[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        ButterKnife.bind(this);
        initView();
    }

    @OnClick(R.id.iv_back)
    public void OnClickBack(View view){
        finish();
    }

    public void initView(){
        try{
            setSupportActionBar(toolbar);

            Questions =  getResources().getStringArray(R.array.qty);
            questions_ans = getResources().getStringArray(R.array.qty_ans);

            MyPagerAdapter MyPagerAdapter = new MyPagerAdapter(TutorialActivity.this);
            viewPager.setAdapter(MyPagerAdapter);
            Log.e(TAG,"getChildCount :-> "+viewPager.getChildCount());
            viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    tv_index.setText(""+(position+1)+" / "+11);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }
        catch (Exception e){e.printStackTrace();}
    }

    @OnClick(R.id.iv_all_tutorial)
    public void OnClickAllTutorial(View view){
        showTutorialDialog();
    }

    public static class MyPagerAdapter extends PagerAdapter {

        LayoutInflater inflater;
        Context mContext;
        public MyPagerAdapter(Context context) {
            this.mContext = context;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return Questions.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((LinearLayout   ) object);
        }
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View itemView = inflater.inflate(R.layout.row_question_tutorial,container,false);
            TextView tv_title = (TextView)itemView.findViewById(R.id.tv_title);
            TextView tv_description = (TextView)itemView.findViewById(R.id.tv_description);
            ImageView iv_tutorial_img = (ImageView) itemView.findViewById(R.id.iv_tutorial_img);
            WebView web_view = (WebView)itemView.findViewById(R.id.web_view);

            web_view.setVisibility(View.GONE);
            web_view.clearFormData();
            web_view.clearFocus();
            iv_tutorial_img.setVisibility(View.VISIBLE);
            tv_title.setTypeface(Share.Font.thin_bold);
            tv_description.setTypeface(Share.Font.thin_regular);

            tv_title.setText(Questions[position]);
            tv_description.setText(questions_ans[position]);



            if (position == 3){
                GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(iv_tutorial_img);
                Glide.with(mContext).load(R.raw.bird).into(imageViewTarget);
            }
            else if(position == 9){
                web_view.setVisibility(View.VISIBLE);
                iv_tutorial_img.setVisibility(View.GONE);

                WebSettings settings = web_view.getSettings();
                settings.setJavaScriptEnabled(true);
                if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN)
                    settings.setAllowUniversalAccessFromFileURLs(true);
//                final ProgressDialog prDialog = ProgressDialog.show(mContext,"","Loading...",true,true);
                settings.setBuiltInZoomControls(true);
                web_view.setWebChromeClient(new WebChromeClient());
                web_view.setWebViewClient(new WebViewClient() {
                    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                        Toast.makeText(mContext, description, Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onPageStarted(WebView view, String url, Bitmap favicon)
                    {
//                        if(prDialog  != null && !prDialog.isShowing())
//                            prDialog.show();
                    }
                    @Override
                    public void onPageFinished(WebView view, String url) {
//                        prDialog.dismiss();
                    }

                });
                web_view.getSettings().setLoadWithOverviewMode(true);
                web_view.getSettings().setUseWideViewPort(true);
                web_view.loadUrl("https://www.oilfieldnotifications.com/images/tutorial.mp4");
            }
            else{
                iv_tutorial_img.setImageResource(R.drawable.ic_attach_file);
            }

            ((ViewPager) container).addView(itemView);
            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // Remove viewpager_item.xml from ViewPager
            ((ViewPager) container).removeView((LinearLayout) object);

        }
    }

    public void showTutorialDialog()
    {
        try{
            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dlg_tutorial_menu);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();

            LinearLayout ll_tutorial = (LinearLayout)dialog.findViewById(R.id.ll_tutorial);
            for (int index=0;index<Questions.length;index++)
            {
                View view = LayoutInflater.from(TutorialActivity.this).inflate(R.layout.row_tutorial,ll_tutorial,false);
                TextView tv_qty = (TextView)view.findViewById(R.id.tv_qty);
                tv_qty.setText(Questions[index]);
                tv_qty.setTypeface(Share.Font.thin_medium);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
                ll_tutorial.addView(view);
            }
        }
        catch (Exception e){e.printStackTrace();}
    }



}
