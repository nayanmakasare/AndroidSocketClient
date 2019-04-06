package tv.cloudwalker.androidsocketclient;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import Utils.OttoBus;
import Utils.PreferenceManager;
import api.ProfileApiInterface;
import api.TVApiClient;
import model.NsdSocketSendMessage;
import model.UserProfileBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import services.CloudwalkerNSDService;

public class ProfileCreationActivity extends Activity {

    public static final String TAG = ProfileCreationActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OttoBus.getBus().register(this);
        setContentView(R.layout.activity_profile_creation);
    }

    public void createProfile(View view)
    {
        UserProfileBody userProfileBody = new UserProfileBody();
        userProfileBody.setName(((EditText)findViewById(R.id.profileName)).getText().toString());
        userProfileBody.setDob(((EditText)findViewById(R.id.profileDob)).getText().toString());
        userProfileBody.setGender(((EditText)findViewById(R.id.profileGender)).getText().toString());
        ArrayList<String> genreStringArray = new ArrayList<>();
        genreStringArray.add(((EditText)findViewById(R.id.profileGenre)).getText().toString());
        userProfileBody.setGenres(genreStringArray);
        ArrayList<String> preferenceArrayList = new ArrayList<>();
        preferenceArrayList.add(((EditText)findViewById(R.id.profilePreference)).getText().toString());
        userProfileBody.setLanguages(preferenceArrayList);
        userProfileBody.setContent_type(preferenceArrayList);

        Log.d(TAG, "run: userProfileObject created");

        TVApiClient.getClient(ProfileCreationActivity.this).create(ProfileApiInterface.class).postUserProfile(userProfileBody)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        Log.d(TAG, "onResponse: "+response.code());
                        if (response.code() == 200) {
                            OttoBus.getBus().post(new NsdSocketSendMessage("fetchProfile"));
                            finish();
                        } else {
                            Toast.makeText(ProfileCreationActivity.this, "Could not save profile. Please try again", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(ProfileCreationActivity.this, "Could not save profile. Please try again", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onFailure: " + t.getMessage());
                    }
                });

//
//        PreferenceManager preferenceManager = new PreferenceManager(view.getContext());
//        StringBuilder sb = new StringBuilder("profile~");
//        sb.append(((EditText)findViewById(R.id.profileName)).getText().toString());
//        sb.append("~");
//        sb.append(((EditText)findViewById(R.id.profileEmail)).getText().toString());
//        sb.append("~");
//        sb.append(preferenceManager.getGoogleId());
//        sb.append("~");
//        sb.append(preferenceManager.getProfileImageUrl());
//        sb.append("~");
//        sb.append(((EditText)findViewById(R.id.profileDob)).getText().toString());
//        sb.append("~");
//        sb.append(((EditText)findViewById(R.id.profilePreference)).getText().toString());
//        sb.append("~");
//        sb.append(((EditText)findViewById(R.id.profileGenre)).getText().toString());
//
//        Set<String> profileSet ;
//        if(preferenceManager.getProfiles() == null){
//            profileSet = new HashSet<>();
//        }else {
//            profileSet = preferenceManager.getProfiles();
//        }
//        profileSet.add(sb.toString());
//        preferenceManager.setProfiles(profileSet);
//        OttoBus.getBus().post(new NsdSocketSendMessage(sb.toString()));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OttoBus.getBus().unregister(this);
    }
}
