package cz.ktweb.notifier;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;

import java.util.Date;

public class NoiseMaker {

    static public void HandleTick(Context ctx, Date currentTick, int currentCode) {
        if (NoiseMaker.ShouldFire(ctx)) {
            if (Config.fireTicks) {
                MainActivity.showTickNotification(ctx, currentTick, currentCode);
            }
            if (Config.vibrateTicks) {
                NoiseMaker.Vibrate(ctx, NoiseMaker.getIndicator());
            }
            if (Config.playTicks) {
                NoiseMaker.PlaySound(ctx, NoiseMaker.getIndicator());
            }
        }
    }

    static public boolean ShouldFire(Context ctx) {
        Date now = new Date();
        Date today = new Date();
        today.setHours(0);
        today.setMinutes(0);
        today.setSeconds(0);
        long nowMinutes = (now.getTime()/1000 - today.getTime()/1000)/60;

        if(now.getTime() < Config.snoozeUntil.getTime()) {
            return false;
        }

        if(nowMinutes < Config.activeSince || nowMinutes > Config.activeUntil) {
            return false;
        }
/*
        if(nowMinutes % Config.interval != 0) {
            return false;
        }
*/
        return true;
    }

    public static int getIndicator() {
        Date now = new Date();
        Date today = new Date();
        today.setHours(0);
        today.setMinutes(0);
        today.setSeconds(0);
        long nowMinutes = (now.getTime()/1000 - today.getTime()/1000)/60;
        int indicateTime = (int)nowMinutes;

        if(true) {
            indicateTime %= 12*60;
        }
        if(Config.interval < 60) {
            indicateTime %= 60;
        }
        if(Config.interval < 5) {
            indicateTime %= 10;
        }

        return indicateTime / Config.interval;
    }

    public static int tickLength = 100;
    public static int vibratePeriod = 400;

    public static int p(int v, int mask) {
        return (v & mask) > 0 ? tickLength*2 : tickLength;
    }

    public static int n(int v, int mask) {
        return (v & mask) > 0 ? vibratePeriod - tickLength*2 : vibratePeriod - tickLength;
    }

    static public void Vibrate(Context ctx, int i) {
        Vibrator v = (Vibrator) ctx.getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            long[] pattern = {0,
                    p(i, 8), n(i, 8),
                    p(i, 4), n(i, 4),
                    p(i, 2), n(i, 2),
                    p(i, 1), n(i, 1),
            };
            v.vibrate(VibrationEffect.createWaveform(pattern,-1));
        } else {
            //deprecated in API 26
            v.vibrate(300);
        }
    }



    static public void PlaySound(Context ctx, int i) {
        if(i == 0) {
            MediaPlayer.create(ctx, R.raw.gpizz_long).start();

        } else {
            MediaPlayer.create(ctx, R.raw.apizz_muted).start();
        }
    }
}
