package com.ziv.cme;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;


public class MusicService extends Service implements AudioManager.OnAudioFocusChangeListener
{
    private static final String LOG_TAG = MusicService.class.getSimpleName();
    IBinder mBinder = new LocalBinder();
    private MusicServiceCallback mMusicServiceCallback;
    private MediaPlayer mMediaPlayer;
    private boolean mShouldPlayAll = false;

    MediaPlayer.OnErrorListener mOnErrorListener = new MediaPlayer.OnErrorListener()
    {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra)
        {
            stopMusic();
            playMusic(mContext);
            return false;
        }
    };

    private int mId;
    private AudioManager mAudioManager;
    private Context mContext;

    @Override
    public void onAudioFocusChange(int focusChange)
    {
        if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT)
        {

            //            stopMusic();
            pause();
            mMusicServiceCallback.onFocusLost();
        }
        else if (focusChange == AudioManager.AUDIOFOCUS_LOSS)
        {
            //            stopMusic();
            pause();
            mMusicServiceCallback.onFocusLost();
        }
    }


    public class LocalBinder extends Binder
    {
        public MusicService getServerInstance()
        {
            return MusicService.this;
        }
    }

    @Nullable
    @Override

    public IBinder onBind(Intent intent)
    {
        return mBinder;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        //mMediaPlayer = new MediaPlayer();
        mAudioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
    }

    public boolean isShouldPlayAll()
    {
        return mShouldPlayAll;
    }

    public void setShouldPlayAll()
    {
        mShouldPlayAll = !mShouldPlayAll;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        return Service.START_STICKY;
    }

    private int requestAudioFocus()
    {
        return mAudioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN);
    }


    public void setCallback(MusicServiceCallback musicServiceCallback)
    {
        mMusicServiceCallback = musicServiceCallback;
    }

    public void playMusic(Context context)
    {
        if (requestAudioFocus() == AudioManager.AUDIOFOCUS_REQUEST_GRANTED)
        {
            if(isPlaying())
            {
                pause();
                return;
            }
//            else if(mMediaPlayer!=null)
//            {
//                mMusicServiceCallback.initSeekBar(mMediaPlayer.getDuration(), mMediaPlayer.getCurrentPosition());
//                resume(mMediaPlayer.getCurrentPosition());
//            }
//            else
//            {
                // Start playback
                mContext = context;

                if(mMediaPlayer==null)
                {
                    mMediaPlayer = MediaPlayer.create(this, Uri.parse("http://traffic.libsyn.com/comainevent/242_master.mp3"));
                }
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mMediaPlayer.setOnErrorListener(mOnErrorListener);

                //mMusicServiceCallback.initSeekBar(mMediaPlayer.getDuration(), mMediaPlayer.getCurrentPosition());

                mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener()
                {
                    @Override
                    public void onPrepared(MediaPlayer mp)
                    {
                        //mMusicServiceCallback.initSeekBar(mp.getDuration(), mp.getCurrentPosition());
                        mp.start();
                    }
                });
            }
      //  }
    }

    public void stopMusic()
    {
        if (mMediaPlayer.isPlaying())
        {
            mMediaPlayer.stop();
        }
    }

    public int getCurrentPosition()
    {
        return mMediaPlayer.getCurrentPosition();
    }

    public boolean isPlaying()
    {
        return mMediaPlayer != null && mMediaPlayer.isPlaying();
    }

    public void pause()
    {
        mMediaPlayer.pause();
    }

    public void resume(int position)
    {
        if (requestAudioFocus() == AudioManager.AUDIOFOCUS_REQUEST_GRANTED)
        {
            seekTo(position);
            mMediaPlayer.start();
        }
    }

    public void resumeFromCurrentPosition()
    {
        if (requestAudioFocus() == AudioManager.AUDIOFOCUS_REQUEST_GRANTED)
        {
            Log.d(LOG_TAG, "getCurrentPosition: "+getCurrentPosition());
            seekTo(getCurrentPosition());
            mMediaPlayer.start();
            Log.d(LOG_TAG, "getCurrentPosition AFTER START: "+getCurrentPosition());
        }
    }

    public void seekTo(int position)
    {
        mMediaPlayer.seekTo(position);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        mAudioManager.abandonAudioFocus(this);
    }

    public int getDuration()
    {
        return mMediaPlayer.getDuration();
    }



}