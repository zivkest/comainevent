package com.ziv.cme;

public interface MusicServiceCallback
{
    void  initSeekBar(int dutration, int position);
    void onPlayBackEnded();
    void onFocusLost();
    void onNextSongSelected(int position, String songName);
}