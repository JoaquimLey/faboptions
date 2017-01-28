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

package com.joaquimley.sample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.joaquimley.faboptions.FabOptions;
import com.joaquimley.faboptions.sample.R;

import static com.joaquimley.faboptions.sample.R.id.toolbar;

/**
 * Faboptions sample via XML {@see R.layout.activity_sample_xml}
 */

public class XmlSampleActivity extends AppCompatActivity implements View.OnClickListener {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private Toolbar mToolbar;
    private FabOptions mFabOptions;

    public static Intent newStartIntent(Context context) {
        return new Intent(context, XmlSampleActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_xml);
        mToolbar = (Toolbar) findViewById(toolbar);
        mToolbar.setTitle(getString(R.string.title_activity_xml));
        setSupportActionBar(mToolbar);
        mFabOptions = (FabOptions) findViewById(R.id.fab_options);
        mFabOptions.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.faboptions_favorite:
                mFabOptions.setButtonColor(R.id.faboptions_favorite, R.color.colorAccent);
                Toast.makeText(XmlSampleActivity.this, "Favorite", Toast.LENGTH_SHORT).show();
                break;

            case R.id.faboptions_textsms:
                mFabOptions.setButtonColor(R.id.faboptions_textsms, R.color.colorAccent);
                Toast.makeText(XmlSampleActivity.this, "Message", Toast.LENGTH_SHORT).show();
                break;


            case R.id.faboptions_download:
                mFabOptions.setButtonColor(R.id.faboptions_download, R.color.colorAccent);
                Toast.makeText(XmlSampleActivity.this, "Download", Toast.LENGTH_SHORT).show();
                break;


            case R.id.faboptions_share:
                mFabOptions.setButtonColor(R.id.faboptions_share, R.color.colorAccent);
                Toast.makeText(XmlSampleActivity.this, "Share", Toast.LENGTH_SHORT).show();
                break;

            default:
                // no-op
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_snackbar_test:
                Snackbar.make(mToolbar, getString(R.string.action_snackbar_test_message),
                        Snackbar.LENGTH_LONG).show();
                return true;

            case R.id.action_change_activity:
                startActivity(JavaSampleActivity.newStartIntent(XmlSampleActivity.this));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}