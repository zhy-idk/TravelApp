package com.example.travelapp;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;

public class FileUtils {
    public static File getFileFromUri(Context context, Uri uri) {
        File file = null;
        String filePath = null;

        if (uri.getScheme().equals("file")) {
            filePath = uri.getPath();
        } else if (uri.getScheme().equals("content")) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                if (idx != -1) {
                    filePath = cursor.getString(idx);
                }
                cursor.close();
            }
        }

        if (filePath != null) {
            file = new File(filePath);
        }

        return file;
    }
}
