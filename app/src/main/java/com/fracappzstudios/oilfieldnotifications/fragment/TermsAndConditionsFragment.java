package com.fracappzstudios.oilfieldnotifications.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;

import com.fracappzstudios.oilfieldnotifications.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Kathiriya Harshad on 6/20/2017.
 */

public class TermsAndConditionsFragment extends Fragment
{
    private static final String TAG = TermsAndConditionsFragment.class.getSimpleName();

    @BindView(R.id.web_view)
    WebView web_view;

    public static TermsAndConditionsFragment newInstance(Activity activity) {
        Bundle args = new Bundle();
        TermsAndConditionsFragment fragment = new TermsAndConditionsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_terms_condi,container,false);
        ButterKnife.bind(this,view);
        web_view.loadUrl("file:///android_asset/Terms_and_Conditions_-_Android.html");
        return view;
    }


}
