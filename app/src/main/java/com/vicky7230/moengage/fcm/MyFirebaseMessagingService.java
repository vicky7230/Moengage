package com.vicky7230.moengage.fcm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.vicky7230.moengage.R;
import com.vicky7230.moengage.ui.MainActivity;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    public static final String TAG = "firebaseService";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage.getData().size() > 0) {
            super.onMessageReceived(remoteMessage);

            Map<String, String> data = remoteMessage.getData();
            String body = data.get("deaths");
            String title = data.get("virus");

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);

            PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 101, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationManager nm = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);

            NotificationChannel channel = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                channel = new NotificationChannel("222", "my_channel", NotificationManager.IMPORTANCE_HIGH);
                if (nm != null) {
                    nm.createNotificationChannel(channel);
                }
            }

            Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_warning);
            Bitmap bitmap = null;
            if (drawable != null) {
                bitmap = ((BitmapDrawable) drawable).getBitmap();
            }
            NotificationCompat.Builder builder =
                    new NotificationCompat.Builder(
                            getApplicationContext(), "222")
                            .setContentTitle(title)
                            .setAutoCancel(true)
                            .setLargeIcon(bitmap)
                            .setSmallIcon(R.drawable.ic_warning)
                            .setContentText(body)
                            .setSmallIcon(R.drawable.ic_warning)
                            .setContentIntent(pi);

            builder.setPriority(NotificationCompat.PRIORITY_HIGH);
            if (nm != null) {
                nm.notify(101, builder.build());
            }
        }
    }

    @Override
    public void onNewToken(@NonNull String token) {
        Log.d(TAG, "Refreshed token: " + token);
    }
}
