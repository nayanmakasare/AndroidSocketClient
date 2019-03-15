package tv.cloudwalker.androidsocketclient;

import android.app.Activity;
import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class NSDActivity extends Activity implements  NsdManager.DiscoveryListener {

    private String SERVICE_NAME = "CloudwalkerService";
    private String SERVICE_TYPE = "_http._tcp.";
    public static final String TAG = NSDActivity.class.getSimpleName();
    private InetAddress hostAddress;
    private int hostPort;
    private NsdManager mNsdManager;
//    private Socket socket;
    // Declare variables
    private ListView listView;
    private List<String> serviceList = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private List<NsdServiceInfo> nsdServiceInfoList = new ArrayList<>();
    private Handler updateConversationHandler;
    private String socketClientMessage = "" ;
    private TextView chat ;
    private Handler handler = new Handler();



    // initialize socket and input output streams
    private Socket socket            = null;
    private DataInputStream input   = null;
    private DataOutputStream out     = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nsd);
        listView = findViewById(R.id.listview);
        chat = findViewById(R.id.chat);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, serviceList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String serviceName = (String) listView.getItemAtPosition(position);
                if (serviceName.compareToIgnoreCase(SERVICE_NAME) == 0) {
                    mNsdManager.resolveService(nsdServiceInfoList.get(position), new NsdManager.ResolveListener() {
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
                            hostPort = serviceInfo.getPort();
                            hostAddress = serviceInfo.getHost();
                            Log.d(TAG, "onServiceResolved: " + hostPort + " " + hostAddress);
                            Thread clientThread = new Thread(new ClientThread());
                            clientThread.start();
                        }
                    });
                } else {
                    Toast.makeText(view.getContext(), "Not a Cloudwalker Service. Please select Cloudwalker Service. ", Toast.LENGTH_LONG).show();
                }
            }
        });

        findViewById(R.id.myButton).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Thread sentThread = new Thread(new sentMessage());
                sentThread.start();
            }
        });

        //nsd
        mNsdManager = (NsdManager) getSystemService(Context.NSD_SERVICE);

        //handler
        updateConversationHandler = new Handler();
    }

    // Called as soon as service discovery begins.
    @Override
    public void onDiscoveryStarted(String regType) {
        Log.d(TAG, "Service discovery started");
    }

    @Override
    public void onServiceFound(NsdServiceInfo service) {
        nsdServiceInfoList.add(service);
        serviceList.add(service.getServiceName());
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });
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
        mNsdManager.stopServiceDiscovery(this);
    }

    @Override
    public void onStopDiscoveryFailed(String serviceType, int errorCode) {
        Log.e(TAG, "Discovery failed: Error code:" + errorCode);
        mNsdManager.stopServiceDiscovery(this);
    }




    public class ClientThread implements Runnable
    {
        public void run()
        {
            try
            {
                while(true)
                {
                    Socket socket = new Socket(hostAddress, hostPort); //
                    DataInputStream in = new DataInputStream(socket.getInputStream());
                    String line = null;
                    while ((line = in.readLine()) != null)
                    {
                        final String finalLine = line;
                        handler.post(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                chat.setText(finalLine);
                            }
                        });
                    }
                    in.close();
                    socket.close();
                    Thread.sleep(100);
                }
            }
            catch (Exception e)
            {

            }
        }
    }



    class sentMessage implements Runnable
    {
        @Override
        public void run()
        {
            try
            {
                Socket socket = new Socket(hostAddress, hostPort);
                EditText editText = findViewById(R.id.EditText01);
                DataOutputStream os = new DataOutputStream(socket.getOutputStream());
                os.writeBytes(editText.getText().toString());
                os.flush();
                os.close();
                socket.close();
            }
            catch(IOException e)
            {
            }
        }
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause: ");
        if (mNsdManager != null) {
            nsdServiceInfoList.clear();
            serviceList.clear();
            adapter.notifyDataSetChanged();
            mNsdManager.stopServiceDiscovery(this);
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
        if (mNsdManager != null) {
            mNsdManager.discoverServices(
                    SERVICE_TYPE, NsdManager.PROTOCOL_DNS_SD, this);
        }
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        if (mNsdManager != null) {
            mNsdManager.stopServiceDiscovery(this);
        }
        super.onDestroy();
    }

//    @Override
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.myButton: {
//                //get TvInfo
//                Log.d(TAG, "onClick: ");
//                EditText editText = findViewById(R.id.EditText01);
//                final String message = editText.getText().toString();
//                if(!message.isEmpty() && hostAddress != null && hostPort != 0)
//                {
//                    Log.d(TAG, "onClick: in if ");
//                    try {
//                        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
//                        out.println(message);
////                        Intent rabbitIntent = new Intent(view.getContext(), RabbitActivity.class);
////                        startActivity(rabbitIntent);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }else {
//                    Toast.makeText(view.getContext(), "Host and port dont know ", Toast.LENGTH_LONG).show();
//                }
//            }
//            break;
//        }
//    }

}





//    class ClientThread implements Runnable {
//        @Override
//        public void run() {
//            try {
//                Log.d(TAG, "run: client thread " + hostAddress + " " + hostPort);
//                socket = new Socket(hostAddress, hostPort);
//                Log.d(TAG, "run: socket created communincation thread started");
//                CommunicationThread commThread = new CommunicationThread(socket);
//                new Thread(commThread).start();
//            } catch (final UnknownHostException e1) {
//                e1.printStackTrace();
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(NSDActivity.this, e1.getMessage(), Toast.LENGTH_LONG).show();
//                    }
//                });
//            } catch (final IOException e1) {
//                e1.printStackTrace();
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(NSDActivity.this, e1.getMessage(), Toast.LENGTH_LONG).show();
//                    }
//                });
//            } catch (final Exception e) {
//                e.printStackTrace();
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(NSDActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
//                    }
//                });
//            }
//        }
//    }
//
//
//    class CommunicationThread implements Runnable {
//        private Socket Socket;
//
//        private BufferedReader input;
//
//        CommunicationThread(Socket Socket) {
//            Log.d(TAG, "CommunicationThread: construct");
//            this.Socket = Socket;
//            try {
//                this.input = new BufferedReader(new InputStreamReader(this.Socket.getInputStream()));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        public void run() {
//            Log.d(TAG, "run: communincationThread ");
//            while (!Thread.currentThread().isInterrupted()) {
//                try {
//                    Log.d(TAG, "run: communication thread running ");
//                    String read = input.readLine();
//                    if (read != null) {
//                        Log.d(TAG, "run: server reply " + read);
//                        Intent rabbitIntent = new Intent(NSDActivity.this, RabbitActivity.class);
//                        rabbitIntent.putExtra("rabbit", read);
//                        startActivity(rabbitIntent);
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
