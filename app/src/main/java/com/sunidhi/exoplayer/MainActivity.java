package com.sunidhi.exoplayer;

import android.content.Context;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.sunidhi.exoplayer.databinding.ActivityMainBinding;

import static android.media.AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK;
import static android.media.AudioManager.AUDIOFOCUS_LOSS;
import static android.media.AudioManager.AUDIOFOCUS_LOSS_TRANSIENT;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    SimpleExoPlayer simpleExoPlayer;
    AudioManager mAudioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        simpleExoPlayer = new SimpleExoPlayer.Builder(this).build();
        binding.videoView.setPlayer(simpleExoPlayer);
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        int result = mAudioManager.requestAudioFocus(mAudioChangeListener,
                AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN);
        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {

            MediaItem mediaItem = MediaItem.fromUri("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4");
            simpleExoPlayer.addMediaItem(mediaItem);
            simpleExoPlayer.prepare();
        }
    }

    AudioManager.OnAudioFocusChangeListener mAudioChangeListener = focusChange -> {
        if(focusChange == AUDIOFOCUS_LOSS_TRANSIENT ||
                focusChange == AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK){
            //pause
            simpleExoPlayer.pause();

        }else if(focusChange==AudioManager.AUDIOFOCUS_GAIN){
            //resume
            simpleExoPlayer.play();

        }else if(focusChange== AUDIOFOCUS_LOSS){
            //stop playback
            simpleExoPlayer.release();
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onStop() {
        super.onStop();
        releaseMediaPlayer();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void releaseMediaPlayer() {
        if(simpleExoPlayer != null)
            simpleExoPlayer.release();

        simpleExoPlayer = null;
        mAudioManager.abandonAudioFocus(mAudioChangeListener);

    }



}