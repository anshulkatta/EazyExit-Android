package test.com.eazyexit;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.MQTTConnector;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MainActivity extends Activity {

    private boolean writepermission = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setContentView(R.layout.activity_main);
    }
/*
        int permissionCheck2 = ActivityCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(permissionCheck2 !=  PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    2);
        }
        else {
            // use this to start and trigger a service
            Intent i= new Intent(this, EazyExitService.class);
            // potentially add data to the intent
            i.putExtra("server", "started");
            startService(i);
        }

        Button b1 = (Button)findViewById(R.id.b1);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NodePing n = new NodePing();
                n.ping("ON",getBrokerURL());
            }
        });

        Button b2 = (Button)findViewById(R.id.b2);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AsyncTask m = new AsyncMqttTask();
                m.execute();

            }
        });

    }

    class AsyncMqttTask extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] params) {
            MQTTConnector mqttConnector = new MQTTConnector();
            mqttConnector.connect(getApplicationContext(),getBrokerURL(),"discoverReceive");
           return null;
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 2: {
                    // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    writepermission = true;
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(MainActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }

            }

            if(writepermission) {
                // use this to start and trigger a service
                Intent i= new Intent(this, EazyExitService.class);
                // potentially add data to the intent
                i.putExtra("server", "started");
                startService(i);
            }
        }
    }

    private String getBrokerURL() {
        WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
        String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        return "tcp://"+ip+":1883";
    }
    */
}
