/*
 * Copyright (c) Joaquim Ley 2016. All Rights Reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.joaquimley.faboptions;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.support.annotation.MenuRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.view.SupportMenuInflater;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;

/**
 * FabOptions component
 */
@CoordinatorLayout.DefaultBehavior(FabOptionsBehavior.class)
public class FabOptions extends FrameLayout implements View.OnClickListener {

    private static final String TAG = "FabOptions";

    private boolean mIsOpen;
    private View.OnClickListener mListener;

    private Menu mMenu; // TODO: 22/11/2016 add items in runtime
    private FloatingActionButton mFab;

    private View mBackground;
    private View mSeparator;
    private FabOptionsButtonContainer mButtonContainer;
    private int mAnimationDuration;

    public FabOptions(Context context) {
        this(context, null, 0);
    }

    public FabOptions(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FabOptions(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);
        if (attrs != null) {
            inflateButtonsFromAttrs(context, attrs);
        }
        close();
    }

    private void initViews(Context context) {
        inflate(context, R.layout.faboptions_layout, this);
        mIsOpen = false;
        mBackground = findViewById(R.id.background);
        mFab = (FloatingActionButton) findViewById(R.id.faboptions_fab);
        mFab.setOnClickListener(this);
        mButtonContainer = (FabOptionsButtonContainer) findViewById(R.id.button_container);
        setDrawableColor(mBackground.getBackground());
        mAnimationDuration = getResources().getInteger(android.R.integer.config_shortAnimTime);
    }

    private void inflateButtonsFromAttrs(Context context, AttributeSet attrs) {
        TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.FabOptions, 0, 0);
        if (attributes.hasValue(R.styleable.FabOptions_button_menu)) {
            setButtonsMenu(attributes.getResourceId(R.styleable.FabOptions_button_menu, 0));
        }
    }

    public void setButtonsMenu(@MenuRes int menuId) {
        Context context = getContext();
        mMenu = new MenuBuilder(context);
        SupportMenuInflater menuInf = new SupportMenuInflater(context);
        menuInf.inflate(menuId, mMenu);
        addButtonsFromMenu(context, mMenu);
        mSeparator = mButtonContainer.addSeparator(context);
    }

    private void addButtonsFromMenu(Context context, Menu menu) {
        for (int i = 0; i < menu.size(); i++) {
            MenuItem menuItem = menu.getItem(i);
            addButton(context, menuItem);
        }
    }

    private void addButton(Context context, MenuItem menuItem) {
        AppCompatImageView button = mButtonContainer.addButton(context, menuItem.getItemId(),
                menuItem.getTitle(), menuItem.getIcon());
        button.setOnClickListener(this);
    }

    private int fetchAccentColor() {
        TypedValue typedValue = new TypedValue();

        TypedArray a = getContext().obtainStyledAttributes(typedValue.data, new int[] { R.attr.colorAccent });
        int color = a.getColor(0, 0);

        a.recycle();

        return color;
    }

    public void setDrawableColor(Drawable d) {
        d = DrawableCompat.wrap(d);
        DrawableCompat.setTint(d.mutate(), fetchAccentColor());
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.faboptions_fab) {
            if (mIsOpen) {
                close();
            } else {
                open();
            }
        } else {
            if (mListener != null) {
                mListener.onClick(v);
                close();
            }
        }
    }

    public void setOnClickListener(View.OnClickListener listener) {
        mListener = listener;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    private void open() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AnimatedVectorDrawable drawable = (AnimatedVectorDrawable) getResources().getDrawable(R.drawable.faboptions_ic_menu_animatable, null);
            mFab.setImageDrawable(drawable);
            drawable.start();
        } else {
            animateFabDrawable(true);
        }
        animateBackground(true);
        mIsOpen = true;
    }

    private void close() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            AnimatedVectorDrawable drawable = (AnimatedVectorDrawable) getResources().getDrawable(R.drawable.faboptions_ic_close_animatable, null);
            mFab.setImageDrawable(drawable);
            drawable.start();
        } else {
            animateFabDrawable(false);
        }
        animateButtons(false);
        animateBackground(false);
        mIsOpen = false;
    }
  
    private void animateFabDrawable(boolean isOpen){
        final Drawable drawables[];
        if (isOpen){
            drawables = new Drawable[]{VectorDrawableCompat.create(getResources(), R.drawable.faboptions_ic_close, null),
                    VectorDrawableCompat.create(getResources(), R.drawable.faboptions_ic_overflow, null)};
        }else {
            drawables = new Drawable[]{VectorDrawableCompat.create(getResources(), R.drawable.faboptions_ic_overflow, null),
                    VectorDrawableCompat.create(getResources(), R.drawable.faboptions_ic_close, null)};
        }
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, 255);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setDuration(mAnimationDuration);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                drawables[0].setAlpha(value);
                drawables[1].setAlpha(255 - value);
                LayerDrawable layerDrawable = new LayerDrawable(drawables);
                mFab.setImageDrawable(layerDrawable);
            }
        });
        valueAnimator.start();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mSeparator != null) {
            ViewGroup.LayoutParams separatorLayoutParams = mSeparator.getLayoutParams();
            separatorLayoutParams.width = mFab.getMeasuredWidth();
            separatorLayoutParams.height = mFab.getMeasuredHeight();
            mSeparator.setLayoutParams(separatorLayoutParams);
        }
        if (!mIsOpen) {
            ViewGroup.LayoutParams backgroundLayoutParams = mBackground.getLayoutParams();
            backgroundLayoutParams.width = mButtonContainer.getMeasuredWidth();
            backgroundLayoutParams.height = mButtonContainer.getMeasuredHeight();
            mBackground.setLayoutParams(backgroundLayoutParams);
        }
    }

    private void animateBackground(final boolean isOpen) {
        if (isOpen) {
            performScaleAnimation(mBackground, 0f, 1f, 1f, 1f, new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    animateButtons(true);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        } else {
            performScaleAnimation(mBackground, 1f, 0f, 1f, 1f);
        }
    }


    private void animateButtons(boolean isOpen) {
        for (int i = 0; i < mButtonContainer.getChildCount(); i++) {
            if (isOpen) {
                performScaleAnimation(mButtonContainer.getChildAt(i), 0f, 1f, 0f, 1f);
            } else {
                performScaleAnimation(mButtonContainer.getChildAt(i), 1f, 0f, 1f, 0f);
            }
        }
    }

    public void performScaleAnimation(View view, float startScaleX, float endScaleX, float startScaleY, float endScaleY) {
        performScaleAnimation(view, startScaleX, endScaleX, startScaleY, endScaleY, null);
    }

    public void performScaleAnimation(View view, float startScaleX, float endScaleX, float startScaleY, float endScaleY, Animation.AnimationListener animationListener) {
        Animation anim = new ScaleAnimation(
                startScaleX, endScaleX, // Start and end values for the X axis scaling
                startScaleY, endScaleY, // Start and end values for the Y axis scaling
                Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point of X scaling
                Animation.RELATIVE_TO_SELF, 0.5f); // Pivot point of Y scaling
        anim.setFillAfter(true); // Needed to keep the result of the animation
        anim.setDuration(mAnimationDuration);
        if (animationListener != null) {
            anim.setAnimationListener(animationListener);
        }
        view.startAnimation(anim);
    }

    public boolean isOpen() {
        return mIsOpen;
    }
}