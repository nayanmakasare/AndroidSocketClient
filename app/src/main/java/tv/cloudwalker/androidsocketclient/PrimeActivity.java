package tv.cloudwalker.androidsocketclient;

import android.net.nsd.NsdServiceInfo;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import Utils.PreferenceManager;
import connection.NSDDiscover;
import fragment.LinkedDevicesFragment;
import fragment.MovieBox;
import fragment.ProfileFragment;
import fragment.TvRemoteFragment;
import fragment.YoutubeFragment;

public class PrimeActivity extends AppCompatActivity {

    public NSDDiscover mNSDDiscover;
    public List<NsdServiceInfo> nsdServiceInfoList = new ArrayList<>();
    public PreferenceManager preferenceManager;
    public BottomNavigationView navigation;
    public List<String> serviceList = new ArrayList<>();
    public ArrayAdapter<String> adapter1;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = item -> {
        switch (item.getItemId()) {
            case R.id.deviceHistory:
                loadFragment(new LinkedDevicesFragment());
                return true;
            case R.id.remoteController:
                loadFragment(new TvRemoteFragment());
                return true;
            case R.id.profileNavigation:
                loadFragment(new ProfileFragment());
                return true;
            case R.id.movieBox:
                loadFragment(new MovieBox());
                return true;
            case R.id.youtube:
                getSupportActionBar().hide();
                loadFragment(new YoutubeFragment());
                return true;
        }
        return false;
    };

    public NSDDiscover.DiscoveryListener mDiscoveryListener = new NSDDiscover.DiscoveryListener() {
        @Override
        public void serviceDiscovered(String host, int port) {
            mNSDDiscover.sayHello("sendTvInfo");
        }

        @Override
        public void foundService(NsdServiceInfo nsdServiceInfo) {
            // checking if  the device is a linked device
            Set<String> linkedDeviceSet = preferenceManager.getLinkedNsdDevices();
            if(linkedDeviceSet != null && linkedDeviceSet.size() > 0){
                for(String device : linkedDeviceSet){
                    if(nsdServiceInfo.getServiceName().contains(device)){
                        mNSDDiscover.resolveFoundNsdService(nsdServiceInfo);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                navigation.setSelectedItemId(R.id.remoteController);
                            }
                        });
                        return;
                    }
                }
            }
            nsdServiceInfoList.add(nsdServiceInfo);
            serviceList.add(nsdServiceInfo.getServiceName());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter1.notifyDataSetChanged();
                }
            });
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prime);
        preferenceManager = new PreferenceManager(this);
        adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_2, android.R.id.text1, serviceList);
        mNSDDiscover = new NSDDiscover(getApplicationContext(), mDiscoveryListener);
        mNSDDiscover.discoverServices();
        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.deviceHistory);
    }


    private void loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
        }
    }

}
