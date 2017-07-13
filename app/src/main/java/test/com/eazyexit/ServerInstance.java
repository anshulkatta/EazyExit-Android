package test.com.eazyexit;

import org.eclipse.moquette.server.Server;

/**
 * Created by Anshul on 11-07-2017.
 *
 * SingleTon ServerInstance class to get server object
 * This will make sure the Server instance is singleton
 */

public class ServerInstance {

    private static Server serverInstance = null;

    public static Server getServerInstance() {
        try {
            if(serverInstance == null) {
                serverInstance = new Server();
                return serverInstance;
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return serverInstance;
    }
}
