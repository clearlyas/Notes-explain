package net.micode.notes.gtask.data;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import java.util.Calendar;
import java.util.Date;

public class Reminder {

    private Context context;
    private String title;
    private Date reminderDate;

    public Reminder(Context context, String title, Date reminderDate) {
        this.context = context;
        this.title = title;
        this.reminderDate = reminderDate;
    }

    public void setReminder() {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ReminderReceiver.class);
        intent.putExtra("title", title);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        long triggerTime = reminderDate.getTime();
        if (System.currentTimeMillis() > triggerTime) {
            // 如果设置的时间已经过去，则设置为下一秒触发
            triggerTime = System.currentTimeMillis() + 1000;
        }

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
    }

    public static class ReminderReceiver extends androidx.appcompat.app.AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);

            String title = getIntent().getStringExtra("title");
            showNotification(title);

            // 结束Activity，因为我们不需要显示任何UI
            finish();
        }

        private void showNotification(String title) {
            // 这里创建并显示一个通知
            // 你可以使用Android的通知API来创建通知

            Intent notificationIntent = new Intent(this, MainActivity.class);

            notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "your_channel_id")
                    .setSmallIcon(R.drawable.ic_notification)
                    .setContentTitle("Reminder")
                    .setContentText(title)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            if (notificationManager != null) {
                notificationManager.notify(1, builder.build());
            }


        }
    }
}