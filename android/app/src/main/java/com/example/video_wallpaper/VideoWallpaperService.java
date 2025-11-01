package com.example.video_wallpaper;

import android.net.Uri;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;

import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.exoplayer.ExoPlayer;

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

            player = new ExoPlayer.Builder(getApplicationContext()).build();

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

            String videoUrl = "https://storage.googleapis.com/exoplayer-test-media-0/BigBuckBunny_320x180.mp4";
            MediaItem mediaItem = MediaItem.fromUri(Uri.parse(videoUrl));

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