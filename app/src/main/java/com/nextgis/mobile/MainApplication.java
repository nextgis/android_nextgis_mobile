/*
 * Project:  NextGIS Mobile
 * Purpose:  Mobile GIS for Android.
 * Author:   Dmitry Baryshnikov (aka Bishop), bishop.dev@gmail.com
 * Author:   NikitaFeodonit, nfeodonit@yandex.com
 * Author:   Stanislav Petriakov, becomeglory@gmail.com
 * ****************************************************************************
 * Copyright (c) 2012-2017 NextGIS, info@nextgis.com
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.nextgis.mobile;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;
import com.joshdholtz.sentry.Sentry;
import com.nextgis.libngui.GISApplication;
import com.nextgis.libngui.util.ConstantsUI;
import com.nextgis.mobile.activity.SettingsActivity;
import com.nextgis.libngui.util.SettingsConstantsUI;
import com.nextgis.mobile.util.AppSettingsConstants;
import com.nextgis.ngsandroid.NgsAndroidJni;
import com.nextgis.store.bindings.Api;
import com.nextgis.store.bindings.Options;

import java.io.File;


public class MainApplication
        extends GISApplication
{
    @Override
    public void onCreate()
    {
        super.onCreate();

        Sentry.init(this, BuildConfig.SENTRY_DSN);
        Sentry.captureMessage("NGM3 Sentry is init.", Sentry.SentryEventLevel.DEBUG);

        NgsAndroidJni.initLogger();
        Log.d(ConstantsUI.TAG, "NGS version: " + Api.ngsGetVersionString(null));

        updateFromOldVersion();

        initNgs();
    }


    public String getApkPath()
    {
        try {
            return getPackageManager().getApplicationInfo(getPackageName(), 0).sourceDir;
        } catch (PackageManager.NameNotFoundException e) {
            Log.d(ConstantsUI.TAG, e.getLocalizedMessage());
            return null;
        }
    }


    public String getMapPath()
    {
        File defaultPath = getExternalFilesDir("");
        if (defaultPath == null) {
            defaultPath = new File(getFilesDir(), "");
        }
        return defaultPath.getPath();
    }


    public String getGdalPath()
    {
        return "/vsizip" + getApkPath() + "/assets/gdal_data";
    }


    protected void initNgs()
    {
        Api.ngsInit(getGdalPath(), null);
        Api.ngsSetOptions(Options.OPT_DEBUGMODE);
        Log.d(ConstantsUI.TAG, "NGS formats: " + Api.ngsGetVersionString("formats"));
    }


    @Override
    public void onLowMemory()
    {
        // TODO: ngsOnLowMemory
        Log.d(ConstantsUI.TAG, "onLowMemory() is fired");
//        Api.ngsOnLowMemory();
        super.onLowMemory();
    }


    @Override
    public void showSettings(String settings)
    {
        if (TextUtils.isEmpty(settings)) {
            settings = SettingsConstantsUI.ACTION_PREFS_GENERAL;
        }

        switch (settings) {
            case SettingsConstantsUI.ACTION_PREFS_GENERAL:
            case SettingsConstantsUI.ACTION_PREFS_LOCATION:
            case SettingsConstantsUI.ACTION_PREFS_TRACKING:
                break;
            default:
                return;
        }

        Intent intent = new Intent(this, SettingsActivity.class);
        intent.setAction(settings);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    private void updateFromOldVersion()
    {
        try {
            int currentVersionCode =
                    getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
            int savedVersionCode =
                    mSharedPreferences.getInt(AppSettingsConstants.KEY_PREF_APP_VERSION, 0);

            switch (savedVersionCode) {
                case 0:
                default:
                    break;
            }

            if (savedVersionCode < currentVersionCode) {
                mSharedPreferences.edit()
                        .putInt(AppSettingsConstants.KEY_PREF_APP_VERSION, currentVersionCode)
                        .apply();
            }
        } catch (PackageManager.NameNotFoundException ignored) {
        }
    }
}
