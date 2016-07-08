package com.softdesign.devintensive.utils;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;


public class MyBehavior extends CoordinatorLayout.Behavior<LinearLayout> {
    private float mMultiplier=ConstantManager.BEHAVIOR_MULTIPIER;
    public MyBehavior(Context context, AttributeSet attrs) {
    }
    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, LinearLayout child, View dependency) {
        return dependency instanceof NestedScrollView;
    }
    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, LinearLayout child, View dependency) {

        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        int pxindp=TypedValue.COMPLEX_UNIT_DIP;
        if((int)(dependency.getY()*mMultiplier)>=child.getChildAt(0).getHeight())lp.height=(int)(dependency.getY()*mMultiplier)*pxindp;
        else lp.height=child.getChildAt(0).getHeight();
        dependency.setPadding(dependency.getPaddingLeft(),lp.height,dependency.getPaddingRight(),dependency.getPaddingBottom());
        child.setY(dependency.getY());
        child.setLayoutParams(lp);
        return true;

    }



}
