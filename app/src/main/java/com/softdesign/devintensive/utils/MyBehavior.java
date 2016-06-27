package com.softdesign.devintensive.utils;

import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class MyBehavior extends CoordinatorLayout.Behavior<ScrollView> {
    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, ScrollView child, View dependency) {
        return dependency instanceof ImageView;
    }
    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, ScrollView child, View dependency) {
        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        lp.height=dependency.getHeight()/10;
        System.out.println(lp.getBehavior());
        child.setLayoutParams(lp);
        return true;
    }
}
