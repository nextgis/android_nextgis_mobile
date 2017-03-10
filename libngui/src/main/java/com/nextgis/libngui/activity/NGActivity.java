/*
 * Project:  NextGIS Mobile
 * Purpose:  Mobile GIS for Android.
 * Author:   Dmitry Baryshnikov (aka Bishop), bishop.dev@gmail.com
 * Author:   NikitaFeodonit, nfeodonit@yandex.com
 * Author:   Stanislav Petriakov, becomeglory@gmail.com
 * *****************************************************************************
 * Copyright (c) 2012-2017 NextGIS, info@nextgis.com
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser Public License for more details.
 *
 * You should have received a copy of the GNU Lesser Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.nextgis.libngui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import com.nextgis.libngui.util.ThemeUtil;


/**
 * A base activity for NextGIS derived activities
 */
public class NGActivity
        extends AppCompatActivity
{
    protected String mCurrentTheme;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        rememberCurrentTheme();
        setTheme(ThemeUtil.getThemeId(ThemeUtil.isDarkTheme(this)));
        super.onCreate(savedInstanceState);
    }


    @Override
    protected void onResume()
    {
        refreshCurrentTheme();
        super.onResume();
    }


    // for overriding in a subclass
    protected void rememberCurrentTheme()
    {
        mCurrentTheme = ThemeUtil.getSavedTheme(this);
    }


    // for overriding in a subclass
    protected void refreshCurrentTheme()
    {
        String newTheme = ThemeUtil.getSavedTheme(this);
        if (!newTheme.equals(mCurrentTheme)) {
            refreshActivityView();
        }
    }


    public void refreshActivityView()
    {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }


    /**
     * This hook is called whenever an item in your options menu is selected. The default
     * implementation simply returns false to have the normal processing happen (calling the item's
     * Runnable or sending a message to its Handler as appropriate).  You can use this method for
     * any items for which you would like to do processing without those other facilities. <p/>
     * <p>Derived classes should call through to the base class for it to perform the default menu
     * handling.</p>
     *
     * @param item
     *         The menu item that was selected.
     *
     * @return boolean Return false to allow normal menu processing to proceed, true to consume it
     * here.
     *
     * @see #onCreateOptionsMenu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (isHomeEnabled()) {
                    finish();
                    return true;
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    protected boolean isHomeEnabled()
    {
        return true;
    }


    protected void setToolbar(int toolbarId)
    {
        Toolbar toolbar = (Toolbar) findViewById(toolbarId);
        if (null == toolbar) {
            return;
        }
        toolbar.getBackground().setAlpha(getToolbarAlpha());
        setSupportActionBar(toolbar);

        if (null != getSupportActionBar()) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }


    protected int getToolbarAlpha()
    {
        return 255; // not transparent
    }
}
