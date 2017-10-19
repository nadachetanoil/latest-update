package com.fracappzstudios.oilfieldnotifications.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;

import com.fracappzstudios.oilfieldnotifications.R;
import com.fracappzstudios.oilfieldnotifications.util.Share;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Harshad Kathiriya on 6/20/2017.
 */

public class ContactUsFragment extends Fragment
{
    private static final String TAG = ContactUsFragment.class.getSimpleName();

    @BindView(R.id.tbl_rw_call_us) TableRow tbl_rw_call_us;
    @BindView(R.id.tbl_rw_email_us) TableRow tbl_rw_email_us;
    @BindView(R.id.tv_address)TextView tv_address;
    @BindView(R.id.tv_call_us)TextView tv_call_us;
    @BindView(R.id.tv_email_us)TextView tv_email_us;

    Activity activity;

    public static ContactUsFragment newInstance(Activity activity) {
        Bundle args = new Bundle();
        ContactUsFragment fragment = new ContactUsFragment();
        fragment.setArguments(args);
        fragment.activity =activity;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_contactus,container,false);
        ButterKnife.bind(this,view);
        setFont();
        return view;
    }

    private void setFont()
    {
        tv_address.setTypeface(Share.Font.regular);
        tv_call_us.setTypeface(Share.Font.bold);
        tv_email_us.setTypeface(Share.Font.bold);
    }

    @OnClick(R.id.tbl_rw_call_us)
    public void onClickCallUs(View view)
    {
        try
        {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:0123456789"));
            startActivity(intent);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.tbl_rw_email_us)
    public void onClickEmailUs(View view)
    {
        try
        {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto","abc@gmail.com", null));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Body");
            startActivity(Intent.createChooser(emailIntent, "Send email..."));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
