package com.softdesign.devintensive.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.softdesign.devintensive.R;


public class MyBehavior extends CoordinatorLayout.Behavior<LinearLayout> {
    private float mMultiplier=ConstantManager.BEHAVIOR_MULTIPIER;
    private final int mMaxAppbarHeight;
    private final int mMaxUserInfoHeight;
    private final int mMinUserInfoHeight;
    public MyBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.UserInfoBehavior);
        mMinUserInfoHeight = a.getDimensionPixelSize(
                R.styleable.UserInfoBehavior_behavior_min_height, 64);
        mMaxAppbarHeight = context.getResources().getDimensionPixelSize(R.dimen.profile_image_size);//256dp
        mMaxUserInfoHeight = context.getResources().getDimensionPixelSize(R.dimen.statistic_size_112);//112dp
    }
    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, LinearLayout child, View dependency) {
        return dependency instanceof NestedScrollView;
    }
    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, LinearLayout child, View dependency) {

        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        float currentFriction = UIHelper.currentFriction(mMinUserInfoHeight,mMaxAppbarHeight,dependency.getTop());
        int currentHeight = UIHelper.lerp(mMinUserInfoHeight,mMaxUserInfoHeight,currentFriction);
        lp.height=currentHeight;
//        int pxindp=TypedValue.COMPLEX_UNIT_DIP;
//        if((int)(dependency.getY()*mMultiplier)>=child.getChildAt(0).getHeight())lp.height=(int)(dependency.getY()*mMultiplier)*pxindp;
//        else lp.height=child.getChildAt(0).getHeight();
        dependency.setPadding(dependency.getPaddingLeft(),lp.height,dependency.getPaddingRight(),dependency.getPaddingBottom());
        child.setY(dependency.getY());
        child.setLayoutParams(lp);
        return true;

    }



}
