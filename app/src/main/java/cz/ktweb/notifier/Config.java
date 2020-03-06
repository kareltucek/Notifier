package cz.ktweb.notifier;

import java.util.Date;

public class Config {
    public static int activeSince = 8*60; //minutes
    public static int activeUntil = 22*60;

    public static int interval = 15; //minutes //1,5,15,30,1h,2h,3h,4h,5h,6h

    public static Date snoozeUntil = new Date();
}
