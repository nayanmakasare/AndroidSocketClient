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
import com.google.android.gms.common.api.ApiException;
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
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .requestIdToken(getString(R.string.webApplicationClientId))
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }


    @Override
    protected void onStart() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account == null) {
            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
        }else {
            updateUI(account);
        }
        super.onStart();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 777) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }


    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            updateUI(account);
        } catch (ApiException e) {
            e.printStackTrace();
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode()+" "+e.getMessage()+" "+e.getStatusMessage());
            updateUI(null);
        }
    }



    private void updateUI(GoogleSignInAccount account)
    {
        if(account != null){
            PreferenceManager preferenceManager = new PreferenceManager(this);
            ((TextView)findViewById(R.id.signInmessage)).setText("Successfull");
            preferenceManager.setUserEmail(account.getEmail());
            preferenceManager.setUserName(account.getDisplayName());
            preferenceManager.setGoogleId(account.getId());
            preferenceManager.setIsGoogleSignIn(true);
            preferenceManager.setProfileImageUrl(account.getPhotoUrl().toString());
            startActivity(new Intent(SignInActivity.this, IntermideateActivity.class));
            onBackPressed();
        }else {
            ((TextView)findViewById(R.id.signInmessage)).setText("No success");
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
            default:
        }

    }

    private void signOut() {
        Log.d(TAG, "signOut: ");
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
