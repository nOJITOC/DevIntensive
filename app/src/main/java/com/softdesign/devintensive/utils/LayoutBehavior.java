package com.softdesign.devintensive.utils;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by Иван on 08.07.2016.
 */
public class LayoutBehavior extends CoordinatorLayout.Behavior<LinearLayout>{
    private  Context mContext;
    private AttributeSet mAttributeSet;
    /**
     * Default constructor for inflating Behaviors from layout. The Behavior will have
     * the opportunity to parse specially defined layout parameters. These parameters will
     * appear on the child view tag.
     *
     * @param context
     * @param attrs
     */
    public LayoutBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext=context;
        mAttributeSet=attrs;
    }

    /**
     * Respond to a change in a child's dependent view
     * <p/>
     * <p>This method is called whenever a dependent view changes in size or position outside
     * of the standard layout flow. A Behavior may use this method to appropriately update
     * the child view in response.</p>
     * <p/>
     * <p>A view's dependency is determined by
     * {@link #layoutDependsOn(CoordinatorLayout, View, View)} or
     * if {@code child} has set another view as it's anchor.</p>
     * <p/>
     * <p>Note that if a Behavior changes the layout of a child via this method, it should
     * also be able to reconstruct the correct position in
     * {@link #onLayoutChild(CoordinatorLayout, View, int) onLayoutChild}.
     * <code>onDependentViewChanged</code> will not be called during normal layout since
     * the layout of each child view will always happen in dependency order.</p>
     * <p/>
     * <p>If the Behavior changes the child view's size or position, it should return true.
     * The default implementation returns false.</p>
     *
     * @param parent     the parent view of the given child
     * @param child      the child view to manipulate
     * @param dependency the dependent view that changed
     * @return true if the Behavior changed the child view's size or position, false otherwise
     */
    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, LinearLayout child, View dependency) {
        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        NestedScrollView nestedScroll=null;
        for (int i = 0; i <parent.getChildCount() ; i++) {
            if(parent.getChildAt(i) instanceof NestedScrollView){
                nestedScroll=(NestedScrollView) parent.getChildAt(i);
            }
        }
        if(nestedScroll!=null){
            child.setY(nestedScroll.getY());
            lp.height=(int)(dependency.getTranslationY()*ConstantManager.BEHAVIOR_MULTIPIER);
            CoordinatorLayout.LayoutParams lps = (CoordinatorLayout.LayoutParams) dependency.getLayoutParams();
           lps.bottomMargin=lp.height;

            child.setLayoutParams(lp);
           dependency.setLayoutParams(lps);
        }
        return super.onDependentViewChanged(parent, child, dependency);
    }

    /**
     * Determine whether the supplied child view has another specific sibling view as a
     * layout dependency.
     * <p/>
     * <p>This method will be called at least once in response to a layout request. If it
     * returns true for a given child and dependency view pair, the parent CoordinatorLayout
     * will:</p>
     * <ol>
     * <li>Always lay out this child after the dependent child is laid out, regardless
     * of child order.</li>
     * <li>Call {@link #onDependentViewChanged} when the dependency view's layout or
     * position changes.</li>
     * </ol>
     *
     * @param parent     the parent view of the given child
     * @param child      the child view to test
     * @param dependency the proposed dependency of child
     * @return true if child's layout depends on the proposed dependency's layout,
     * false otherwise
     * @see #onDependentViewChanged(CoordinatorLayout, View, View)
     */
    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, LinearLayout child, View dependency) {
        return dependency instanceof AppBarLayout;
    }
}
