package tv.cloudwalker.androidsocketclient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.nsd.NsdServiceInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import Utils.OttoBus;
import Utils.PreferenceManager;
import api.MyProfileInterface;
import model.NsdServiceFound;
import model.NsdServiceResolve;
import model.profile.Profile;
import model.profile.TvInfo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import services.CloudwalkerNSDService;

public class NsdActivity extends Activity{
    public static final String TAG = NsdActivity.class.getSimpleName();
    private List<String> serviceList = new ArrayList<>();
    private List<String> linkedServiceList = new ArrayList<>();
    private ArrayAdapter<String> adapter1;
    private ArrayAdapter<String> adapter2;
    private List<NsdServiceInfo> nsdServiceInfoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nsd);
        Log.d(TAG, "onCreate: ");
        OttoBus.getBus().register(this);
        PreferenceManager preferenceManager = new PreferenceManager(NsdActivity.this);
        if(preferenceManager.isGoogleSignIn()) {
            Log.d(TAG, "onCreate: in google sign in");
            ListView listView = findViewById(R.id.listview);
            ListView linkedDeviceListView = findViewById(R.id.list_view1);
            adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_2, android.R.id.text1, serviceList);
            adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_2, android.R.id.text1, linkedServiceList);
            Log.d(TAG, "onCreate: adapter created");
            listView.setAdapter(adapter1);
            linkedDeviceListView.setAdapter(adapter2);
            //fetch linked devices
            fetchLinkedDevicesListFromServer();
            Log.d(TAG, "onCreate: set adapter");
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    NsdServiceResolve nsdServiceResolve = new NsdServiceResolve(nsdServiceInfoList.get(position));
                    checkingLinkedDevices(nsdServiceResolve);
                    view.getContext().startActivity(new Intent(view.getContext(), PrimeActivity.class));
                    onBackPressed();
                }
            });

            linkedDeviceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(view.getContext(), linkedServiceList.get(position), Toast.LENGTH_LONG).show();
                }
            });

        }else {
            Log.d(TAG, "onCreate: not google sign it");
            startActivity(new Intent(this, SignInActivity.class));
        }
    }

    private void checkingLinkedDevices(NsdServiceResolve nsdServiceResolve)
    {
        for(String string : linkedServiceList )
        {
            if(!string.equals(nsdServiceResolve.getNsdServiceInfo().getServiceName())) {
                Log.d(TAG, "checkingLinkedDevices: new device found !!!");
                nsdServiceResolve.setLinkedDevice(false);
            }
        }
        OttoBus.getBus().post(nsdServiceResolve);
    }

    private void fetchLinkedDevicesListFromServer() {
        new Retrofit.Builder()
                .baseUrl("http://192.168.1.143:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(MyProfileInterface.class).getUserProfile().enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response) {
                if(response.code() == 200) {
                    Log.d(TAG, "onResponse: 200"+response.body().toString());
                    Profile profile = response.body();
                    List<TvInfo> tvInfoList = profile.getTvinfo();
                    for(TvInfo tvInfo : tvInfoList) {
                        linkedServiceList.add(tvInfo.getEmac());
                    }
                    adapter2.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<Profile> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
    }

    @Subscribe
    public void getNsdServiceInfo(NsdServiceFound nsdServiceFound)
    {
        nsdServiceInfoList.add(nsdServiceFound.getNsdServiceInfo());
        serviceList.add(nsdServiceFound.getNsdServiceInfo().getServiceName());
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter1.notifyDataSetChanged();
            }
        });
    }


    @Override
    public void onDestroy() {
        OttoBus.getBus().unregister(this);
        super.onDestroy();
    }
}
