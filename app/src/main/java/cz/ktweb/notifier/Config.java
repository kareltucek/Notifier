package cz.ktweb.notifier;

import android.app.Activity;
import android.content.SharedPreferences;

import java.util.Date;

public class Config {
    public static final int CurrentConfigVersion = 1;

    public static int activeSince = 8*60; //minutes
    public static int activeUntil = 22*60;

    public static int interval = 15; //minutes //1,5,15,30,1h,2h,3h,4h,5h,6h

    public static Date snoozeUntil = new Date();

    public static boolean showPermanent = true;
    public static boolean fireTicks = false;
    public static boolean vibrateTicks = false;
    public static boolean playTicks = true;

    public static TimerImp timerImp = TimerImp.JobReceiver;

    public static int version = CurrentConfigVersion;


    public static void Save(Activity a) {
        SharedPreferences settings = a.getPreferences(a.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        editor.putInt("version", version);
        editor.putInt("activeSince", activeSince);
        editor.putInt("activeUntil", activeUntil);
        editor.putInt("interval", interval);
        //editor.putInt("timerImp", timerImp.ordinal());
        editor.putLong("snoozeUntil", snoozeUntil.getTime());
        editor.putBoolean("showPermanent", showPermanent);
        editor.putBoolean("fireTicks", fireTicks);
        editor.putBoolean("vibrateTicks", vibrateTicks);
        editor.putBoolean("playTicks", playTicks);

        editor.commit();
    }

    public static void Load(Activity a) {
        SharedPreferences settings = a.getPreferences(a.MODE_PRIVATE);

        version = settings.getInt("version", version);
        activeSince = settings.getInt("activeSince", activeSince);
        activeUntil = settings.getInt("activeUntil", activeUntil);
        interval = settings.getInt("interval", interval);
        //timerImp = TimerImp.values()[settings.getInt("timerImp", timerImp.ordinal())];
        snoozeUntil = new Date(settings.getLong("snoozeUntil", snoozeUntil.getTime()));
        showPermanent = settings.getBoolean("showPermanent", showPermanent);
        fireTicks = settings.getBoolean("fireTicks", fireTicks);
        vibrateTicks = settings.getBoolean("vibrateTicks", vibrateTicks);
        playTicks = settings.getBoolean("playTicks", playTicks);
    }
}
