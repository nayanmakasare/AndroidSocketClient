package fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import Utils.OttoBus;
import model.NsdSocketSendMessage;
import tv.cloudwalker.androidsocketclient.PrimeActivity;
import tv.cloudwalker.androidsocketclient.R;

public class TvRemoteFragment extends Fragment implements View.OnClickListener
{
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OttoBus.getBus().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        return  inflater.inflate(R.layout.fragment_remote_layout, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.keyBack).setOnClickListener(this);
        view.findViewById(R.id.keyCenter).setOnClickListener(this);
        view.findViewById(R.id.keyLeft).setOnClickListener(this);
        view.findViewById(R.id.keyRight).setOnClickListener(this);
        view.findViewById(R.id.keyUp).setOnClickListener(this);
        view.findViewById(R.id.keyDown).setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.keyCenter :
            {
                ((PrimeActivity)getActivity()).mNSDDiscover.sayHello("23");
                break;
            }
            case R.id.keyLeft :
            {
                ((PrimeActivity)getActivity()).mNSDDiscover.sayHello("21");
                break;
            }
            case R.id.keyRight :
            {
                ((PrimeActivity)getActivity()).mNSDDiscover.sayHello("22");
                break;
            }
            case R.id.keyDown :
            {
                ((PrimeActivity)getActivity()).mNSDDiscover.sayHello("20");
                break;
            }
            case R.id.keyUp :
            {
                ((PrimeActivity)getActivity()).mNSDDiscover.sayHello("19");
                break;
            }
            case R.id.keyBack :
            {
                ((PrimeActivity)getActivity()).mNSDDiscover.sayHello("4");
                break;
            }
        }
    }

    @Override
    public void onDestroy() {
        OttoBus.getBus().unregister(this);
        super.onDestroy();
    }
}
