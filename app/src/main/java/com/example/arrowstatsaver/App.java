package com.example.arrowstatsaver;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import java.util.HashMap;
import java.util.Map;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.getInstance();

        Map<String, Object> defaultvalue = new HashMap<>();
        defaultvalue.put(UpdateHelper.KEY_UPDATE_ENABLE , false);
        defaultvalue.put(UpdateHelper.KEY_UPDATE_VERSION, 1.0);
        defaultvalue.put(UpdateHelper.KEY_UPDATE_URL, "https://firebasestorage.googleapis.com/v0/b/statsaver-a23f7.appspot.com/o/app-debug.apk?alt=media&token=c2da5828-1724-42ad-bfbb-a2e0acc56a6b" );

        remoteConfig.setDefaultsAsync(defaultvalue);
        remoteConfig.fetch(5)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            remoteConfig.fetchAndActivate();
                        }
                    }
                });

    }
}
