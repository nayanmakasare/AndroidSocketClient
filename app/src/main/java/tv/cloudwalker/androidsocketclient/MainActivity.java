package tv.cloudwalker.androidsocketclient;

import android.net.nsd.NsdManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

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
//        startActivity(new Intent(this, SignInActivity.class));
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
}
