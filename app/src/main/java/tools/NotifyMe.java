package tools;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotifyMe extends BroadcastReceiver {

    private static final String CHANNEL_ID = "C196_User";
    private static final String NOTIFICATION = "Notification";

    public NotifyMe() {
    }

    public void onReceive(Context context, Intent intent) {
        NotificationManager nm = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = intent.getParcelableExtra(NOTIFICATION);
        int id = intent.getIntExtra(CHANNEL_ID, 0);
        nm.notify(id, notification);
    }
}
