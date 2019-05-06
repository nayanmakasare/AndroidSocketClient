package connection;

import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import Utils.Constants;
import Utils.OttoBus;
import Utils.PreferenceManager;
import api.MyProfileInterface;
import model.NsdServiceFound;
import newDeviceModel.TvInfo;
import newDeviceModel.TvInfoList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tv.cloudwalker.androidsocketclient.R;

/**
 * Created by martincazares on 3/10/15.
 */
public class NSDDiscover {

    public static final String TAG = "TrackingFlow";
    public String mDiscoveryServiceName = "cloudtv_";
    private Context mContext;
    private NsdManager mNsdManager;
    private DiscoveryListener mListener;
    private String mHostFound;
    private int mPortFound;
    private DISCOVERY_STATUS mCurrentDiscoveryStatus = DISCOVERY_STATUS.OFF;
    private PreferenceManager preferenceManager;

    private enum DISCOVERY_STATUS{
        ON,
        OFF
    }

    public NSDDiscover(Context context, DiscoveryListener listener) {
        this.mContext = context;
        this.mListener = listener;
        this.mNsdManager = (NsdManager) context.getSystemService(Context.NSD_SERVICE);
        preferenceManager = new PreferenceManager(context);
    }

    public void discoverServices() {
        if(mCurrentDiscoveryStatus == DISCOVERY_STATUS.ON)return;
        Toast.makeText(mContext, "Discover SERVICES!", Toast.LENGTH_LONG).show();
        mCurrentDiscoveryStatus = DISCOVERY_STATUS.ON;
        mNsdManager.discoverServices(Constants.SERVICE_TYPE, NsdManager.PROTOCOL_DNS_SD, mDiscoveryListener);
    }

    public void sayHello(String message){
        if(mHostFound == null || mPortFound <= 0){
            Toast.makeText(mContext, mContext.getString(R.string.devices_not_found_toast), Toast.LENGTH_LONG).show();
            return;
        }

        new SocketConnection().sayHello(mHostFound, mPortFound, message);
    }

    NsdManager.ResolveListener mResolveListener = new NsdManager.ResolveListener() {
        @Override
        public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {
            Log.e(TAG, "Resolve failed" + errorCode);
        }

        @Override
        public void onServiceResolved(NsdServiceInfo serviceInfo) {
            Log.e(TAG, "Resolve Succeeded. " + serviceInfo);
            Toast.makeText(mContext, "FOUND A CONNECTION!", Toast.LENGTH_LONG).show();
//            mNsdManager.stopServiceDiscovery(mDiscoveryListener);//TODO: You can remove this line if necessary, that way the discovery process continues...
            setHostAndPortValues(serviceInfo);
            if(mListener != null){
                Log.d(TAG, "onServiceResolved: to listiner");
                mListener.serviceDiscovered(mHostFound, mPortFound);
            }
        }
    };

    private void setHostAndPortValues(NsdServiceInfo serviceInfo){
        mHostFound = serviceInfo.getHost().getHostAddress();
        mPortFound = serviceInfo.getPort();
    }

    private class SocketConnection {
        private Socket mSocket;
        public void sayHello(final String host, final int port, final  String nsdMessage){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    mSocket = new Socket();
                    SocketAddress address = new InetSocketAddress(host, port);
                    try {
                        Log.e("TrackingFlow", "Trying to connect to: " + host);
                        mSocket.connect(address);
                        DataOutputStream os = new DataOutputStream(mSocket.getOutputStream());
                        DataInputStream is = new DataInputStream(mSocket.getInputStream());
                        //Send a message...
                        os.write(nsdMessage.getBytes());
                        os.flush();
                        Log.e("TrackingFlow", "Message SENT!!!");

                        //Read the message
                        int bufferSize = 1024;
                        byte[] buffer = new byte[bufferSize];
                        StringBuilder sb = new StringBuilder();
                        int length = Integer.MAX_VALUE;
                        try {
                            while (length >= bufferSize) {
                                length = is.read(buffer);
                                sb.append(new String(buffer, 0, length));
                            }
                            final String receivedMessage = sb.toString();
                            Log.d(TAG, "run: recevice message "+receivedMessage);
                            if(receivedMessage.contains("tvinfo~"))
                            {
                                Log.d(TAG, "run: recevice in if ");
                                Set<String> deviceList = preferenceManager.getLinkedNsdDevices();
                                Log.d(TAG, "run: got device list ");
                                boolean isDeviceLinked = false;
                                if(deviceList != null && !deviceList.isEmpty() && deviceList.size() > 0)
                                {
                                    for(String device : deviceList) {
                                        if(receivedMessage.contains(device)) {
                                            Toast.makeText(mContext, "Device Found "+device, Toast.LENGTH_SHORT).show();
                                            isDeviceLinked = true;
                                            break;
                                        }
                                    }
                                }

                                Log.d(TAG, "run: linked result "+isDeviceLinked);
                                if(!isDeviceLinked) {
                                    registerDevice(receivedMessage);
                                }

                            }
                            //TODO:Send message on the main thread, Note: We don't need to create a thread every time, this is just for prototyping...
                            new Handler(mContext.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(mContext, "Message received: " + receivedMessage, Toast.LENGTH_LONG).show();
                                }
                            });
                        } catch (Exception e) {e.printStackTrace();}
                        os.close();
                        is.close();

                    } catch (IOException e) {e.printStackTrace();}
                }
            }).start();
        }
    }

    private void registerDevice(String infoString)
    {
        Log.d(TAG, "registerDevice: "+infoString);
        String[] contentPices = infoString.split("~");
        TvInfo tvInfo = new TvInfo();
        tvInfo.setBoardName(contentPices[1]);
        tvInfo.setPanelName(contentPices[2]);
        tvInfo.setEmac(contentPices[4]);

        Log.d(TAG, "registerDevice: ");

        new Retrofit.Builder()
                .baseUrl("http://192.168.1.143:4000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MyProfileInterface.class).postNewTvDevice(tvInfo, preferenceManager.getGoogleId())
                .enqueue(new Callback<TvInfo>() {
                    @Override
                    public void onResponse(Call<TvInfo> call, Response<TvInfo> response) {
                        Log.d(TAG, "onResponse: lniked "+response.code());
                        if(response.code() == 200) {
                            Set<String> deviceList = preferenceManager.getLinkedNsdDevices();
                            if(deviceList == null) {
                                deviceList = new HashSet<>();
                            }
                            deviceList.add(contentPices[4]);
                            preferenceManager.setLinkedNsdDevices(deviceList);
                        }
                    }

                    @Override
                    public void onFailure(Call<TvInfo> call, Throwable t) {
                        Log.d(TAG, "onFailure: "+t.getMessage());

                    }
                });
    }

    public void resolveFoundNsdService(NsdServiceInfo nsdServiceInfo) {
        mNsdManager.resolveService(nsdServiceInfo, mResolveListener);
    }

    private NsdManager.DiscoveryListener mDiscoveryListener = new NsdManager.DiscoveryListener() {

        @Override
        public void onDiscoveryStarted(String regType) {
            Log.d(TAG, "Service discovery started");
        }

        @Override
        public void onServiceFound(NsdServiceInfo service) {
            Log.d(TAG, "Service discovery success " + service);

            if(service.getServiceType().equals(Constants.SERVICE_TYPE) && service.getServiceName().contains(mDiscoveryServiceName))
            {
                Log.d(TAG, "onServiceFound: cloud Tv");
                mListener.foundService(service);
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
            mCurrentDiscoveryStatus = DISCOVERY_STATUS.OFF;
            mNsdManager.stopServiceDiscovery(this);
        }
    };

    /**
     * Registration Listener for our NDS Listen logic
     */
    private NsdManager.RegistrationListener mRegistrationListener = new NsdManager.RegistrationListener() {
        @Override
        public void onServiceRegistered(NsdServiceInfo NsdServiceInfo) {
            mDiscoveryServiceName = NsdServiceInfo.getServiceName();
            Log.e("TrackingFlow", "This device has been registered to be discovered through NSD...");
        }

        @Override
        public void onRegistrationFailed(NsdServiceInfo arg0, int arg1) {
        }

        @Override
        public void onServiceUnregistered(NsdServiceInfo arg0) {
        }

        @Override
        public void onUnregistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {
        }

    };

    public void shutdown(){
        try {
            mNsdManager.stopServiceDiscovery(mDiscoveryListener);
        }catch(Exception e){e.printStackTrace();}
    }

    public interface DiscoveryListener {
        public void serviceDiscovered(String host, int port);
        public void foundService(NsdServiceInfo nsdServiceInfo);
    }
}
