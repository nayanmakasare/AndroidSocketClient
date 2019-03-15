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

import Utils.PreferenceManager;

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
        findViewById(R.id.interactTv).setOnClickListener(this);
        receiverServiceIntent = new Intent(this, ReceiverService.class);
        receiverServiceIntent.putExtra("imei", new PreferenceManager(this).getGoogleId());
        receiverServiceIntent.putExtra("rabbit", getIntent().getStringExtra("rabbit"));
        startService(receiverServiceIntent);
        bindService(receiverServiceIntent, serviceConnection, BIND_AUTO_CREATE);


//        askForPermission("android.permission.READ_PHONE_STATE", 1 );
    }

    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(RabbitActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {
            // Should show an explanation
            if (ActivityCompat.shouldShowRequestPermissionRationale(RabbitActivity.this, permission)) {
                ActivityCompat.requestPermissions(RabbitActivity.this, new String[]{permission}, requestCode);
            } else {
                ActivityCompat.requestPermissions(RabbitActivity.this, new String[]{permission}, requestCode);
            }
        } else {
            imeiNumber = getImeiNumber();

            receiverServiceIntent = new Intent(this, ReceiverService.class);
            receiverServiceIntent.putExtra("imei", imeiNumber);
            receiverServiceIntent.putExtra("rabbit", getIntent().getStringExtra("rabbit"));
            startService(receiverServiceIntent);
            bindService(receiverServiceIntent, serviceConnection, BIND_AUTO_CREATE);
            Log.d(TAG, "askForPermission: imei "+imeiNumber);
            Toast.makeText(this,permission + " is already granted.", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    imeiNumber = getImeiNumber();
                    receiverServiceIntent = new Intent(this, ReceiverService.class);
                    receiverServiceIntent.putExtra("imei", imeiNumber);
                    receiverServiceIntent.putExtra("rabbit", getIntent().getStringExtra("rabbit"));
                    startService(receiverServiceIntent);
                    bindService(receiverServiceIntent, serviceConnection, BIND_AUTO_CREATE);
                    Log.d(TAG, "onRequestPermissionsResult: imei "+imeiNumber);
                } else {
                    Toast.makeText(RabbitActivity.this, "You have Denied the Permission", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private String getImeiNumber() {
        final TelephonyManager telephonyManager= (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //getDeviceId() is Deprecated so for android O we can use getImei() method
            return telephonyManager.getImei();
        }
        else {
            return telephonyManager.getDeviceId();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
                receiverService.publishMessageToRabbit("lockTv");
            }
            break;
            default:
        }
    }
}
