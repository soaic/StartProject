package com.soaic.libcommon.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.LocaleList;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.soaic.libcommon.bean.AppInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AppUtils {

    public static void switchLanguage(Context context, String language) {
        if (TextUtils.isEmpty(language)) {
            return;
        }
        Locale myLocale = new Locale(language);
        Resources res = context.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            conf.setLocale(myLocale);
        } else {
            conf.locale = myLocale;
        }
        res.updateConfiguration(conf, dm);
        setAppLanguage(context, language);
    }

    public static String getAppLanguage(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString("language", getSystemLanguage());
    }

    private static void setAppLanguage(Context context, String language) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("language", language);
        editor.apply();
        editor.commit();
    }

    public static String getSystemLanguage() {
        Locale locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = LocaleList.getDefault().get(0);
        } else {
            locale = Locale.getDefault();
        }
        return locale.getLanguage() + "-" + locale.getCountry();
    }

    public static String getAssetsToString(Context context, String fileName) {
        try {
            return ConvertUtils.inputStreamToStr(context.getAssets().open(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static int getAppVersionCode(Context context) {
        PackageManager pm = context.getPackageManager();
        PackageInfo pi;
        try {
            pi = pm.getPackageInfo(context.getPackageName(), 0);
            return pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }


    public static String getApplicationMetaData(Context context, String metaDataKey, String defaultData) {
        String result = defaultData;
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            result = String.valueOf(appInfo.metaData.get(metaDataKey));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取本机所有邮件App
     * @return apps
     */
    public static List<AppInfo> getMailApps(Context context) {
        List<AppInfo> mApps = new ArrayList<>();
        Uri uri = Uri.parse("mailto:");
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        List<ResolveInfo> resolveInfos = context.getPackageManager().queryIntentActivities(intent, PackageManager.GET_RESOLVED_FILTER);

        AppInfo appInfo;
        for (ResolveInfo resolveInfo : resolveInfos) {
            appInfo = new AppInfo();
            appInfo.icon = resolveInfo.loadIcon(context.getPackageManager());
            appInfo.label = resolveInfo.loadLabel(context.getPackageManager()).toString();
            appInfo.packageName = resolveInfo.activityInfo.packageName;
            mApps.add(appInfo);
        }
        return mApps;
    }

    /**
     * 通过 packageName 跳转app
     * @param packageName 包名
     */
    public static void goAppByPackageName(Context context, String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            return;
        }
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        if (intent != null)
            context.startActivity(intent);
    }
}
