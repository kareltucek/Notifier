package cz.ktweb.notifier;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;

import java.util.Date;


public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        HandleAlarm(context);
    }

    static public void HandleAlarm(Context ctx) {
        Date now = new Date();
        Date today = new Date();
        today.setHours(0);
        today.setMinutes(0);
        today.setSeconds(0);
        long nowMinutes = (now.getTime()/1000 - today.getTime()/1000)/60;

        if(now.getTime() < Config.snoozeUntil.getTime()) {
            return;
        }

        if(nowMinutes < Config.activeSince || nowMinutes > Config.activeUntil) {
            return;
        }

        if(nowMinutes % Config.interval != 0) {
            return;
        }

        int indicate = 0;
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

        indicate = indicateTime / Config.interval;

        Vibrate(ctx, indicate);
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
}