package tv.cloudwalker.androidsocketclient;

import android.app.Activity;
import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;

public class NSDActivity extends Activity implements View.OnClickListener{

    private String SERVICE_NAME = "CloudwalkerService";
    private String SERVICE_TYPE = "_https._tcp.";
    public static final String TAG = MainActivity.class.getSimpleName();
    private InetAddress hostAddress;
    private int hostPort;
    private NsdManager mNsdManager;
    private Socket socket;
    private TextView text;



    NsdManager.DiscoveryListener mDiscoveryListener = new NsdManager.DiscoveryListener() {

        // Called as soon as service discovery begins.
        @Override
        public void onDiscoveryStarted(String regType) {
            Log.d(TAG, "Service discovery started");
        }

        @Override
        public void onServiceFound(final NsdServiceInfo service) {
            // A service was found! Do something with it.
            Log.d(TAG, "Service discovery success : " + service);
            Log.d(TAG, "Host = "+ service.getServiceName());
            Log.d(TAG, "port = " + String.valueOf(service.getPort()));

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    text.setText("Service Name :  "+ service.getServiceName() + "\n");
                }
            });


            if(service.getServiceName().compareToIgnoreCase(SERVICE_NAME)==0) {
                mNsdManager.resolveService(service, mResolveListener);
            }

//            if (!service.getServiceType().equals(SERVICE_TYPE)) {
//                // Service type is the string containing the protocol and
//                // transport layer for this service.
//                Log.d(TAG, "Unknown Service Type: " + service.getServiceType());
//            } else if (service.getServiceName().equals(SERVICE_NAME)) {
//                // The name of the service tells the user what they'd be
//                // connecting to. It could be "Bob's Chat App".
//                Log.d(TAG, "Same machine: " + SERVICE_NAME);
//            } else {
//                Log.d(TAG, "Diff Machine : " + service.getServiceName());
//                // connect to the service and obtain serviceInfo
//                mNsdManager.resolveService(service, mResolveListener);
//            }
        }

        @Override
        public void onServiceLost(NsdServiceInfo service) {
            // When the network service is no longer available.
            // Internal bookkeeping code goes here.
            Log.e(TAG, "service lost" + service);
        }

        @Override
        public void onDiscoveryStopped(String serviceType) {
            Log.i(TAG, "Discovery stopped: " + serviceType);
        }

        @Override
        public void onStartDiscoveryFailed(String serviceType, int errorCode) {
            Log.e(TAG, "Discovery failed: Error code:" + errorCode);
            mNsdManager.stopServiceDiscovery(this);
        }

        @Override
        public void onStopDiscoveryFailed(String serviceType, int errorCode) {
            Log.e(TAG, "Discovery failed: Error code:" + errorCode);
            mNsdManager.stopServiceDiscovery(this);
        }
    };


    NsdManager.ResolveListener mResolveListener = new NsdManager.ResolveListener() {

        @Override
        public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {
            // Called when the resolve fails. Use the error code to debug.
            Log.e(TAG, "Resolve failed " + errorCode);
            Log.e(TAG, "serivce = " + serviceInfo);
        }

        @Override
        public void onServiceResolved(NsdServiceInfo serviceInfo) {
            Log.d(TAG, "Resolve Succeeded. " + serviceInfo);

//            if (serviceInfo.getServiceName().equals(SERVICE_NAME)) {
//                Log.d(TAG, "Same IP.");
//                return;
//            }

            // Obtain port and IP
            hostPort = serviceInfo.getPort();
            hostAddress = serviceInfo.getHost();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    findViewById(R.id.getTvInfo).setVisibility(View.VISIBLE);
                }
            });

            Log.d(TAG, "onServiceResolved: "+hostPort+" "+hostAddress);
            new Thread(new ClientThread()).start();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nsd);
        text = (TextView) findViewById(R.id.text2);
        mNsdManager = (NsdManager) getSystemService(Context.NSD_SERVICE);
        findViewById(R.id.getTvInfo).setOnClickListener(this);
    }


        class ClientThread implements Runnable {

        @Override
        public void run() {
            try {
                Log.d(TAG, "run: client thread "+hostAddress+" "+hostPort);
                socket = new Socket(hostAddress, hostPort);
            }
            catch (final UnknownHostException e1) {
                e1.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(NSDActivity.this, e1.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            } catch (final IOException e1) {
                e1.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(NSDActivity.this, e1.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
            catch (final Exception e){
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(NSDActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }


    @Override
    protected void onPause() {
        if (mNsdManager != null) {
            mNsdManager.stopServiceDiscovery(mDiscoveryListener);
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mNsdManager != null) {
            mNsdManager.discoverServices(
                    SERVICE_TYPE, NsdManager.PROTOCOL_DNS_SD, mDiscoveryListener);
        }
    }

    @Override
    protected void onDestroy() {
        if (mNsdManager != null) {
            mNsdManager.stopServiceDiscovery(mDiscoveryListener);
        }
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.getTvInfo:
                Log.d(TAG, "onClick: tvInfo");
                try {
                    String str = "tvInfo";
                    PrintWriter out = new PrintWriter(new BufferedWriter(
                            new OutputStreamWriter(socket.getOutputStream())),
                            true);
                    out.println(str);
                } catch (UnknownHostException e) {
                    Log.d(TAG, "onClick: error "+e.getMessage());
                    e.printStackTrace();
                } catch (IOException e) {
                    Log.d(TAG, "onClick: error "+e.getMessage());
                    e.printStackTrace();
                } catch (Exception e) {
                    Log.d(TAG, "onClick: error "+e.getMessage());
                    e.printStackTrace();
                }
                break;

            case R.id.logout:
                break;

            default:
        }



    }

}
