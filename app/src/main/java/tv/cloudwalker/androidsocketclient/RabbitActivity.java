package tv.cloudwalker.androidsocketclient;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import Utils.OttoBus;
import Utils.PreferenceManager;
import model.RabbitSendMessage;

public class RabbitActivity extends Activity implements View.OnClickListener {

    private Intent receiverServiceIntent;
    private String imeiNumber ;
    public static final String TAG = RabbitActivity.class.getSimpleName();
    private Boolean isServiceBind = false;
    private ReceiverService receiverService;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected: ");
            isServiceBind = true;
            receiverService = ((ReceiverService.RabbitBinder)service).getService();
            findViewById(R.id.interactTv).setVisibility(View.VISIBLE);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isServiceBind = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rabbit);
        Log.d(TAG, "onCreate: ");
        OttoBus.getBus().register(this);
        findViewById(R.id.interactTv).setOnClickListener(this);
        receiverServiceIntent = new Intent(this, ReceiverService.class);
        receiverServiceIntent.putExtra("imei", new PreferenceManager(this).getGoogleId());
        receiverServiceIntent.putExtra("rabbit", getIntent().getStringExtra("rabbit"));
        startService(receiverServiceIntent);
        bindService(receiverServiceIntent, serviceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OttoBus.getBus().unregister(this);
        if(isServiceBind){
            unbindService(serviceConnection);
        }
        if (receiverServiceIntent != null) {
            stopService(receiverServiceIntent);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.interactTv : {
                Log.d(TAG, "onClick: interactTV");
                OttoBus.getBus().post(new RabbitSendMessage("locktv", "vttrubte7njujn"));
            }
            break;
            default:
        }
    }
}
