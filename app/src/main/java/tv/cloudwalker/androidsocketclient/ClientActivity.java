package tv.cloudwalker.androidsocketclient;

import android.app.Activity;
import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class ClientActivity extends Activity implements NsdManager.DiscoveryListener , NsdManager.ResolveListener {

    private EditText serverIp;

    private Button connectPhones;

    private String serverIpAddress = "";

    private boolean connected = false;

    private Handler handler = new Handler();

    private NsdManager mNsdManager;

    public static final String TAG = ClientActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        mNsdManager = (NsdManager) getSystemService(Context.NSD_SERVICE);
        serverIp = (EditText) findViewById(R.id.server_ip);
        connectPhones = (Button) findViewById(R.id.connect_phones);
        connectPhones.setOnClickListener(connectListener);
    }

    private View.OnClickListener connectListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (!connected) {
                serverIpAddress = serverIp.getText().toString();
                if (!serverIpAddress.equals("")) {
                    Thread cThread = new Thread(new ClientThread());
                    cThread.start();
                }
            }
        }
    };

    @Override
    public void onDiscoveryStarted(String regType) {
        Log.d(TAG, "Service discovery started");
    }

    @Override
    public void onServiceFound(NsdServiceInfo service) {
        Log.d(TAG, "onServiceFound: "+service);
    }

    @Override
    public void onServiceLost(NsdServiceInfo service) {
        Log.e(TAG, "service lost" + service);
    }

    @Override
    public void onDiscoveryStopped(String serviceType) {
        Log.i(TAG, "Discovery stopped: " + serviceType);
    }

    @Override
    public void onStartDiscoveryFailed(String serviceType, int errorCode) {
        Log.e(TAG, "Discovery failed: Error code:" + errorCode);
    }

    @Override
    public void onStopDiscoveryFailed(String serviceType, int errorCode) {
        Log.e(TAG, "Discovery failed: Error code:" + errorCode);
    }

    @Override
    public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {
        // Called when the resolve fails. Use the error code to debug.
        Log.e(TAG, "Resolve failed " + errorCode);
        Log.e(TAG, "serivce = " + serviceInfo);
    }

    @Override
    public void onServiceResolved(NsdServiceInfo serviceInfo) {
        Log.d(TAG, "Resolve Succeeded. " + serviceInfo);
        // Obtain port and IP
        Log.d(TAG, "onServiceResolved: " + serviceInfo.getPort() + " " + serviceInfo.getHost());
    }

    public class ClientThread implements Runnable {

        public void run() {
            try {
                InetAddress serverAddr = InetAddress.getByName(serverIpAddress);
                Log.d("ClientActivity", "C: Connecting...");
                Socket socket = new Socket(serverAddr, 15000);
                connected = true;
                while (connected) {
                    try {
                        Log.d("ClientActivity", "C: Sending command.");
                        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                        // where you issue the commands
                        out.println("Hey Server!");
                        Log.d("ClientActivity", "C: Sent.");
                    } catch (Exception e) {
                        Log.e("ClientActivity", "S: Error", e);
                    }
                }
                socket.close();
                Log.d("ClientActivity", "C: Closed.");
            } catch (Exception e) {
                Log.e("ClientActivity", "C: Error", e);
                connected = false;
            }
        }
    }
}
