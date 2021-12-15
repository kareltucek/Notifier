package cz.ktweb.notifier;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;

import java.util.Date;

public class JobReceiver extends JobService {
    public static Date nextTick = new Date();

    private static final int JOB_ID = 1;

    public static void setupService(Context context) {
        nextTick = new Date((System.currentTimeMillis() + Config.interval*60000)/(Config.interval*60000)*(Config.interval*60000));
        long delay = nextTick.getTime() - new Date().getTime();
        long deadlineOffset = Config.interval < 5 ? 10*1000 : 50*1000;

        ComponentName component = new ComponentName(context, JobReceiver.class);
        JobInfo.Builder builder = new JobInfo.Builder(JOB_ID, component)
                // schedule it to run any time between 1 - 5 minutes
                .setMinimumLatency(delay)
                .setOverrideDeadline(delay + deadlineOffset);

        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(builder.build());
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        Date currentTick = nextTick;
        setupService(getApplicationContext());
        NoiseMaker.HandleTick(getApplicationContext(), currentTick, 0);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        // whether or not you would like JobReceiver to automatically retry your failed job.
        return false;
    }

    public static void cancelService(Context context) {
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.cancelAll();
    }

}
