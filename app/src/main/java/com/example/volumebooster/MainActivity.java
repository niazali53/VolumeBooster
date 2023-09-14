package com.example.volumebooster;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.audiofx.BassBoost;
import android.media.audiofx.Equalizer;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {

    SeekBar seek_Bar_Bass1;
    SeekBar seek_Bar_Bass2;
    SeekBar seek_Bar_Bass3;
    SeekBar seek_Bar_Bass4;
    SeekBar seek_Bar_Bass5;
    SeekBar seek_Bar_Volume;
    SeekBar Seek_Bar_VolumeBooster;
    AudioManager audioManager;
    int currentVolume;
    int maxVolume;
    int volumeLevel;
    MediaPlayer mediaPlayer;
    Equalizer equalizer60;
    TextView volBoostTextView;
    TextView volume_Level;
    TextView boost_Level;
    String suffix = "%";
    int audioSessionId;
    BassBoost bassBoost;
    ToggleButton bassBoost_Btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialization();

        seek_Bar_Volume.setMax(maxVolume);
        seek_Bar_Volume.setProgress(currentVolume);

        Seek_Bar_VolumeBooster.setMax(60);
        Seek_Bar_VolumeBooster.setProgress(currentVolume);

        //start
        mediaPlayer.setAudioSessionId(AudioManager.AUDIO_SESSION_ID_GENERATE);
        audioSessionId = mediaPlayer.getAudioSessionId();

        bassBoost = new BassBoost(0,audioSessionId);

        equalizer60 = new Equalizer(0, audioSessionId);
        equalizer60.setEnabled(true);


        short band60Hz = equalizer60.getBand(60 * 1000);
        short band230Hz = equalizer60.getBand(230 * 1000);
        short band910Hz = equalizer60.getBand(910 * 1000);

        short treble = equalizer60.getBand(3600000);
        short treble1 = equalizer60.getBand(14000000);

        //end

        seek_Bar_Volume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                volumeLevel = progress;
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
                int volumePercentage = (int) ((progress / (float) maxVolume) * 100);
                volume_Level.setText(volumePercentage + suffix);


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        try {
            Seek_Bar_VolumeBooster.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {

                    try {

                        VolumeBoosterService.setVolume(volumeLevel, progress, getApplicationContext());
                        int volumePercentage = (int) ((progress / (float) 30) * 100);
                        boost_Level.setText(volumePercentage + suffix);

                    } catch (UnsupportedOperationException e) {
                        Log.e("Equilizer Error", "Unsupported Operation" + e.getMessage());
                    }

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
        } catch (UnsupportedOperationException e) {
            Log.e("Equalizer Error", "Failed to initialize Equalizer: " + e.getMessage());
        }


        seek_Bar_Bass1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {

//                equalizer60.setBandLevel(band60Hz, (short) ((progress - 50) * 100));
                int minBass = equalizer60.getBandLevelRange()[0];
                int maxBass = equalizer60.getBandLevelRange()[1];
                int bass = minBass + (progress * (maxBass - minBass)) / seekBar.getMax();

                // Set the bass level for the first Equalizer
                equalizer60.setBandLevel(band60Hz, (short) bass);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seek_Bar_Bass3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {

//                equalizer230.setBandLevel(band230Hz, (short) ((progress - 50) * 100));

                int minBass = equalizer60.getBandLevelRange()[0];
                int maxBass = equalizer60.getBandLevelRange()[1];
                int bass = minBass + (progress * (maxBass - minBass)) / seekBar.getMax();

                // Set the bass level for the first Equalizer
                equalizer60.setBandLevel(band230Hz, (short) bass);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seek_Bar_Bass4.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
//                equalizer60.setEnabled(true);
//                equalizer60.setBandLevel(band910Hz, (short) ((progress - 50) * 100));

                int minBass = equalizer60.getBandLevelRange()[0];
                int maxBass = equalizer60.getBandLevelRange()[1];
                int bass = minBass + (progress * (maxBass - minBass)) / seekBar.getMax();

                // Set the bass level for the first Equalizer
                equalizer60.setBandLevel(band910Hz, (short) bass);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seek_Bar_Bass5.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {

                try {

                    int minBass = equalizer60.getBandLevelRange()[0];
                    int maxBass = equalizer60.getBandLevelRange()[1];
                    int bass = minBass + (progress * (maxBass - minBass)) / seekBar.getMax();

                    // Set the bass level for the first Equalizer
                    equalizer60.setBandLevel(treble, (short) bass);


                } catch (IllegalArgumentException e) {
                    Log.e("Equalizer Error", "Illegal Argument Exception" + e.getMessage());
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seek_Bar_Bass2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {

                int minBass = equalizer60.getBandLevelRange()[0];
                int maxBass = equalizer60.getBandLevelRange()[1];
                int bass = minBass + (progress * (maxBass - minBass)) / seekBar.getMax();

                // Set the bass level for the first Equalizer
                equalizer60.setBandLevel(treble1, (short) bass);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        bassBoost_Btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    bassBoost.setStrength((short) 1000);
                    bassBoost.setEnabled(true);

                }else {
                    bassBoost.setEnabled(false);
                }
            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (equalizer60 != null) {
            equalizer60.release();
            bassBoost.release();
            equalizer60 = null;
        }

        if (mediaPlayer != null) {
            mediaPlayer.release();
        }

    }

    private void initialization() {
        seek_Bar_Bass1 = findViewById(R.id.seek_Bar_Bass1);
        seek_Bar_Bass2 = findViewById(R.id.seek_Bar_Bass2);
        seek_Bar_Bass3 = findViewById(R.id.seek_Bar_Bass3);
        seek_Bar_Bass4 = findViewById(R.id.seek_Bar_Bass4);
        seek_Bar_Bass5 = findViewById(R.id.seek_Bar_Bass5);
        seek_Bar_Volume = findViewById(R.id.seek_Bar_Volume);
        Seek_Bar_VolumeBooster = findViewById(R.id.seek_Bar_BoostVolume);
        volBoostTextView = findViewById(R.id.volBoostTextView);
        volume_Level = findViewById(R.id.volume_Level);
        bassBoost_Btn = findViewById(R.id.bassBoost_Btn);

        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        volume_Level.setText(currentVolume+suffix);
        boost_Level = findViewById(R.id.boost_Level);
        boost_Level.setText(currentVolume + suffix);

        mediaPlayer = new MediaPlayer();

    }
}