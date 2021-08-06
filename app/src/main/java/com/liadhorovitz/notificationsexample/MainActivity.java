package com.liadhorovitz.notificationsexample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "1";
    private EditText notificationET;
    private TextView notificationTV;
    private List<String> notifications;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notifications = new ArrayList<>();
        notificationTV = findViewById(R.id.notifications_content);
        notificationET = findViewById(R.id.notification_edit_text);

        findViewById(R.id.send_notification_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendPushNotification();
            }
        });
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Test notification";
            String description = "channel one";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void sendPushNotification() {
        createNotificationChannel();
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        String notificationInput = notificationET.getText().toString();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_notification_overlay)
                .setContentTitle("You got a new message!")
                .setContentText(notificationInput)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setSound(alarmSound)
                .setVibrate(new long[]{0, 100, 200, 300})
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        notifications.add(notificationInput);
        notificationTV.setText(Arrays.toString(notifications.toArray()));
        notificationET.setText(null);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(1, builder.build());
    }
}