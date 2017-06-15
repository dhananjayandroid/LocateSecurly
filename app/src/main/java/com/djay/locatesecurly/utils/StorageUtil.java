package com.djay.locatesecurly.utils;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;

import com.djay.locatesecurly.App;
import com.djay.locatesecurly.BuildConfig;

import java.io.File;

public class StorageUtil {

    public static String getAppExternalDataDirectoryPath() {
        StringBuilder sb = new StringBuilder();
        sb.append(Environment.getExternalStorageDirectory()).append(File.separator).append("Android")
                .append(File.separator).append("data").append(File.separator)
                .append(App.getInstance().getPackageName()).append(File.separator)
                .append("RECORDINGS").append(File
                .separator);

        return sb.toString();
    }

    public static File getAppExternalDataDirectoryFile() {
        File dataDirectoryFile = new File(getAppExternalDataDirectoryPath());
        if (!dataDirectoryFile.exists())
            dataDirectoryFile.mkdirs();

        return dataDirectoryFile;
    }

    public static Uri uriFromFile(Context pContext, File file) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return FileProvider.getUriForFile(pContext,
                    BuildConfig.APPLICATION_ID + ".provider",
                    file);
        } else {
            return Uri.fromFile(file);
        }
    }
}