package com.littlejie.filemanager.util;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by littlejie on 2017/1/7.
 */

public class MediaUtil {

    public static List<File> getImageLibrary(Context context) {
        ArrayList<File> list = new ArrayList<>();

        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String data[] = new String[]{MediaStore.Images.Media.DATA};

        Cursor cursor = new CursorLoader(context, uri, data, null, null, null).loadInBackground();

        if (cursor != null) {

            while (cursor.moveToNext()) {

                File file = new File(cursor.getString(cursor.getColumnIndex(data[0])));

                if (file.exists()) list.add(file);
            }

            cursor.close();
        }

        return list;
    }
}
