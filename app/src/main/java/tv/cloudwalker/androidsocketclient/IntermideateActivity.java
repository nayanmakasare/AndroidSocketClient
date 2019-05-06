package tv.cloudwalker.androidsocketclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.bumptech.glide.Glide;

import Utils.PreferenceManager;
import de.hdodenhof.circleimageview.CircleImageView;

public class IntermideateActivity extends Activity {

    public static final String TAG = IntermideateActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intermideate);
        PreferenceManager preferenceManager = new PreferenceManager(IntermideateActivity.this);
        Log.d(TAG, "onCreate: "+preferenceManager.isGoogleSignIn() +" "+preferenceManager.isCloudwalkerSigIn());
        if(preferenceManager.isGoogleSignIn() && preferenceManager.isCloudwalkerSigIn()){
            startActivity(new Intent(IntermideateActivity.this, PrimeActivity.class));
            onBackPressed();
        }else if(preferenceManager.isGoogleSignIn() && !preferenceManager.isCloudwalkerSigIn()) {
            startActivity(new Intent(IntermideateActivity.this, CloudwalkerPreferenceActivity.class));
            onBackPressed();
        }else {
            ((EditText)findViewById(R.id.fullname)).setText(preferenceManager.getUserName());
            ((EditText)findViewById(R.id.useremail)).setText(preferenceManager.getUserEmail());
            ((EditText)findViewById(R.id.usergoogleId)).setText(preferenceManager.getGoogleId());
            Glide.with(this).load(preferenceManager.getProfileImageUrl()).into((CircleImageView)findViewById(R.id.profile_image));
        }
    }

    public void createPreference(View view) {
        startActivity(new Intent(view.getContext(), CloudwalkerPreferenceActivity.class));
        onBackPressed();
    }
}
