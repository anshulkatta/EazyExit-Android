package test.com.eazyexit;

import android.os.AsyncTask;

/**
 * Created by Anshul on 11-07-2017.
 *
 * Stop MQTT Server
 */

public class StopServer  {

    public static  boolean stopServer() {
        try {
            ServerInstance.getServerInstance().stopServer();
        }
        catch( Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
