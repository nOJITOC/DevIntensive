package com.softdesign.devintensive.utils;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;


public class MyBehavior extends CoordinatorLayout.Behavior<LinearLayout> {
    public MyBehavior(Context context, AttributeSet attrs) {
    }
    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, LinearLayout child, View dependency) {
        return dependency instanceof NestedScrollView;
    }
    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, LinearLayout child, View dependency) {
        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        CoordinatorLayout.LayoutParams lpd = (CoordinatorLayout.LayoutParams) dependency.getLayoutParams();
        if(dependency.getY()>=128 &&dependency.getY()<=224)lp.height=(int)dependency.getY()/2;
        dependency.setPadding(dependency.getPaddingLeft(),lp.height,dependency.getPaddingRight(),dependency.getPaddingBottom());
        child.setY(dependency.getY());
        System.out.println(lp.height);
        System.out.println(lp.getBehavior());
        child.setLayoutParams(lp);
        return true;

    }


}
