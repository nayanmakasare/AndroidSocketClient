package fragment;


import android.net.nsd.NsdServiceInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import Utils.PreferenceManager;
import api.MyProfileInterface;
import newDeviceModel.TvInfo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tv.cloudwalker.androidsocketclient.PrimeActivity;
import tv.cloudwalker.androidsocketclient.R;

public class LinkedDevicesFragment extends Fragment
{
    private List<String> linkedServiceList = new ArrayList<>();
    private ArrayAdapter<String> adapter2;
    public static final String TAG = LinkedDevicesFragment.class.getSimpleName();
    private PreferenceManager preferenceManager;
    private TextView linkedDeviceTv;
    private ListView listView, linkedDeviceListView ;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return  inflater.inflate(R.layout.linked_device_fragment_layout, null);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        preferenceManager = new PreferenceManager(view.getContext());
        linkedDeviceTv = view.findViewById(R.id.linkedDevice);
        listView = view.findViewById(R.id.listview);
        linkedDeviceListView = view.findViewById(R.id.list_view1);
        adapter2 = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_list_item_2, android.R.id.text1, linkedServiceList);
        Log.d(TAG, "onCreate: adapter created");
        listView.setAdapter(((PrimeActivity)getActivity()).adapter1);
        linkedDeviceListView.setAdapter(adapter2);
        fetchLinkedDevicesListFromServer();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((PrimeActivity)getActivity()).mNSDDiscover.resolveFoundNsdService(((PrimeActivity)getActivity()).nsdServiceInfoList.get(position));
                ((PrimeActivity)getActivity()).navigation.setSelectedItemId(R.id.remoteController);
            }
        });

        linkedDeviceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                boolean isDeviceLinkedandFound = false;
                for(NsdServiceInfo nsdServiceInfo : ((PrimeActivity)getActivity()).nsdServiceInfoList){
                    if(nsdServiceInfo.getServiceName().compareToIgnoreCase(linkedServiceList.get(position)) == 0)
                    {
                        isDeviceLinkedandFound = true;
                        ((PrimeActivity)getActivity()).navigation.setSelectedItemId(R.id.remoteController);
                        break;
                    }
                }
                if(!isDeviceLinkedandFound){
                    Toast.makeText(view.getContext(), linkedServiceList.get(position)+" is not in the Network.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void fetchLinkedDevicesListFromServer() {
        new Retrofit.Builder()
                .baseUrl("http://192.168.1.143:4000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(MyProfileInterface.class).getLinkDevices(preferenceManager.getGoogleId())
                .enqueue(new Callback<List<TvInfo>>() {
                    @Override
                    public void onResponse(Call<List<TvInfo>> call, Response<List<TvInfo>> response) {
                        if(response.code() == 200) {
                            List<TvInfo> tvInfoList = response.body();
                            if(tvInfoList.isEmpty()) {
                                linkedDeviceTv.setText("No Linked Device for this Profile");
                                linkedDeviceListView.setVisibility(View.GONE);
                            }else {
                                for(TvInfo tvInfo : tvInfoList) {
                                    linkedServiceList.add("cloudtv_"+tvInfo.getEmac());
                                    Set<String> linkedDevice = new HashSet<>();
                                    linkedDevice.add(tvInfo.getEmac());
                                    preferenceManager.setLinkedNsdDevices(linkedDevice);
                                }
                                adapter2.notifyDataSetChanged();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<TvInfo>> call, Throwable t) {
                        Log.d(TAG, "onFailure: "+t.getMessage());
                    }
                });
    }
}


