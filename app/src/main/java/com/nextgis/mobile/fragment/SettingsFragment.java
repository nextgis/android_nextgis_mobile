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

package com.nextgis.mobile.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.preference.PreferenceScreen;
import com.nextgis.libngui.fragment.NGPreferenceSettingsFragment;
import com.nextgis.libngui.util.SettingsConstantsUI;
import com.nextgis.mobile.MainApplication;
import com.nextgis.mobile.R;
import com.nextgis.mobile.util.AppSettingsConstants;
import com.nextgis.mobile.util.SelectMapPathPreference;
import com.nextgis.mobile.util.ApkDownloader;

import java.io.File;


public class SettingsFragment
        extends NGPreferenceSettingsFragment
        implements SelectMapPathPreference.OnAttachedListener
{
    @Override
    protected void createPreferences(PreferenceScreen screen)
    {
        switch (mAction) {
            case SettingsConstantsUI.ACTION_PREFS_GENERAL:
                addPreferencesFromResource(R.xml.preferences_general);

                final ListPreference theme =
                        (ListPreference) findPreference(SettingsConstantsUI.KEY_PREF_THEME);
                initializeTheme(getActivity(), theme);
                final Preference reset =
                        findPreference(SettingsConstantsUI.KEY_PREF_RESET_SETTINGS);
                initializeReset(getActivity(), reset);
                break;

//            case SettingsConstantsUI.ACTION_PREFS_UPDATE:
//                ApkDownloader.check(getActivity(), true);
//                break;
        }
    }


    @Override
    public FragmentManager getFragmentManagerFromParentFragment()
    {
        return getFragmentManager();
    }


    public static void initializeTheme(
            final Activity activity,
            final ListPreference theme)
    {
        if (null != theme) {
            theme.setSummary(theme.getEntry());

            theme.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener()
            {
                @Override
                public boolean onPreferenceChange(
                        Preference preference,
                        Object newValue)
                {
                    activity.startActivity(activity.getIntent());
                    activity.finish();
                    return true;
                }
            });
        }
    }


    public static void initializeReset(
            final Activity activity,
            final Preference preference)
    {
        if (null != preference) {
            preference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
            {
                @Override
                public boolean onPreferenceClick(Preference preference)
                {
                    AlertDialog.Builder confirm = new AlertDialog.Builder(activity);
                    confirm.setTitle(R.string.reset_settings_title)
                            .setMessage(R.string.reset_settings_message)
                            .setNegativeButton(android.R.string.cancel, null)
                            .setPositiveButton(android.R.string.ok,
                                    new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(
                                                DialogInterface dialog,
                                                int which)
                                        {
                                            resetSettings(activity);
//                                            deleteLayers(activity);
//                                            ((MainApplication) activity.getApplication()).initBaseLayers();
                                        }
                                    })
                            .show();
                    return false;
                }
            });
        }
    }


    protected static void resetSettings(Activity activity)
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(SettingsConstantsUI.KEY_PREF_THEME);
        editor.remove(SettingsConstantsUI.KEY_PREF_COMPASS_TRUE_NORTH);
        editor.remove(SettingsConstantsUI.KEY_PREF_COMPASS_MAGNETIC);
        editor.remove(SettingsConstantsUI.KEY_PREF_COMPASS_KEEP_SCREEN);
        editor.remove(SettingsConstantsUI.KEY_PREF_COMPASS_VIBRATE);
        editor.remove(SettingsConstantsUI.KEY_PREF_SHOW_STATUS_PANEL);
        editor.remove(SettingsConstantsUI.KEY_PREF_SHOW_CURRENT_LOC);
        editor.remove(AppSettingsConstants.KEY_PREF_SHOW_COMPASS);
        editor.remove(SettingsConstantsUI.KEY_PREF_KEEPSCREENON);
        editor.remove(SettingsConstantsUI.KEY_PREF_COORD_FORMAT);
        editor.remove(AppSettingsConstants.KEY_PREF_SHOW_ZOOM_CONTROLS);
        editor.remove(SettingsConstantsUI.KEY_PREF_LOCATION_SOURCE);
        editor.remove(SettingsConstantsUI.KEY_PREF_LOCATION_MIN_TIME);
        editor.remove(SettingsConstantsUI.KEY_PREF_LOCATION_MIN_DISTANCE);
        editor.remove(SettingsConstantsUI.KEY_PREF_LOCATION_ACCURATE_COUNT);
        editor.remove(SettingsConstantsUI.KEY_PREF_TRACKS_SOURCE);
        editor.remove(SettingsConstantsUI.KEY_PREF_TRACKS_MIN_TIME);
        editor.remove(SettingsConstantsUI.KEY_PREF_TRACKS_MIN_DISTANCE);
        editor.remove(SettingsConstantsUI.KEY_PREF_TRACK_RESTORE);
        editor.remove(AppSettingsConstants.KEY_PREF_SHOW_MEASURING);
        editor.remove(AppSettingsConstants.KEY_PREF_SHOW_SCALE_RULER);
        editor.remove(SettingsConstantsUI.KEY_PREF_SHOW_GEO_DIALOG);
        editor.remove(AppSettingsConstants.KEY_PREF_GA);

        File defaultPath = activity.getExternalFilesDir(SettingsConstantsUI.KEY_PREF_MAP);
        if (defaultPath == null) {
            defaultPath = new File(activity.getFilesDir(), SettingsConstantsUI.KEY_PREF_MAP);
        }

        editor.putString(SettingsConstantsUI.KEY_PREF_MAP_PATH, defaultPath.getPath());
        editor.apply();

        PreferenceManager.setDefaultValues(activity, R.xml.preferences_general, true);

//        moveMap(activity, defaultPath);
    }
}
