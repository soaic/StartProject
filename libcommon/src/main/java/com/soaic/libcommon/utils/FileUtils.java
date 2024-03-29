package com.soaic.libcommon.utils;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import androidx.core.content.FileProvider;

import java.io.File;

public class FileUtils {

    /**
     * 获取临时存储的图片地址
     */
    public static String getTempFilePath(Context context){
        return context.getExternalCacheDir() + File.separator + "temp_" + System.currentTimeMillis() / 1000+".jpg";
    }

    public static Uri getFileUri(Context context, File file) {
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(context, getFileProviderName(context), file);//通过FileProvider创建一个content类型的Uri
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }

    public static String getFileProviderName(Context context) {
        return context.getPackageName() + ".fileProvider";
    }

    /**
     * 获取文件大小
     * @param file
     * @return
     */
    public static int getFileSize(File file) {
        return (int) (file.length() / 1024 / 1024);
    }

    /**
     * 获取文件夹大小
     * @param file
     * @return
     */
    public static int getFolderSize(File file) {
        long size = 0;
        File[] fileList = file.listFiles();
        if (fileList == null) {
            return 0;
        }
        for (int i = 0; i < fileList.length; i++) {
            if (fileList[i].isDirectory()) {
                size = size + getFolderSize(fileList[i]);
            } else {
                size = size + fileList[i].length();
            }
        }
        return (int) (size / 1024 / 1024);
    }

    /**
     * 删除文件或文件夹
     */
    public static boolean deleteFileOrFolder(File file) {
        if (file.isFile()) {
            return file.delete();
        }
        if (file.isDirectory()) {
            File[] childFile = file.listFiles();
            if ((childFile == null) || (childFile.length == 0)) {
                return file.delete();
            }
            for (File f : childFile) {
                deleteFileOrFolder(f);
            }
            return file.delete();
        }
        return false;
    }
}
