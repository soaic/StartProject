package com.soaic.startproject.utils;


import com.soaic.startproject.SApplication;
import com.soaic.startproject.base.AppConfigs;
import com.soaic.startproject.base.AppConstants;
import com.soaic.libcommon.utils.SPUtils;

public class AppUtils {

    public static String getServerAddress() {
        String svr;
        SPUtils spUtils = SPUtils.getInstance(SApplication.Companion.getApplication());
        if (AppConstants.IsReleaseModel) {
            svr = spUtils.getString(AppConfigs.ServerEnv, AppConstants.Value.RELEASE_SERVER);
        } else if (AppConstants.IsQAModel) {
            svr = spUtils.getString(AppConfigs.ServerEnv, AppConstants.Value.QA_SERVER);
        } else if (AppConstants.IsStagModel) {
            svr = spUtils.getString(AppConfigs.ServerEnv, AppConstants.Value.STAG_SERVER);
        } else {
            svr = spUtils.getString(AppConfigs.ServerEnv, AppConstants.Value.DEV_SERVER);
        }
        return svr;
    }


}
