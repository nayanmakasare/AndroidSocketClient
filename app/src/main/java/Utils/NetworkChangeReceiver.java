package Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by cognoscis on 13/3/18.
 */

public class NetworkChangeReceiver extends BroadcastReceiver {

    private Set<NetworkConnectivityInterface> connectivityInterfaceSet;
    private Boolean connected = false;

    public NetworkChangeReceiver() {
        connectivityInterfaceSet = new HashSet<>();
    }

    public void addListener(NetworkConnectivityInterface networkConnectivityInterface) {
        connectivityInterfaceSet.add(networkConnectivityInterface);
    }

    public void removeListener(NetworkConnectivityInterface networkConnectivityInterface) {
        connectivityInterfaceSet.remove(networkConnectivityInterface);
    }

    private void notifyAllListener() {
        for (NetworkConnectivityInterface networkConnectivityInterface : connectivityInterfaceSet) {
            notifyListener(networkConnectivityInterface);
        }
    }

    private void notifyListener(NetworkConnectivityInterface networkConnectivityInterface) {
        if (connected) {
            networkConnectivityInterface.networkConnected();
        } else {
            networkConnectivityInterface.networkDisconnected();
        }
    }


    @Override
    public void onReceive(final Context context, final Intent intent) {
        if (intent.getAction() != null && intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            connected = NetworkUtils.getConnectivityStatus(context) != NetworkUtils.TYPE_NOT_CONNECTED;
            Toast.makeText(context, connected.toString() , Toast.LENGTH_LONG).show();
            notifyAllListener();
        }
    }

    public interface NetworkConnectivityInterface {
        void networkConnected();
        void networkDisconnected();
    }
}
