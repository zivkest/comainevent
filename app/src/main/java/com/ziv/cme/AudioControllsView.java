package com.ziv.cme;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageButton;

public class AudioControllsView extends FrameLayout
{
    private ImageButton mPause;
    private ImageButton mPlay;
    private ImageButton mStop;

    public AudioControllsView(Context context)
    {
        super(context);
        init();
    }

    public AudioControllsView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public AudioControllsView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init()
    {
        inflate(getContext(), R.layout.view_audio_controls, this);
        this.mPlay = (ImageButton) findViewById(R.id.play);
        this.mStop= (ImageButton) findViewById(R.id.stop);
        this.mPause= (ImageButton) findViewById(R.id.pause);
    }
}
