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

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.MenuRes;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.view.SupportMenuInflater;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.AppCompatImageView;
import android.transition.ChangeBounds;
import android.transition.ChangeTransform;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import static com.joaquimley.faboptions.R.drawable.faboptions_ic_overflow;

/**
 * FabOptions component
 */
@CoordinatorLayout.DefaultBehavior(FabOptionsBehavior.class)
public class FabOptions extends FrameLayout implements View.OnClickListener {

    private static final String TAG = "FabOptions";
    private static final int NO_DIMENSION = 0;
    private static final long CLOSE_MORPH_TRANSFORM_DURATION = 70;

    private boolean mIsAnimating;
    private boolean mIsOpen;
    private View.OnClickListener mListener;

    private Menu mMenu; // TODO: 22/11/2016 add items in runtime
    private FloatingActionButton mFab;

    private View mBackground;
    private View mSeparator;
    private FabOptionsButtonContainer mButtonContainer;

    public FabOptions(Context context) {
        this(context, null, 0);
    }

    public FabOptions(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FabOptions(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mIsOpen = false;
        initViews(context);
        setInitialFabIcon();

        TypedArray fabOptionsAttributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.FabOptions, 0, 0);
        styleComponent(context, fabOptionsAttributes);
        inflateButtonsFromAttrs(context, fabOptionsAttributes);
    }

    private void initViews(Context context) {
        inflate(context, R.layout.faboptions_layout, this);
        mBackground = findViewById(R.id.faboptions_background);
        mButtonContainer = (FabOptionsButtonContainer) findViewById(R.id.faboptions_button_container);
        mFab = (FloatingActionButton) findViewById(R.id.faboptions_fab);
        mFab.setOnClickListener(this);
    }

    private void setInitialFabIcon() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            VectorDrawable drawable = (VectorDrawable) getResources().getDrawable(faboptions_ic_overflow, null);
            mFab.setImageDrawable(drawable);
        } else {
            mFab.setImageResource(R.drawable.faboptions_ic_overflow);
        }
    }

    private void styleComponent(Context context, TypedArray attributes) {
        // If not set, the background same colour as the FAB, which if not set
        // it will use the default accent color
        int fabColor = attributes.getColor(R.styleable.FabOptions_fab_color, getThemeAccentColor(context));
        int backgroundColor = attributes.getColor(R.styleable.FabOptions_background_color, fabColor);

        setBackgroundColor(context, backgroundColor);
        mFab.setBackgroundTintList(ColorStateList.valueOf(fabColor));
    }

    public void setFabColor(@ColorRes int fabColor) {
        Context context = getContext();
        if (context != null) {
            @ColorInt int colorId = ContextCompat.getColor(context, fabColor);
            mFab.setBackgroundTintList(ColorStateList.valueOf(colorId));
        }
    }

    public void setBackgroundColor(@ColorRes int backgroundColor) {
        Context context = getContext();
        if (context != null) {
            @ColorInt int color = ContextCompat.getColor(context, backgroundColor);
            setBackgroundColor(context, color);
        } else {
            Log.w(TAG, "Couldn't set background color, context is null");
        }
    }


    private void setBackgroundColor(Context context, @ColorInt int backgroundColor) {
        Drawable backgroundShape = ContextCompat.getDrawable(context, R.drawable.faboptions_background);
        backgroundShape.setColorFilter(backgroundColor, PorterDuff.Mode.ADD);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mBackground.setBackground(backgroundShape);
        } else {
            mBackground.setBackgroundDrawable(backgroundShape);
        }
    }

    @ColorInt
    private int getThemeAccentColor(final Context context) {
        final TypedValue value = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.colorAccent, value, true);
        return value.data;
    }

    private void inflateButtonsFromAttrs(Context context, TypedArray attributes) {
        if (attributes.hasValue(R.styleable.FabOptions_button_menu)) {
            setButtonsMenu(context, attributes.getResourceId(R.styleable.FabOptions_button_menu, 0));
        }
    }

    public boolean setButtonColor(int buttonId, @ColorRes int color) {
        for (int i = 0; i < mButtonContainer.getChildCount(); i++) {
            if (mMenu.getItem(i).getItemId() == buttonId) {
                return styleButton(i, color);
            }
        }
        Log.d(TAG, "setButtonColor(): Couldn't find button with id " + buttonId);
        return false;
    }

    public void setButtonsMenu(@MenuRes int menuId) {
        Context context = getContext();
        if (context != null) {
            setButtonsMenu(context, menuId);
        } else {
            Log.w(TAG, "Couldn't set buttons, context is null");
        }
    }

    /**
     * Deprecated. Use {@link #setButtonsMenu(int)} instead.
     */
    @Deprecated
    public void setButtonsMenu(Context context, @MenuRes int menuId) {
        mMenu = new MenuBuilder(context);
        SupportMenuInflater menuInf = new SupportMenuInflater(context);
        menuInf.inflate(menuId, mMenu);
        addButtonsFromMenu(context, mMenu);
        mSeparator = mButtonContainer.addSeparator(context);
        animateButtons(false);
    }

    private void addButtonsFromMenu(Context context, Menu menu) {
        for (int i = 0; i < menu.size(); i++) {
            addButton(context, menu.getItem(i));
        }
    }

    private void addButton(Context context, MenuItem menuItem) {
        AppCompatImageView button = mButtonContainer.addButton(context, menuItem.getItemId(),
                menuItem.getTitle(), menuItem.getIcon());
        button.setOnClickListener(this);
    }

    private boolean styleButton(int buttonIndex, @ColorRes int color) {
        if (buttonIndex >= (mButtonContainer.getChildCount() / 2)) {
            // Hacky way to deal with the separator view index
            buttonIndex++;
        }

        if (buttonIndex >= mButtonContainer.getChildCount()) {
            Log.e(TAG, "Button at " + buttonIndex + " is null (index out of bounds)");
            return false;
        }

        AppCompatImageView imageView = (AppCompatImageView) mButtonContainer.getChildAt(buttonIndex);
        imageView.setColorFilter(ContextCompat.getColor(getContext(), color));
        return true;
    }

    @Override
    public void onClick(View v) {
        if (!mIsAnimating) {
            mIsAnimating = true;

            if (v.getId() == R.id.faboptions_fab) {
                if (mIsOpen) {
                    close();
                } else {
                    open();
                }
            } else {
                if (mListener != null && mIsOpen) {
                    mListener.onClick(v);
                    close();
                }
            }
        }
    }

    @Override
    public void setOnClickListener(View.OnClickListener listener) {
        mListener = listener;
    }

    private void open() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AnimatedVectorDrawable drawable = (AnimatedVectorDrawable) getResources()
                    .getDrawable(R.drawable.faboptions_ic_menu_animatable, null);
            mFab.setImageDrawable(drawable);
            drawable.start();
        } else {
            mFab.setImageResource(R.drawable.faboptions_ic_close);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            final TransitionSet transitionSet = new OpenMorphTransition(mButtonContainer);
            transitionSet.addListener(new Transition.TransitionListener() {
                @Override
                public void onTransitionStart(final Transition transition) {
                }

                @Override
                public void onTransitionEnd(final Transition transition) {
                    mIsAnimating = false;
                }

                @Override
                public void onTransitionCancel(final Transition transition) {
                }

                @Override
                public void onTransitionPause(final Transition transition) {
                }

                @Override
                public void onTransitionResume(final Transition transition) {
                }
            });

            TransitionManager.beginDelayedTransition(this, transitionSet);
        }
        animateBackground(true);
        animateButtons(true);

        mIsOpen = true;
    }

    private void close() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AnimatedVectorDrawable drawable = (AnimatedVectorDrawable) getResources().getDrawable(R.drawable.faboptions_ic_close_animatable, null);
            mFab.setImageDrawable(drawable);
            drawable.start();
        } else {
            mFab.setImageResource(R.drawable.faboptions_ic_overflow);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            final TransitionSet transitionSet = new CloseMorphTransition(mButtonContainer);
            transitionSet.addListener(new Transition.TransitionListener() {
                @Override
                public void onTransitionStart(final Transition transition) {
                }

                @Override
                public void onTransitionEnd(final Transition transition) {
                    mIsAnimating = false;
                }

                @Override
                public void onTransitionCancel(final Transition transition) {
                }

                @Override
                public void onTransitionPause(final Transition transition) {
                }

                @Override
                public void onTransitionResume(final Transition transition) {
                }
            });

            TransitionManager.beginDelayedTransition(this, transitionSet);
        }
        animateButtons(false);
        animateBackground(false);
        mIsOpen = false;
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
    }

    private void animateBackground(final boolean isOpen) {
        ViewGroup.LayoutParams backgroundLayoutParams = mBackground.getLayoutParams();
        backgroundLayoutParams.width = isOpen ? mButtonContainer.getMeasuredWidth() : NO_DIMENSION;
        mBackground.setLayoutParams(backgroundLayoutParams);
    }

    private void openCompatAnimation() {
        ObjectAnimator anim = ObjectAnimator.ofFloat(mBackground, "scaleX", 1.0f);
        anim.setDuration(30000); // duration 3 seconds
        anim.start();
    }


    private void closeCompatAnimation() {
        ObjectAnimator anim = ObjectAnimator.ofFloat(mBackground, "scaleX", 0.0f);
        anim.setDuration(3000);
        anim.start();
        animateButtons(false);
    }

    private void animateButtons(boolean isOpen) {
        for (int i = 0; i < mButtonContainer.getChildCount(); i++) {
            mButtonContainer.getChildAt(i).setScaleX(isOpen ? 1 : 0);
            mButtonContainer.getChildAt(i).setScaleY(isOpen ? 1 : 0);
        }
    }

    public boolean isOpen() {
        return mIsOpen;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private class OpenMorphTransition extends TransitionSet {
        OpenMorphTransition(ViewGroup viewGroup) {

            ChangeBounds changeBound = new ChangeBounds();
            changeBound.excludeChildren(R.id.faboptions_button_container, true);
            addTransition(changeBound);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                ChangeTransform changeTransform = new ChangeTransform();
                for (int i = 0; i < viewGroup.getChildCount(); i++) {
                    changeTransform.addTarget(viewGroup.getChildAt(i));
                }
                addTransition(changeTransform);
            }

            setOrdering(TransitionSet.ORDERING_SEQUENTIAL);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private class CloseMorphTransition extends TransitionSet {
        CloseMorphTransition(ViewGroup viewGroup) {

            ChangeBounds changeBound = new ChangeBounds();
            changeBound.excludeChildren(R.id.faboptions_button_container, true);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                ChangeTransform changeTransform = new ChangeTransform();
                for (int i = 0; i < viewGroup.getChildCount(); i++) {
                    changeTransform.addTarget(viewGroup.getChildAt(i));
                }
                changeTransform.setDuration(CLOSE_MORPH_TRANSFORM_DURATION);
                addTransition(changeTransform);
            }

            addTransition(changeBound);
            setOrdering(TransitionSet.ORDERING_TOGETHER);
        }
    }
}