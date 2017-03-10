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

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;


public final class PermissionUtil
{
    public static boolean hasPermission(
            Context context,
            String permission)
    {

        PackageManager pm = context.getPackageManager();
        if (pm == null) {
            if (ConstantsUI.DEBUG_MODE) {
                Log.d(ConstantsUI.TAG, "Permission " + permission + " is not granted");
            }
            return false;
        }
        int hasPerm = pm.checkPermission(permission, context.getPackageName());
        if (ConstantsUI.DEBUG_MODE) {
            Log.d(ConstantsUI.TAG, "Permission " + permission + " is " +
                    (hasPerm == PackageManager.PERMISSION_GRANTED ? "granted" : "not granted"));
        }
        return hasPerm == PackageManager.PERMISSION_GRANTED;
    }


    public static boolean isPermissionGranted(
            Context context,
            String permission)
    {
        return ContextCompat.checkSelfPermission(context, permission)
                == PackageManager.PERMISSION_GRANTED;
    }


    public static void requestPermissions(
            final Activity activity,
            int title,
            int message,
            final int requestCode,
            final String... permissions)
    {
        boolean shouldShowDialog = false;
        for (String permission : permissions) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                shouldShowDialog = true;
                break;
            }
        }

        if (shouldShowDialog) {
            AlertDialog builder = new AlertDialog.Builder(activity).setTitle(title)
                    .setMessage(message)
                    .setPositiveButton(android.R.string.ok, null)
                    .create();
            builder.setCanceledOnTouchOutside(false);
            builder.show();

            builder.setOnDismissListener(new DialogInterface.OnDismissListener()
            {
                @Override
                public void onDismiss(DialogInterface dialog)
                {
                    ActivityCompat.requestPermissions(activity, permissions, requestCode);
                }
            });
        } else {
            ActivityCompat.requestPermissions(activity, permissions, requestCode);
        }
    }
}
