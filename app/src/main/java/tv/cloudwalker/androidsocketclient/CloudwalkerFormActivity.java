package tv.cloudwalker.androidsocketclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import de.hdodenhof.circleimageview.CircleImageView;

public class CloudwalkerFormActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloudwalker_form);

        Bundle bundle = getIntent().getBundleExtra("profile");

        ((TextView)findViewById(R.id.email)).setText(bundle.getString("email"));
        ((TextView)findViewById(R.id.name)).setText(bundle.getString("fullName"));
        ((TextView)findViewById(R.id.googleid)).setText(bundle.getString("id"));

        findViewById(R.id.email_sign_in_button).setOnClickListener(this);
        findViewById(R.id.logout).setOnClickListener(this);


        Glide.with(this)
                .load(bundle.getString("photourl"))
                .into((CircleImageView)findViewById(R.id.profile_image));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.email_sign_in_button:

                startActivity(new Intent(CloudwalkerFormActivity.this, NSDActivity.class));

                break;

            case R.id.logout:
                // Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
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
                        Intent intent=new Intent(CloudwalkerFormActivity.this,MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });

                break;

            default:
        }
    }
}
