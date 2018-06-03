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

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Custom FabOptions buttons ({@link ImageView}) container, enables runtime view insertion
 */

public class FabOptionsButtonContainer extends LinearLayout {

    public FabOptionsButtonContainer(Context context) {
        this(context, null);
    }

    public FabOptionsButtonContainer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FabOptionsButtonContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public AppCompatImageView addButton(Context context, int buttonId, CharSequence title, Drawable drawableIcon) {
        return addButton(context, buttonId, title, drawableIcon, null);
    }

    public AppCompatImageView addButton(Context context, int buttonId, CharSequence title, Drawable drawableIcon, Integer index) {
        AppCompatImageView fabOptionButton =
                (AppCompatImageView) LayoutInflater.from(context).inflate(R.layout.faboptions_button, this, false);

        fabOptionButton.setImageDrawable(drawableIcon);
        fabOptionButton.setContentDescription(title);
        fabOptionButton.setId(buttonId);

        if (index == null) {
            addView(fabOptionButton);
        } else {
            addView(fabOptionButton, index);
        }
        return fabOptionButton;
    }

    public View addSeparator(Context context) {
        View separator = LayoutInflater.from(context).inflate(R.layout.faboptions_separator, this, false);
        addView(separator, (getChildCount() / 2));
        return separator;
    }
}
