package test.com.eazyexit;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;

import java.util.concurrent.ExecutionException;

public class EazyExitService extends Service {

    private static boolean isServerrunning = false;

  @Override
  public void onCreate() {
    super.onCreate();
      android.os.Debug.waitForDebugger();
      Thread t= new Thread(new StartServer());
      t.start();
    isServerrunning = StartServer.getServerStatus();

  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
      if(isServerrunning) {
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
        Thread t = new Thread(new StopServer());
        t.start();
        isServerrunning = false;

    }
}