package com.example.arrowstatsaver;

import android.content.Context;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

public class UpdateHelper {

    public static String KEY_UPDATE_ENABLE = "isUpdate";
    public static String KEY_UPDATE_VERSION = "version";
    public static String KEY_UPDATE_URL = "update_url";

    public interface onUpdateCheckListener {
        void onUpdateCheckListener(String urlApp);
    }

    public static Builder with(UpdateHelper.onUpdateCheckListener context) {
        return new Builder(context);
    }

    private onUpdateCheckListener onUpdateCheckListener;
    private Context context;

    public UpdateHelper(UpdateHelper.onUpdateCheckListener onUpdateCheckListener, UpdateHelper.onUpdateCheckListener context) {
        this.onUpdateCheckListener = onUpdateCheckListener;
        this.context = (Context) context;
    }

    public void check() {
        FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.getInstance();
        if (remoteConfig.getBoolean(KEY_UPDATE_ENABLE)) {
            String currentVersion = remoteConfig.getString(KEY_UPDATE_VERSION);
            String appVersion = getAppVersion(context);
            String updateUrl = remoteConfig.getString(KEY_UPDATE_URL);

            if (!TextUtils.equals(currentVersion, appVersion) && onUpdateCheckListener != null)
                onUpdateCheckListener.onUpdateCheckListener(updateUrl);
        }
    }

    private String getAppVersion(Context context) {
        String resuult = "";

        try {
            resuult = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
            resuult = resuult.replaceAll("[a-zA-z] |-", "");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return resuult;
    }

    public static class Builder {
        private UpdateHelper.onUpdateCheckListener context;
        private onUpdateCheckListener onUpdateCheckListener;

        public Builder(UpdateHelper.onUpdateCheckListener context) {
            this.context = context;
        }

        public Builder onUpdateCheck(onUpdateCheckListener onUpdateCheckListener) {
            this.onUpdateCheckListener = onUpdateCheckListener;
            return this;
        }

        public UpdateHelper build() {
            return new UpdateHelper(context, onUpdateCheckListener);
        }

        public UpdateHelper check() {
            UpdateHelper updateHelper = build();
            updateHelper.check();

            return updateHelper;
        }
    }
}
