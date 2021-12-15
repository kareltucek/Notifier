package cz.ktweb.notifier;

import android.content.Context;

public class TimerManager {
    public static void SetupTimer(Context ctx) {
        switch(Config.timerImp) {
            case AlarmReceiver:
                AlarmReceiver.setupNewAlarm(ctx);
                break;
            case JobReceiver:
                JobReceiver.setupService(ctx);
                break;
        }

    }

    public static void CancelTimer(Context ctx) {
        switch(Config.timerImp) {
            case AlarmReceiver:
                AlarmReceiver.cancelCurrentAlarm(ctx);
                break;
            case JobReceiver:
                JobReceiver.cancelService(ctx);
                break;
        }
    }
}
