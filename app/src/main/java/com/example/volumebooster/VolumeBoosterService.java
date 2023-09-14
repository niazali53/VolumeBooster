package com.example.volumebooster;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.audiofx.Equalizer;
import android.media.audiofx.LoudnessEnhancer;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class VolumeBoosterService extends Service {

    private static Context context;
    private static AudioManager audio = null;
    private static LoudnessEnhancer loudness = null;
    private static boolean init = false;
    private static boolean useEQ = false;
    private static Equalizer equalizer = null;

    public static void init(Context c) {
        context = c;

        if (!init) {
            audio = (AudioManager) context.getSystemService(context.AUDIO_SERVICE);
//            loudness = getLoudnessEnhancer();

            if (useEQ) {
                equalizer = getEqualizer();
            } else {
                loudness = getLoudnessEnhancer();
            }
        }
    }

    private static Equalizer getEqualizer() {
        try {
            return new Equalizer(1, 0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static LoudnessEnhancer getLoudnessEnhancer() {
        try {
            return new LoudnessEnhancer(0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    public static void setVolume(int vol, int boost, Context c) {
        init(c);
        checkBoost(boost);
    }

    private static void checkBoost(int boost) {

        if (boost > 0) {
            if (useEQ) {
                boostEqualizer(boost);
            } else {
                boostLoudness(boost);
            }
        } else if (useEQ) {
            if (equalizer != null) {
                equalizer.setEnabled(false);
                equalizer.release();
                equalizer = null;
            }
        } else if (loudness != null) {
            loudness.setEnabled(false);
            loudness.release();
            loudness = null;
        }

    }

    private static void boostEqualizer(int boost) {

        if (equalizer == null) {
            equalizer = getEqualizer();

        }
        if (equalizer != null) {
            try {
                float level = (((float) boost) / 100.0f) * ((float) equalizer.getBandLevelRange()[1]);
                short nBands = equalizer.getNumberOfBands();
                for (short i = (short) 0; i < nBands; i = (short) (i + 1)) {
                    equalizer.setBandLevel(i, (short) ((int) level));
                }
                equalizer.setEnabled(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    private static void boostLoudness(int boost) {

        float level = (((float) boost) / 100.0f) * 12000.0f;
        if (loudness == null) {
            loudness = getLoudnessEnhancer();
        }
        if (loudness != null) {
            try {
                loudness.setTargetGain((int) level);
                loudness.setEnabled(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
