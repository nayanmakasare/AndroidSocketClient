package services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.squareup.otto.Subscribe;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import Utils.OttoBus;
import Utils.PreferenceManager;
import api.ProfileApiInterface;
import api.TVApiClient;
import model.NsdServiceFound;
import model.NsdServiceResolve;
import model.NsdSocketReceiveMessage;
import model.NsdSocketSendMessage;
import model.UserProfileBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CloudwalkerNSDService extends Service implements NsdManager.DiscoveryListener {
    public static final String TAG = CloudwalkerNSDService.class.getSimpleName();
    private NsdManager mNsdManager;
    private InetAddress hostAddress;
    private int hostPort;
    private Socket socket = null;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: ");
        OttoBus.getBus().register(this);
        //nsd
        mNsdManager = (NsdManager) getSystemService(Context.NSD_SERVICE);
        mNsdManager.discoverServices("_http._tcp.", NsdManager.PROTOCOL_DNS_SD, this);
        return START_STICKY;
    }

    // Called as soon as service discovery begins.
    @Override
    public void onDiscoveryStarted(String regType) {
        Log.d(TAG, "Service discovery started");
    }

    @Override
    public void onServiceFound(NsdServiceInfo service) {
        String SERVICE_NAME = "cloudtv_";
        if(service.getServiceName().contains(SERVICE_NAME)) {
            Log.d(TAG, "onServiceFound: cloudwalkerService");
//            OttoBus.getBus().post(new NsdServiceFound(service));


            mNsdManager.resolveService(service, new NsdManager.ResolveListener() {
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

                    new PreferenceManager(CloudwalkerNSDService.this).setNsdServiceOn(true);

                    Log.d(TAG, "onServiceResolved: " + hostPort + " " + hostAddress);
                    Thread clientThread = new Thread(new ClientThread());
                    clientThread.start();
                }
            });

        }
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

    class ClientThread implements Runnable {
        @Override
        public void run() {
            try {
                Log.d(TAG, "run: client thread " + hostAddress + " " + hostPort);
                socket = new Socket(hostAddress, hostPort);
                Log.d(TAG, "run: socket created communincation thread started");
                CommunicationThread commThread = new CommunicationThread(socket);
                new Thread(commThread).start();
            } catch (final UnknownHostException e1) {
                e1.printStackTrace();
            } catch (final IOException e1) {
                e1.printStackTrace();
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Subscribe
    public void sentMessageFromNsd(final NsdSocketSendMessage nsdSocketSendMessage) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                    out.println(nsdSocketSendMessage.getSendSocketMessage());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }


    @Subscribe
    public void resolveNsdService(NsdServiceResolve nsdServiceResolve)
    {
        Log.d(TAG, "resolveNsdService: ");
        mNsdManager.resolveService(nsdServiceResolve.getNsdServiceInfo(), new NsdManager.ResolveListener() {
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

                new PreferenceManager(CloudwalkerNSDService.this).setNsdServiceOn(true);

                Log.d(TAG, "onServiceResolved: " + hostPort + " " + hostAddress);
                Thread clientThread = new Thread(new ClientThread());
                clientThread.start();
            }
        });
    }



    class CommunicationThread implements Runnable {
        private Socket Socket;

        private BufferedReader input;

        CommunicationThread(Socket Socket) {
            Log.d(TAG, "CommunicationThread: construct");
            this.Socket = Socket;
            try {
                this.input = new BufferedReader(new InputStreamReader(this.Socket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run() {
            Log.d(TAG, "run: communincationThread ");
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Log.d(TAG, "run: in while pre read");
                    String read = input.readLine();
                    Log.d(TAG, "run: in while post read");
                    if (read != null) {
                        Log.d(TAG, "run: server reply " + read);
                        if (read.contains("tvinfo~")) {
                            PreferenceManager preferenceManager = new PreferenceManager(CloudwalkerNSDService.this);
                            if (preferenceManager.getTvInfo() == null) {
                                preferenceManager.setTvInfo(read);
                                Log.d(TAG, "run: tvinfo set");
                                UserProfileBody userProfileBody = new UserProfileBody();
                                userProfileBody.setName(preferenceManager.getUserName());
                                userProfileBody.setDob(preferenceManager.getDob());
                                userProfileBody.setGender(preferenceManager.getGender());
                                ArrayList<String> genreStringArray = new ArrayList<>();
                                genreStringArray.add(preferenceManager.getGenre());
                                userProfileBody.setGenres(genreStringArray);
                                ArrayList<String> languageArrayList = new ArrayList<>();
                                languageArrayList.add(preferenceManager.getLanguage());
                                userProfileBody.setLanguages(languageArrayList);
                                ArrayList<String> contentTypeArrayList = new ArrayList<>();
                                contentTypeArrayList.add(preferenceManager.getType());
                                userProfileBody.setContent_type(contentTypeArrayList);

                                Log.d(TAG, "run: userProfileObject created");

                                TVApiClient.getClient(CloudwalkerNSDService.this)
                                        .create(ProfileApiInterface.class).postUserProfile(userProfileBody)
                                        .enqueue(new Callback<ResponseBody>() {
                                            @Override
                                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                Log.d(TAG, "onResponse: "+response.code());
                                                if (response.code() == 200) {
                                                    OttoBus.getBus().post(new NsdSocketSendMessage("fetchProfile"));
                                                } else {
                                                    Toast.makeText(CloudwalkerNSDService.this, "Could not save profile. Please try again", Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                Toast.makeText(CloudwalkerNSDService.this, "Could not save profile. Please try again", Toast.LENGTH_SHORT).show();
                                                Log.d(TAG, "onFailure: " + t.getMessage());
                                            }
                                        });
                            }
                        }
                        OttoBus.getBus().post(new NsdSocketReceiveMessage(read));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d(TAG, "run: ioexception "+e.getMessage());
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        mNsdManager.stopServiceDiscovery(this);
        OttoBus.getBus().post(new NsdSocketSendMessage("exit"));
        OttoBus.getBus().unregister(this);
        super.onDestroy();
    }

}


//class sentMessage implements Runnable
//{
//    @Override
//    public void run()
//    {
//        Log.d(TAG, "run: sendM");
//        try
//        {
//            Log.d(TAG, "run: sendM try");
//            Socket client = serverSocket.accept();
//            Log.d(TAG, "run: sendM socket accpted");
//            DataOutputStream os = new DataOutputStream(client.getOutputStream());
//            Log.d(TAG, "run: sendM dos");
//            String str = "cw.tv.5510.32.ADINFOUIFIUF";
//            os.writeBytes(str);
//            Log.d(TAG, "run: sendM writeBytes");
//            os.flush();
//            os.close();
//            client.close();
//            isTvInfoSend = true;
//        }
//        catch(IOException e)
//        {
//            e.printStackTrace();
//            Log.d(TAG, "run: sendM "+e.getMessage());
//        }
//    }
//}


//public class serverThread implements Runnable
//{
//    @Override
//    public void run()
//    {
//        Log.d(TAG, "run: server");
//        try
//        {
//            Log.d(TAG, "run: server try");
//            while(true)
//            {
//                Log.d(TAG, "run: server while");
//                serverSocket = new ServerSocket(SERVERPORT);
//                Log.d(TAG, "run: server serversocket");
//                Socket client = serverSocket.accept();
//                Log.d(TAG, "run: server socket");
//                if(!isTvInfoSend){
//                    Log.d(TAG, "run: server tvinfo");
//                    Thread sentThread = new Thread(new sentMessage());
//                    sentThread.start();
//                }
//                DataInputStream in = new DataInputStream(client.getInputStream());
//                Log.d(TAG, "run: server dis");
//                String line = null;
//                while((line = in.readLine()) != null)
//                {
//                    Log.d(TAG, "run: server while");
//                    Log.d(TAG, "run: client says "+line);
//                }
//                Log.d(TAG, "run: server while end");
//                in.close();
//                client.close();
//                Thread.sleep(100);
//                Log.d(TAG, "run: server try end");
//            }
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//            Log.d(TAG, "run: server error "+e.getMessage());
//        }
//    }
//}



