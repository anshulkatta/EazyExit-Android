package test.com.eazyexit;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.text.format.Formatter;

import com.MQTTConnector;

import java.util.concurrent.ExecutionException;

public class EazyExitService extends Service {

    private static boolean isServerrunning = false;

  @Override
  public void onCreate() {
    super.onCreate();
      //android.os.Debug.waitForDebugger();
      Intent notificationIntent = new Intent(this, MainActivity.class);

      PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
              notificationIntent, 0);

      Notification notification = new NotificationCompat.Builder(this)
              .setSmallIcon(R.mipmap.ic_launcher)
              .setContentTitle("My Awesome App")
              .setContentText("Doing some work...")
              .setContentIntent(pendingIntent).build();

      startForeground(1337, notification);

  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {

      isServerrunning = StartServer.getServerStatus();
      if(!isServerrunning) {
          Thread t = new Thread(new StartServer());
          t.start();
      }

      return Service.START_NOT_STICKY;
  }

  @Override
  public IBinder onBind(Intent intent) {
    //TODO for communication return IBinder implementation
    return null;
  }

    @Override
    public void onDestroy() {
        super.onDestroy();
        StopServer.stopServer();
        isServerrunning = false;

    }

}