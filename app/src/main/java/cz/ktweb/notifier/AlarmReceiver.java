package cz.ktweb.notifier;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Date;


public class AlarmReceiver extends BroadcastReceiver {
    public static Date nextTick = new Date();
    public static int nextCode = 0;

    @Override
    public void onReceive(Context ctx, Intent intent) {
        Date currentTick = nextTick;
        int currentCode = this.nextCode;
        setupNewAlarm(ctx);
        NoiseMaker.HandleTick(ctx, currentTick, currentCode);
        //whatever was set up, cancel it - prevent accidental existence of multiple parallel timers
        nextCode = currentCode + 1;
        cancelAlarm(ctx, currentCode);
    }

    static public void setupAlarm(Context ctx, int code) {
        nextTick = new Date((System.currentTimeMillis() + Config.interval*60000)/(Config.interval*60000)*(Config.interval*60000));

        AlarmManager alarmMgr = (AlarmManager)ctx.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(ctx, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(ctx, code, intent, 0);

        alarmMgr.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, nextTick.getTime(), pendingIntent);
    }

    static public void setupNewAlarm(Context ctx) {
        int currentCode = nextCode;
        setupAlarm(ctx, currentCode+1);
        nextCode = currentCode+1;
        cancelAlarm(ctx, currentCode);
    }

    static public void cancelAlarm(Context ctx, int code) {
        AlarmManager alarmMgr = (AlarmManager)ctx.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(ctx, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(ctx, code, intent, 0);

        alarmMgr.cancel(pendingIntent);
        pendingIntent.cancel();
    }

    static public void cancelCurrentAlarm(Context ctx) {
        cancelAlarm(ctx, nextCode);
    }

}