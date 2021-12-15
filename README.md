Notifier
========

This is a simple repetitive notifier/reminder intended for (e.g.) habit improvement or regular exercises, written for Android phones.

Usage/Features:
- Basic function - every x minutes, it discretely vibrates. That's all. No interaction needed.
- Reminders are active only during a part of your day - by default 8AM to 10PM.
- If you do not want to get reminders at some particular time, you can snooze it - e.g., for a few hours, or until tomorrow, or until next friday after. After that, the reminders will resume automatically.

Vibration pattern:
- The reminder takes form of four ticks. Every one of the ticks can be either short or long. The short/long pattern forms binary number describing current phase of the subdivision. The patterns are namely:
  - (x*1hour)%12hours
  - (x*15minutes)%1hour
  - (x*5minutes)%1hour
  - (x*1minute)%10minutes
- E.g., if interval is one hour or longer, then pattern 0110 (6) means it is 6 o'clock. If interval is 15 minutes, then 0011 (3) indicates that the time is something:45. 
- Used pattern depends only on chosen interval. Do not expect them to alternate on full hours.

Design decisions:
- We aim not to get in your way.  The interface is very minimalistic and needs the least possible amount of user interaction. 
- We are persistent - by design, the notifier always runs. If its service is undesired, the user is expected to "snooze" it until next desired activation rather than to kill it. This compensates the usual lack of will to *actively* use pomodoro-style timers.
- Our UI's tree is shallow - no nested dialogs, no nested clock faces. Just a few simple slider/button widgets on a simple form.
- We have decided to add persistent notification which indicates that the app is running and allows quick access. This should also prevent the system from putting the app to sleep or the confusion caused by it. If you do not want the notification, simply revoke notification permissions.



