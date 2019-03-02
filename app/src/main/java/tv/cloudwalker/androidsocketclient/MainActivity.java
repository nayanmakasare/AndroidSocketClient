package tv.cloudwalker.androidsocketclient;

import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

public class MainActivity extends AppCompatActivity {

    private String SERVICE_NAME = "CloudwalkerService";
    private String SERVICE_TYPE = "_https._tcp.";
    public static final String TAG = MainActivity.class.getSimpleName();
    private InetAddress hostAddress;
    private int hostPort;
    private NsdManager mNsdManager;
    private Socket socket;


    NsdManager.DiscoveryListener mDiscoveryListener = new NsdManager.DiscoveryListener() {

        // Called as soon as service discovery begins.
        @Override
        public void onDiscoveryStarted(String regType) {
            Log.d(TAG, "Service discovery started");
        }

        @Override
        public void onServiceFound(NsdServiceInfo service) {
            // A service was found! Do something with it.
            Log.d(TAG, "Service discovery success : " + service);
            Log.d(TAG, "Host = "+ service.getServiceName());
            Log.d(TAG, "port = " + String.valueOf(service.getPort()));

            if(service.getServiceName().compareToIgnoreCase(SERVICE_NAME)==0){
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
            Log.d(TAG, "onServiceResolved: "+hostPort+" "+hostAddress);

            Thread thread = new Thread(new ClientThread());
            thread.start();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mNsdManager = (NsdManager) getSystemService(Context.NSD_SERVICE);
    }


    public void onClick(View view) {
        try {
            EditText et = (EditText) findViewById(R.id.EditText01);
            String str = et.getText().toString();
            PrintWriter out = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream())),
                    true);
            out.println(str);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


//    // Start to run the server
//    public void cwRun(){
//        Log.d(TAG, "cwRun: ");
//        SSLContext sslContext = this.createSSLContext();
//        Log.d(TAG, "cwRun: sslcontext "+sslContext);
//        try{
//            // Create socket factory
//            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
//
//            // Create socket
//            SSLSocket sslSocket = (SSLSocket) sslSocketFactory.createSocket(hostAddress, hostPort);
//            Log.d(TAG, "cwRun: SSL client started");
//            new ClientThread(sslSocket).start();
//        } catch (Exception ex){
//            ex.printStackTrace();
//        }
//    }


//    // Thread handling the socket to server
//    static class ClientThread extends Thread {
//        private SSLSocket sslSocket = null;
//
//        ClientThread(SSLSocket sslSocket){
//            this.sslSocket = sslSocket;
//        }
//
//        public void run(){
//            sslSocket.setEnabledCipherSuites(sslSocket.getEnabledCipherSuites());
//
//            try{
//                // Start handshake
//                sslSocket.startHandshake();
//
//                // Get session after the connection is established
//                SSLSession sslSession = sslSocket.getSession();
//
//                Log.d(TAG,"SSLSession :");
//                Log.d(TAG,"\tProtocol : "+sslSession.getProtocol());
//                Log.d(TAG,"\tCipher suite : "+sslSession.getCipherSuite());
//
//
//
//
//
//                // Start handling application content
//                InputStream inputStream = sslSocket.getInputStream();
//                OutputStream outputStream = sslSocket.getOutputStream();
//
//                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
//                PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(outputStream));
//
//                // Write data
//                printWriter.println("Hello server");
//                printWriter.println();
//                printWriter.flush();
//
//                String line = null;
//                while((line = bufferedReader.readLine()) != null){
//                    Log.d(TAG, "run: application client data "+line);
//
//                    if(line.trim().equals("HTTP/1.1 200\r\n")){
//                        break;
//                    }
//                }
//
//                sslSocket.close();
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//        }
//    }


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
                        Toast.makeText(MainActivity.this, e1.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            } catch (final IOException e1) {
                e1.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, e1.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
            catch (final Exception e){
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
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




    // Create the and initialize the SSLContext
    private SSLContext createSSLContext(){
        try{
            Log.d(TAG, "createSSLContext: ");

            TrustManager[] tm = getTrustManagers(this);
            // Initialize SSLContext
            SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
            sslContext.init(null,  tm, null);
            return sslContext;
        } catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }




    private static TrustManager[] getTrustManagers(Context context) {
        TrustManager[] trustManagers;
        try {
            Log.d(TAG, "getTrustManagers: ");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, null);

            InputStream certInputStream = context.getAssets().open("server.crt");
            BufferedInputStream bis = new BufferedInputStream(certInputStream);
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            while (bis.available() > 0) {
                Certificate cert = certificateFactory.generateCertificate(bis);
                keyStore.setCertificateEntry("ca", cert);
            }
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            trustManagers = trustManagerFactory.getTrustManagers();
            return trustManagers;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
