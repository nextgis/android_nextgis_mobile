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

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.URLSpan;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;


public final class ControlHelper
{
    private final static String BUNDLE_SAVED_STATE = "nextgis_control_";


    public static String getPercentValue(
            Context context,
            int stringLabel,
            float value)
    {
        return context.getString(stringLabel) + ": " + ((int) value * 100 / 255) + "%";
    }


    public static void setEnabled(
            MenuItem item,
            boolean state)
    {
        if (null == item) {
            return;
        }
        item.setEnabled(state);
        item.getIcon().setAlpha(state ? 255 : 160);
    }


    public static boolean hasKey(
            Bundle savedState,
            String fieldName)
    {
        return savedState != null && savedState.containsKey(getSavedStateKey(fieldName));
    }


    public static String getSavedStateKey(String fieldName)
    {
        return BUNDLE_SAVED_STATE + fieldName;
    }


    public static Bitmap getBitmap(
            InputStream is,
            BitmapFactory.Options options)
    {
        Bitmap result = null;
        if (is == null) {
            return null;
        }

        try {
            result = BitmapFactory.decodeStream(is, null, options);
        } catch (OutOfMemoryError oom) {
            oom.printStackTrace();

            try {
                options.inSampleSize *= 4;
                result = BitmapFactory.decodeStream(is, null, options);
            } catch (OutOfMemoryError oom1) {
                oom1.printStackTrace();
            }
        }

        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }


    @SuppressWarnings("deprecation")
    public static BitmapFactory.Options getOptions(
            InputStream is,
            int width,
            int height)
    {
        if (is == null) {
            return null;
        }

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, null, options);

        if (height == 0) {
            height = (int) ((1f * width * options.outHeight / options.outWidth));
        } else if (width == 0) {
            width = (int) ((1f * height * options.outWidth / options.outHeight));
        }

        options.inSampleSize = calculateInSampleSize(options, width, height);
        options.inJustDecodeBounds = false;
        options.inDither = false;
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[32 * 1024];
        return options;
    }


    public static int calculateInSampleSize(
            BitmapFactory.Options options,
            int reqWidth,
            int reqHeight)
    {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }


    public static int getColor(
            Context context,
            int attr)
    {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();
        theme.resolveAttribute(attr, typedValue, true);
        return typedValue.data;
    }


    public static int dpToPx(
            int dp,
            Resources resources)
    {
        DisplayMetrics dm = resources.getDisplayMetrics();
        return Math.round(dp * (dm.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }


    // http://stackoverflow.com/a/32973351/2088273
    public static AppCompatActivity getActivity(Context context)
    {
        while (context instanceof ContextWrapper) {
            if (context instanceof AppCompatActivity) {
                return (AppCompatActivity) context;
            }

            context = ((ContextWrapper) context).getBaseContext();
        }

        return null;
    }


    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public static void lockScreenOrientation(Activity activity)
    {
        WindowManager windowManager =
                (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        Configuration configuration = activity.getResources().getConfiguration();
        int rotation = windowManager.getDefaultDisplay().getRotation();

        // Search for the natural position of the device
        if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE && (
                rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_180)
                || configuration.orientation == Configuration.ORIENTATION_PORTRAIT && (
                rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270)) {
            // Natural position is Landscape
            switch (rotation) {
                case Surface.ROTATION_0:
                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    break;
                case Surface.ROTATION_90:
                    activity.setRequestedOrientation(
                            ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
                    break;
                case Surface.ROTATION_180:
                    activity.setRequestedOrientation(
                            ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
                    break;
                case Surface.ROTATION_270:
                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    break;
            }
        } else {
            // Natural position is Portrait
            switch (rotation) {
                case Surface.ROTATION_0:
                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    break;
                case Surface.ROTATION_90:
                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    break;
                case Surface.ROTATION_180:
                    activity.setRequestedOrientation(
                            ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
                    break;
                case Surface.ROTATION_270:
                    activity.setRequestedOrientation(
                            ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
                    break;
            }
        }
    }


    public static void unlockScreenOrientation(Activity activity)
    {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    }


    public static void setClearAction(final EditText target)
    {
        target.setOnTouchListener(new View.OnTouchListener()
        {
            final int RIGHT = 2;


            @Override
            public boolean onTouch(
                    View view,
                    MotionEvent event)
            {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    int leftEdgeOfRightDrawable =
                            view.getRight() - target.getCompoundDrawables()[RIGHT].getBounds()
                                    .width();
                    // when EditBox has padding, adjust leftEdge like
                    // leftEdgeOfRightDrawable -= getResources().getDimension(R.dimen.edittext_padding_left_right);
                    if (event.getRawX() >= leftEdgeOfRightDrawable) {
                        // clicked on clear icon
                        target.setText("");
                        target.clearFocus();
                        return false;
                    }
                }
                return false;
            }
        });
    }


    public static void highlightText(TextView textView)
    {
        final CharSequence text = textView.getText();
        final SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(
                new URLSpan(""), 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(spannableString, TextView.BufferType.SPANNABLE);
    }

}
