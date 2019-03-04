package tv.cloudwalker.androidsocketclient;

import android.content.Context;
import android.content.Intent;
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
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import Utils.CustomHttpClient;
import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity {

    private String SERVICE_NAME = "CloudwalkerService";
    private String SERVICE_TYPE = "_https._tcp.";
    public static final String TAG = MainActivity.class.getSimpleName();
    private InetAddress hostAddress;
    private int hostPort;
    private NsdManager mNsdManager;
    private Socket socket;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startActivity(new Intent(this, SignInActivity.class));

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


    // Start to run the server
    public void cwRun(){
        Log.d(TAG, "cwRun: ");
        SSLContext sslContext = this.createSSLContext();
        Log.d(TAG, "cwRun: sslcontext "+sslContext);
        try{
                // Create socket factory
    //            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
                SSLSocketFactory sslSocketFactory = CustomHttpClient.getOkHttps(MainActivity.this).sslSocketFactory();

                // Create socket
                SSLSocket sslSocket = (SSLSocket) sslSocketFactory.createSocket(hostAddress, hostPort);
                Log.d(TAG, "cwRun: SSL client started");
//                new ClientThread(sslSocket).start();
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }


    // Thread handling the socket to server
//    static class ClientThread extends Thread {
//        private SSLSocket sslSocket = null;
//
//        ClientThread(SSLSocket sslSocket){
//            this.sslSocket = sslSocket;
//        }
//
//        public void run(){
//            sslSocket.setEnabledCipherSuites(sslSocket.getSupportedCipherSuites());
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
