package tv.cloudwalker.androidsocketclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import Utils.PreferenceManager;

public class SignInActivity extends Activity implements View.OnClickListener {

    private GoogleSignInClient mGoogleSignInClient;
    public static final String TAG = SignInActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        findViewById(R.id.sign_in_button).setOnClickListener(this);
        findViewById(R.id.signout).setOnClickListener(this);

        // Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .requestIdToken(getString(R.string.clientId))
                .build();


        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }


    @Override
    protected void onStart() {

        // Check for existing Google Sign In account, if the user is already signed in
// the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account == null)
        {
            // Set the dimensions of the sign-in button.
            SignInButton signInButton = findViewById(R.id.sign_in_button);
            signInButton.setSize(SignInButton.SIZE_STANDARD);
            signInButton.setVisibility(View.VISIBLE);
        }else {
            updateUI(account);
        }
        super.onStart();
    }

    private void updateUI(GoogleSignInAccount account)
    {
        if(account != null){
            Log.d(TAG, "updateUI:display name "+account.getDisplayName());
            Log.d(TAG, "updateUI: email "+account.getEmail());
            Log.d(TAG, "updateUI: id "+account.getId());
            Log.d(TAG, "updateUI: photo "+account.getPhotoUrl());
            Log.d(TAG, "updateUI: zab "+account.zab());
            Log.d(TAG, "updateUI: zac "+account.zac());
            Log.d(TAG, "updateUI: id "+account.getId());
            Log.d(TAG, "updateUI: id token  "+account.getIdToken());
            ((TextView)findViewById(R.id.signInmessage)).setText("Successfull");

//            new PreferenceManager(this).setUserEmail(account.getEmail());
//            new PreferenceManager(this).setUserName(account.getDisplayName());
//            new PreferenceManager(this).setGoogleId(account.getId());

            Intent formIntent = new Intent(SignInActivity.this, CloudwalkerFormActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("fullName",account.getDisplayName());
            bundle.putString("email", account.getEmail());
            bundle.putString("photourl", account.getPhotoUrl().toString());
            bundle.putString("id", account.getId());
            formIntent.putExtra("profile", bundle);
            startActivity(formIntent);
        }else {
            ((TextView)findViewById(R.id.signInmessage)).setText("No success");
        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == 777) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }


    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            e.printStackTrace();
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode()+" "+e.getMessage()+" "+e.getStatusMessage());
            updateUI(null);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;

                case R.id.signout:
                    Log.d(TAG, "onClick: signout");
                signOut();
                break;

            default:
        }

    }

    private void signOut() {

        Log.d(TAG, "signOut: ");
                  /*
          Sign-out is initiated by simply calling the googleSignInClient.signOut API. We add a
          listener which will be invoked once the sign out is the successful
           */
            mGoogleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    //On Succesfull signout we navigate the user back to LoginActivity
                    Log.d(TAG, "onComplete: signout");
                    Intent intent=new Intent(SignInActivity.this,MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 777);
    }
}
