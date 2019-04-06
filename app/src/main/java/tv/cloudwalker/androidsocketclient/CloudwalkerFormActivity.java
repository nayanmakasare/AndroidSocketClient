package tv.cloudwalker.androidsocketclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import Utils.PreferenceManager;
import api.ProfileApiInterface;
import api.TVApiClient;
import de.hdodenhof.circleimageview.CircleImageView;
import model.UserProfileBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import services.CloudwalkerNSDService;
import services.CloudwalkerRabbitService;

public class CloudwalkerFormActivity extends Activity implements View.OnClickListener {

    public static final String TAG = CloudwalkerFormActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloudwalker_form);
        PreferenceManager preferenceManager = new PreferenceManager(CloudwalkerFormActivity.this);
        ((TextView)findViewById(R.id.email)).setText(preferenceManager.getUserEmail());
        ((TextView)findViewById(R.id.name)).setText(preferenceManager.getUserName());
        ((TextView)findViewById(R.id.googleid)).setText(preferenceManager.getGoogleId());
        findViewById(R.id.email_sign_in_button).setOnClickListener(this);
        findViewById(R.id.logout).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        final PreferenceManager preferenceManager = new PreferenceManager(v.getContext());
        switch (v.getId()) {
            case R.id.email_sign_in_button:
                preferenceManager.setIsCloudwalkerSignIn(true);
                preferenceManager.setGenre(((EditText)findViewById(R.id.genre)).getText().toString());
                preferenceManager.setDob(((EditText)findViewById(R.id.dob)).getText().toString());
                preferenceManager.setGender(((EditText)findViewById(R.id.gender)).getText().toString());


                startService(new Intent(getApplicationContext(), CloudwalkerNSDService.class));
                startService(new Intent(getApplicationContext(), CloudwalkerRabbitService.class));

                Toast.makeText(CloudwalkerFormActivity.this, "NSD and Rabbit Services are on", Toast.LENGTH_LONG).show();
                Intent intent=new Intent(CloudwalkerFormActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

//                final StringBuilder sb = new StringBuilder("profile~");
//                sb.append(preferenceManager.getUserName());
//                sb.append("~");
//                sb.append(preferenceManager.getUserEmail());
//                sb.append("~");
//                sb.append(preferenceManager.getGoogleId());
//                sb.append("~");
//                sb.append(preferenceManager.getProfileImageUrl());
//                sb.append("~");
//                sb.append(preferenceManager.getDob());
//                sb.append("~");
//                sb.append(preferenceManager.getPreference());
//                sb.append("~");
//                sb.append(preferenceManager.getGenre());
//
//                Set<String> profileSet ;
//                if(preferenceManager.getProfiles() == null){
//                    profileSet = new HashSet<>();
//                }else {
//                    profileSet = preferenceManager.getProfiles();
//                }
//
//                profileSet.add(sb.toString());
//                preferenceManager.setProfiles(profileSet);

                break;

            case R.id.logout:
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .requestProfile()
                        .requestIdToken(getString(R.string.clientId))
                        .build();

                // Build a GoogleSignInClient with the options specified by gso.
                GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
                mGoogleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //On Succesfull signout we navigate the user back to LoginActivity
                        preferenceManager.setIsGoogleSignIn(false);
                        preferenceManager.setIsCloudwalkerSignIn(false);
                        onBackPressed();
                    }
                });
                break;

            default:
        }
    }
}
