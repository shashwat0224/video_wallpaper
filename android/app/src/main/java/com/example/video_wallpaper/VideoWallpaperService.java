package com.example.video_wallpaper;

import android.net.Uri;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.media3.common.C;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector;

public class VideoWallpaperService extends WallpaperService {

    @Override
    public Engine onCreateEngine() {
        return new MyWallpaperEngine();
    }

    private class MyWallpaperEngine extends WallpaperService.Engine {

        private ExoPlayer player;

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);

            DefaultTrackSelector trackSelector = new DefaultTrackSelector(getApplicationContext());

            trackSelector.setParameters(trackSelector.buildUponParameters().setTrackTypeDisabled(C.TRACK_TYPE_AUDIO, true));

            player = new ExoPlayer.Builder(getApplicationContext())
                    .setTrackSelector(trackSelector)
                    .build();

            player.setRepeatMode(Player.REPEAT_MODE_ONE);

            player.setVolume(0f);
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            if (player != null) {
                player.release();
                player = null;
            }
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            if (visible) {
                player.play();
            } else {
                player.pause();
            }
        }

        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            super.onSurfaceCreated(holder);

            player.setVideoSurfaceHolder(holder);

            SharedPreferences prefs = getApplicationContext().getSharedPreferences(
                    "FlutterSharedPreferences", Context.MODE_PRIVATE
            );

            // 2. Get the video path.
            //    The key MUST be prefixed with "flutter."
            String videoPath = prefs.getString("flutter.video_path", null);

            MediaItem mediaItem;
            if (videoPath != null) {
                // 3. We found a path! Create a MediaItem from the local file
                mediaItem = MediaItem.fromUri(Uri.parse(videoPath));
            } else {
                // 4. No path saved, so play the bunny video as a fallback
                String fallbackUrl = "https://storage.googleapis.com/exoplayer-test-media-0/BigBuckBunny_320x180.mp4";
                mediaItem = MediaItem.fromUri(Uri.parse(fallbackUrl));
            }
            player.setMediaItem(mediaItem);
            player.prepare();
            player.play();
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);

        }
    }
}