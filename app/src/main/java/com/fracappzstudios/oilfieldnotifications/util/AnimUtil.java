package com.fracappzstudios.oilfieldnotifications.util;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;

import com.fracappzstudios.oilfieldnotifications.R;

/**
 * Created by Harshad Kathiriya on 12/19/2016.
 */

public class AnimUtil {

    Animation anim;

    Context context;

    public AnimUtil(Context context) {
        this.context = context;
    }

    public void loadLeftAnimation(View view)
    {
        anim = AnimationUtils.loadAnimation(context, R.anim.move_left);
        view.setAnimation(anim);
    }

    public void loadRightAnimation(View view)
    {
        anim = AnimationUtils.loadAnimation(context, R.anim.move_right);
        view.setAnimation(anim);
    }

    public void loadSlideUpAnimation(View view)
    {
        anim = AnimationUtils.loadAnimation(context, R.anim.slide_up);
        view.setAnimation(anim);
    }

    public void loadSlideDownAnimation(View view)
    {
        anim = AnimationUtils.loadAnimation(context, R.anim.slide_down);
        view.setAnimation(anim);
    }

    public void loadFadeInAnim(View view)
    {
        anim = AnimationUtils.loadAnimation(context, R.anim.fade_in_anim);
        view.startAnimation(anim);
    }

    public void loadFadeOutAnim(View view)
    {
        anim = AnimationUtils.loadAnimation(context, R.anim.fade_out);
        view.startAnimation(anim);
    }


    public static void expand(final View v) {
        v.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final int targtetHeight = v.getMeasuredHeight();

        v.getLayoutParams().height = 0;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? ViewGroup.LayoutParams.WRAP_CONTENT
                        : (int)(targtetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        a.setDuration((int)(targtetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if(interpolatedTime == 1){
                    v.setVisibility(View.GONE);
                }else{
                    v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        a.setDuration((int)(initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }
}
