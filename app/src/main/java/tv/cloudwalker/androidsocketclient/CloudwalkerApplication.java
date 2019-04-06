package tv.cloudwalker.androidsocketclient;

import android.app.Application;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.util.Log;

import java.util.HashSet;
import java.util.Set;

import Utils.NetworkChangeReceiver;
import Utils.PreferenceManager;
import services.CloudwalkerNSDService;
import services.CloudwalkerRabbitService;

public class CloudwalkerApplication extends Application implements NetworkChangeReceiver.NetworkConnectivityInterface
{
    private NetworkChangeReceiver mNetworkChangeReceiver;
    private PreferenceManager mPreferenceManager;
    public static final String TAG = CloudwalkerApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");
        mPreferenceManager = new PreferenceManager(this);
        preapringRoutingKey();
        mNetworkChangeReceiver = new NetworkChangeReceiver();
        mNetworkChangeReceiver.addListener(this);
        registerReceiver(mNetworkChangeReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }


    private void preapringRoutingKey() {
        if(mPreferenceManager.isGoogleSignIn() && mPreferenceManager.isCloudwalkerSigIn())
        {
            Log.d(TAG, "preapringRoutingKey: ");
            Set<String> routingKeyString = new HashSet<>();
            StringBuilder sb = new StringBuilder("cw.app");
            routingKeyString.add(sb.toString());
            sb.append(".");
            sb.append(mPreferenceManager.getGoogleId());
            routingKeyString.add(sb.toString());
            mPreferenceManager.setRoutingKey(routingKeyString);
        }
    }

    @Override
    public void networkConnected() {
        Log.d(TAG, "networkConnected: ");
//        if(mPreferenceManager.isGoogleSignIn() && mPreferenceManager.isCloudwalkerSigIn())
        {
            Log.d(TAG, "networkConnected: in");
            startService(new Intent(getApplicationContext(), CloudwalkerNSDService.class));
//            startService(new Intent(getApplicationContext(), CloudwalkerRabbitService.class));
        }
    }

    @Override
    public void networkDisconnected() {
        Log.d(TAG, "networkDisconnected: ");
//        if(mPreferenceManager.isCloudwalkerSigIn() && mPreferenceManager.isGoogleSignIn())
        {
            Log.d(TAG, "networkDisconnected: in ");
            stopService(new Intent(getApplicationContext(), CloudwalkerNSDService.class));
//            stopService(new Intent(getApplicationContext(), CloudwalkerRabbitService.class));
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        if(mNetworkChangeReceiver != null){
            mNetworkChangeReceiver.removeListener(this);
            unregisterReceiver(mNetworkChangeReceiver);
        }
    }
}
