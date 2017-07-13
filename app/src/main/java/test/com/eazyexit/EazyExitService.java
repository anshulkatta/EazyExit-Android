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
    AsyncTask task = new StartServer().execute();
      try {
          isServerrunning = (boolean) task.get();
      } catch (InterruptedException e) {
          e.printStackTrace();
      } catch (ExecutionException e) {
          e.printStackTrace();
      }
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
      if(!isServerrunning) {
          AsyncTask task = new StartServer().execute();
      }

      return Service.START_STICKY;
  }

  @Override
  public IBinder onBind(Intent intent) {
    //TODO for communication return IBinder implementation
    return null;
  }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AsyncTask task = new StopServer().execute();

    }
}