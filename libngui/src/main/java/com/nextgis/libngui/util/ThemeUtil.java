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

package com.nextgis.libngui.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.PreferenceManager;
import com.nextgis.libngui.R;


public class ThemeUtil
{
    public static int getThemeId(boolean isDark)
    {
        if (isDark) {
            return R.style.Theme_NextGIS_AppCompat_Dark;
        } else {
            return R.style.Theme_NextGIS_AppCompat_Light;
        }
    }


    public static String getSavedTheme(Context context)
    {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(SettingsConstantsUI.KEY_PREF_THEME, SettingsConstantsUI.THEME_LIGHT);
    }


    public static boolean isDarkTheme(Context context)
    {
        return getSavedTheme(context).equals(SettingsConstantsUI.THEME_DARK);
    }


    public static int getDialogTheme(
            Context context,
            int theme)
    {
        int dialogTheme = R.style.Theme_NextGIS_AppCompat_Light_Dialog;
        int[] attrs = {android.R.attr.alertDialogStyle};
        TypedArray ta = context.obtainStyledAttributes(theme, attrs);
        dialogTheme = ta.getResourceId(0, dialogTheme);
        ta.recycle();

        return dialogTheme;
    }
}
