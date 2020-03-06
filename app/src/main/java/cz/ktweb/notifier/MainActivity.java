package cz.ktweb.notifier;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupUI();

        setupAlarm();

        showNotification();
    }

    final public static int[] intervals = {1,5,15,30,60,60*2,60*3,60*4,60*6,60*8};

    public void updateIntervalText() {
        String txt = "";
        if(Config.interval < 60) {
            txt = "Activate every " + Config.interval + " minutes";
        } else {
            txt = "Activate every " + Config.interval/60 + " hours";
        }
        ((TextView)findViewById(R.id.LabelInterval)).setText(txt);
    }

    public void updateSinceUntilTexts() {
        ((TextView)findViewById(R.id.LabelSince)).setText("Active since " + Config.activeSince/60 + "h");
        ((TextView)findViewById(R.id.LabelUntil)).setText("Active until " + Config.activeUntil/60 + "h");
    }

    public void setupUI() {

        final SeekBar mySeekBar = ((SeekBar) findViewById(R.id.SeekBarInterval));
        mySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
                int[] intervals = {1,5,15,30,60,60*2,60*3,60*4,60*6,60*8};
                int v = 15;
                if(arg1 >= 0 && arg1 < intervals.length) {
                    v = intervals[arg1];
                }
                Config.interval = v;
                updateIntervalText();
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        for(int i = 0; i < intervals.length; i++) {
            if(Config.interval == intervals[i]) {
                mySeekBar.setProgress(i);
            }
        }
        updateIntervalText();



        final SeekBar mySeekBar2 = ((SeekBar) findViewById(R.id.SeekBarSince));
        mySeekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
                Config.activeSince = arg1*60;
                updateSinceUntilTexts();
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mySeekBar2.setProgress(Config.activeSince/60);
        updateSinceUntilTexts();

        final SeekBar mySeekBar3 = ((SeekBar) findViewById(R.id.SeekBarUntil));
        mySeekBar3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
                Config.activeUntil = arg1*60;
                updateSinceUntilTexts();
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mySeekBar3.setProgress(Config.activeUntil/60);
        updateSinceUntilTexts();

        updateDelayText();
    }

    public void setupAlarm() {
        AlarmManager alarmMgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        //alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 10*1000, pendingIntent);


        Calendar time = Calendar.getInstance();
        time.setTimeInMillis(System.currentTimeMillis());
        time.add(Calendar.SECOND, 5);
        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, time.getTimeInMillis()/60000*60000, 60*1000, pendingIntent);

    }

    public void showNotification() {
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_icon)
                        .setOngoing(true)
                        .setContentTitle("Notifier running!")
                        .setContentText("Notifier running!")
                        .setContentIntent(contentIntent);

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            String channelId = "Your_channel_id";
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_LOW);
            mNotificationManager.createNotificationChannel(channel);
            mBuilder.setChannelId(channelId);
        }

        mNotificationManager.notify(0, mBuilder.build());
    }

    public void updateDelayText() {
        String txt = "Snooze ";

        boolean soon = Config.snoozeUntil.getTime() < new Date().getTime()+2*24*60*60*1000;

        if(Config.snoozeUntil.getTime() <= new Date().getTime()) {
            txt += "until -";
        } else if(Config.snoozeUntil.getMinutes() == 0 && Config.snoozeUntil.getHours() == 0) {
            if (Config.snoozeUntil.getDay() == new Date().getDay() + 1 && soon) {
                txt += "until tommorow";
            } else if (Config.snoozeUntil.getTime() < new Date().getTime() + 7 * 24 * 60 * 60 * 1000) {
                txt += "until " + new SimpleDateFormat("EEE").format(Config.snoozeUntil);
            } else {
                txt += "until " + new SimpleDateFormat("dd MMM").format(Config.snoozeUntil);
            }
        } else if(Config.snoozeUntil.getDay() == new Date().getDay() && soon) {
            txt += "until " + new SimpleDateFormat("HH:mm").format(Config.snoozeUntil);
        } else {
            txt += "until " + new SimpleDateFormat("HH:mm").format(Config.snoozeUntil) + " on " +
                    new SimpleDateFormat("d MMM").format(Config.snoozeUntil);
        }

        ((TextView)findViewById(R.id.LabelDelay)).setText(txt);
    }

    public void updateDelayMin(int min) {
        Config.snoozeUntil.setTime(Config.snoozeUntil.getTime() + min*60*1000);

        if(Config.snoozeUntil.getTime() < new Date().getTime()) {
            Config.snoozeUntil = new Date();
        }

        updateDelayText();
    }

    public void updateDelayDay(int day) {
        Config.snoozeUntil.setTime(Config.snoozeUntil.getTime()+day*60*60*24*1000);

        Config.snoozeUntil.setHours(0);
        Config.snoozeUntil.setMinutes(0);
        Config.snoozeUntil.setSeconds(0);

        if(Config.snoozeUntil.getTime() < new Date().getTime()) {
            Config.snoozeUntil = new Date();
        }

        updateDelayText();
    }

    public void updateDelayNow() {
        Config.snoozeUntil = new Date();
        updateDelayText();
    }

    public void onClickm1d(View v) { updateDelayDay(-1); }
    public void onClickm4h(View v) { updateDelayMin(-240); }
    public void onClickm1h(View v) { updateDelayMin(-60); }
    public void onClickNow(View v) { updateDelayNow(); }
    public void onClickp1h(View v) { updateDelayMin(60); }
    public void onClickp4h(View v) { updateDelayMin(240); }
    public void onClickp1d(View v) { updateDelayDay(1);  }

    public void onClickTestAlarm(View v) { AlarmReceiver.Vibrate(getApplicationContext(), 8);  }

    public void onClickTestNotification(View v) { showNotification(); }

    public void onClickDone(View v) { moveTaskToBack(true); }

}
