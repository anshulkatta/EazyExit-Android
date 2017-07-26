package test.com.eazyexit;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;


import org.eclipse.moquette.server.Server;

import java.util.Properties;

/**
 * Created by Anshul on 11-07-2017.
 * Start MQTT Server
 */


public class StartServer implements Runnable{

	public static boolean getServerStatus() {
		return serverStatus;
	}

	private static boolean serverStatus = false;

    @Override
    public void run() {
        serverStatus = serverStart();
    }

    private  boolean serverStart() {
	try {
		ServerInstance.getServerInstance().startServer();
	}
	catch(Exception e) {
		e.printStackTrace();
		return false;
	}
	return true;
}

}