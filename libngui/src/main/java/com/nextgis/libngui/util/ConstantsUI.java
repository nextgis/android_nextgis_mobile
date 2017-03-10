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

import com.nextgis.libngui.BuildConfig;


public interface ConstantsUI
{
    String  TAG        = "nextgismobile";
    int     NOT_FOUND  = -1;
    boolean DEBUG_MODE = true;

    /**
     * File types
     */
    int FILETYPE_PARENT  = 1 << 0;
    int FILETYPE_FOLDER  = 1 << 1;
    int FILETYPE_ZIP     = 1 << 2;
    int FILETYPE_GEOJSON = 1 << 3;
    int FILETYPE_FB      = 1 << 4;
    int FILETYPE_SHP     = 1 << 5;
    int FILETYPE_UNKNOWN = 1 << 31;

    int FILETYPE_ALL_FILE_TYPES =
            FILETYPE_FB | FILETYPE_GEOJSON | FILETYPE_ZIP | FILETYPE_SHP | FILETYPE_UNKNOWN;

    String FRAGMENT_NGW_HEADER_SETTINGS  = "ngw_header_settings";
    String FRAGMENT_NGW_SETTINGS         = "ngw_settings";
    String FRAGMENT_NGID_HEADER_SETTINGS = "ngid_header_settings";
    String FRAGMENT_NGID_SETTINGS        = "ngid_settings";
    String FRAGMENT_NGID_LOGIN           = "ngid_login";
    String FRAGMENT_SELECT_RESOURCE      = "select_resource";

    String PREF_SCREEN_TITLE = "pref_screen_title";

    /**
     * HTTP parameters
     */
    String APP_USER_AGENT     = "NextGIS Mobile maplib v" + BuildConfig.VERSION_NAME;
    int    IO_BUFFER_SIZE     = 32 * 1024; // 32k
    int    MAX_CONTENT_LENGTH = 5 * 1024 * 1024; //5Mb

    String TILE_EXT = ".tile";
}
