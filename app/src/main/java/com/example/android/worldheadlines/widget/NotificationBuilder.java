package com.example.android.worldheadlines.widget;

import android.app.Notification;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.example.android.worldheadlines.R;

@RequiresApi(api = Build.VERSION_CODES.O)
public class NotificationBuilder extends Notification {

    private static final String CHANNEL_ID = "channel_id";

    private Context context;

    private Notification.Builder mNotification;

    public void setContext(Context context){
        this.context = context;
    }

    private void setNotification(Notification.Builder notification){
        this.mNotification = notification;
    }

    public Notification.Builder builder  = new Notification.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_stat_name)
            .setContentText("Check for foreground service")
            .setContentTitle("Foreground Service")
            .setPriority(Notification.PRIORITY_DEFAULT);
}
