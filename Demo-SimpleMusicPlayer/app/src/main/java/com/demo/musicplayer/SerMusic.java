package com.demo.musicplayer;

import android.app.Service;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.IOException;

/**
 * @author 尉迟涛
 * create time : 2020/2/14 15:18
 * description :
 */
public class SerMusic extends Service {

    public static final String TAG = SerMusic.class.getSimpleName();
    public static final String EXTRA_STATUS = "status";
    public static final String EXTRA_MUSIC_INFO = "music_info";
    public static final int STATUS_STOPPING = 0x11;
    public static final int STATUS_PLAYING = 0x12;
    public static final int STATUS_PAUSING = 0x13;

    private static final MusicInfo[] MUSIC_ARRAY;

    static {
        MusicInfo m1 = new MusicInfo("01.mp3", "吴青峰", "起风了");
        MusicInfo m2 = new MusicInfo("02.mp3", "张韶涵", "欧若拉");
        MUSIC_ARRAY = new MusicInfo[]{m1, m2};
    }

    private AssetManager am;
    private MediaPlayer mediaPlayer;
    private int status = STATUS_STOPPING;
    private int current = 0;
    private MyBinder myBinder;

    private class MyBinder extends Binder implements IServiceInterface {
        @Override
        public void play() {
            switch (status) {
                case STATUS_STOPPING:
                    Log.e(TAG, "play");
                    prepareAndPlay(MUSIC_ARRAY[current].getFileName());
                    status = STATUS_PLAYING;
                    break;
                case STATUS_PLAYING:
                    Log.e(TAG, "pause");
                    mediaPlayer.pause();
                    status = STATUS_PAUSING;
                    break;
                case STATUS_PAUSING:
                    Log.e(TAG, "play");
                    mediaPlayer.start();
                    status = STATUS_PLAYING;
                    break;
                default:
            }
            sendMessageToActivity(MUSIC_ARRAY[current], status);
        }

        @Override
        public void next() {
            if (status == STATUS_PLAYING || status == STATUS_PAUSING) {
                Log.e(TAG, "next");
                mediaPlayer.stop();
                playNext();
            }

        }

        @Override
        public void stop() {
            if (status == STATUS_PLAYING || status == STATUS_PAUSING) {
                Log.e(TAG, "stop");
                mediaPlayer.stop();
                status = STATUS_STOPPING;
            }
            sendMessageToActivity(MUSIC_ARRAY[current], status);
        }
    }

    @Override
    public void onCreate() {
        am = getAssets();
        myBinder = new MyBinder();
        mediaPlayer = new MediaPlayer();

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.e(TAG, "onCompletion");
                playNext();

            }
        });
        super.onCreate();
    }

    /**
     * 获取下一首歌的下标
     */
    private int getNextIndex() {
        if (++current >= MUSIC_ARRAY.length) {
            current = 0;
        }
        return current;
    }

    /**
     * 播放下一首歌
     */
    private void playNext() {
        MusicInfo musicInfo = MUSIC_ARRAY[getNextIndex()];
        prepareAndPlay(musicInfo.getFileName());
        sendMessageToActivity(musicInfo, -1);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e(TAG, "onBind");
        return myBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.e(TAG, "onUnbind");
        myBinder = null;
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        mediaPlayer.stop();
        mediaPlayer.release();
        super.onDestroy();
    }

    private void sendMessageToActivity(MusicInfo musicInfo, int status) {
        Intent sendIntent = new Intent(ActMain.TAG);
        sendIntent.putExtra(EXTRA_STATUS, status);
        sendIntent.putExtra(EXTRA_MUSIC_INFO, musicInfo);
        sendBroadcast(sendIntent);
    }

    private void prepareAndPlay(String musicFileName) {
        try {
            AssetFileDescriptor afd = am.openFd(musicFileName);
            mediaPlayer.reset();
            mediaPlayer.setDataSource(
                    afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength()
            );
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
