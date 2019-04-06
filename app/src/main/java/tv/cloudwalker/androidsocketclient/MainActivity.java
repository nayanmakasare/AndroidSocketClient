package tv.cloudwalker.androidsocketclient;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import Utils.OttoBus;
import Utils.PreferenceManager;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PreferenceManager preferenceManager = new PreferenceManager(this);
        Log.d(TAG, "onCreate: "+preferenceManager.isCloudwalkerSigIn()  +" "+preferenceManager.isGoogleSignIn() );

        startActivity(new Intent(this, SignInActivity.class));
//        if(! preferenceManager.isGoogleSignIn()){
//            Log.d(TAG, "onCreate: not signed in");
//            startActivity(new Intent(this, SignInActivity.class));
//        }else {
//            startActivity(new Intent(this, NsdActivity.class));
//        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}






// ((TextView)findViewById(R.id.username)).setText(preferenceManager.getUserName());
//         ((TextView)findViewById(R.id.useremail)).setText(preferenceManager.getUserEmail());
//         ((TextView)findViewById(R.id.usergoogleId)).setText(preferenceManager.getGoogleId());
//         String routingKey = preferenceManager.getTvInfo();
//         if(routingKey != null ){
//         String[] tvInfoString = routingKey.split("~");
//         ((TextView)findViewById(R.id.tvEmac)).setText(tvInfoString[4]);
//         ((TextView)findViewById(R.id.tvSize)).setText(tvInfoString[3]);
//         ((TextView)findViewById(R.id.tvmodel)).setText(tvInfoString[2]);
//         }






//    Intent serviceIntent = new Intent(this, RabbitIntentService.class);
//        serviceIntent.setAction("services.action.FOO");
//        startService(serviceIntent);





























