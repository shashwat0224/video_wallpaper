package com.example.video_wallpaper;

import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.plugin.common.MethodChannel;

import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import androidx.annotation.NonNull;

public class MainActivity extends FlutterActivity {

    private static final String CHANNEL = "com.example.video_wallpaper/wallpaper";

    @Override
    public void configureFlutterEngine(@NonNull FlutterEngine flutterEngine) {
        super.configureFlutterEngine(flutterEngine);

        new MethodChannel(flutterEngine.getDartExecutor().getBinaryMessenger(), CHANNEL).setMethodCallHandler((call, result) -> {
            if (call.method.equals("applyWallpaper")) {
                try {
                    Intent intent = new Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
                    intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT, new ComponentName(this, VideoWallpaperService.class));
                    startActivity(intent);
                    result.success(true);
                } catch (Exception e) {
                    result.error("ERROR", "Failed to open wallpaper chooser", null);
                }
            } else {
                result.notImplemented();
            }
        });
    }
}
