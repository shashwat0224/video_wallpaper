package com.example.video_wallpaper;

import android.net.Uri;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.media3.common.C;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector;

public class VideoWallpaperService extends WallpaperService {

    @Override
    public Engine onCreateEngine() {
        return new MyWallpaperEngine();
    }

    private class MyWallpaperEngine extends WallpaperService.Engine {

        private ExoPlayer player;

        private String currentVideoPath = "";

        @UnstableApi
        @Override
        public void onCreate(final SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);

            final DefaultTrackSelector trackSelector = new DefaultTrackSelector(VideoWallpaperService.this.getApplicationContext());
            trackSelector.setParameters(trackSelector.buildUponParameters().setTrackTypeDisabled(C.TRACK_TYPE_AUDIO, true));

            this.player = new ExoPlayer.Builder(VideoWallpaperService.this.getApplicationContext())
                    .setTrackSelector(trackSelector)
                    .build();

            this.player.setRepeatMode(Player.REPEAT_MODE_ONE);
            this.player.setVolume(0.0f);
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            if (null != player) {
                this.player.release();
                this.player = null;
            }
        }


        @Override
        public void onVisibilityChanged(final boolean visible) {
            if (visible) {
                this.loadVideoFromPreferences();
                this.player.play();
            } else {
                this.player.pause();
            }
        }

        @Override
        public void onSurfaceCreated(final SurfaceHolder holder) {
            super.onSurfaceCreated(holder);
            this.player.setVideoSurfaceHolder(holder);
        }

        @Override
        public void onSurfaceDestroyed(final SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);

        }

        private void loadVideoFromPreferences() {
            final SharedPreferences prefs = VideoWallpaperService.this.getApplicationContext().getSharedPreferences(
                    "FlutterSharedPreferences", MODE_PRIVATE
            );

            // Get the latest path from Flutter
            String newPath = prefs.getString("flutter.video_path", null);

            // Fallback URL if nothing is saved
            if (null == newPath) {
                newPath = "https://storage.googleapis.com/exoplayer-test-media-0/BigBuckBunny_320x180.mp4";
            }

            // CRITICAL CHECK: Only reload if the path is DIFFERENT from what is playing
            if (!newPath.equals(this.currentVideoPath)) {

                // Update our tracker
                this.currentVideoPath = newPath;

                // Load the new video into ExoPlayer
                final MediaItem mediaItem = MediaItem.fromUri(Uri.parse(newPath));
                this.player.setMediaItem(mediaItem);
                this.player.prepare();
                // We don't need player.play() here because onVisibilityChanged calls it right after
            }
        }
    }
}