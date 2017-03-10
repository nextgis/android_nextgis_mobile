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

package com.nextgis.libngui;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.nextgis.libngui.api.IGISApplication;
import com.nextgis.libngui.util.SettingsConstantsUI;
import com.nextgis.libngui.util.ThemeUtil;


/**
 * This is a base application class. Each application should inherited their base application from
 * this class.
 * <p>
 * The main application class stored some singleton objects.
 *
 * @author Dmitry Baryshnikov (aka Bishop), bishop.dev@gmail.com
 */
public abstract class GISApplication
        extends Application
        implements IGISApplication
{
    protected SharedPreferences mSharedPreferences;


    @Override
    public void onCreate()
    {
        super.onCreate();

        setTheme(ThemeUtil.getThemeId(ThemeUtil.isDarkTheme(this)));

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (mSharedPreferences.getBoolean(SettingsConstantsUI.KEY_PREF_APP_FIRST_RUN, true)) {
            onFirstRun();
            SharedPreferences.Editor edit = mSharedPreferences.edit();
            edit.putBoolean(SettingsConstantsUI.KEY_PREF_APP_FIRST_RUN, false);
            edit.apply();
        }
    }


    /**
     * Executed then application first run. One can create some data here (some layers, etc.).
     */
    protected void onFirstRun()
    {

    }
}
