package tv.cloudwalker.androidsocketclient;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import Utils.MultiSelectSpinner;
import Utils.PreferenceManager;
import Utils.appUtils;
import api.MyProfileInterface;
import newDeviceModel.NewUserProfile;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CloudwalkerPreferenceActivity extends Activity {

    MultiSelectSpinner generSpinner, languageSpinner, contentSpinner;
    JSONObject profileConfigObj;
    public static final String TAG = CloudwalkerPreferenceActivity.class.getSimpleName();
    private AwesomeValidation awesomeValidation;
    private PreferenceManager preferenceManager;
    private DatePickerDialog.OnDateSetListener mDateSetListener ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloudwalker_preference);
        preferenceManager = new PreferenceManager(CloudwalkerPreferenceActivity.this);
        if (preferenceManager.isGoogleSignIn() && preferenceManager.isCloudwalkerSigIn()) {
            startActivity(new Intent(CloudwalkerPreferenceActivity.this, PrimeActivity.class));
            onBackPressed();
        } else {
            awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
            generSpinner = (MultiSelectSpinner) findViewById(R.id.mySpinner1);
            languageSpinner = (MultiSelectSpinner) findViewById(R.id.mySpinner2);
            contentSpinner = (MultiSelectSpinner) findViewById(R.id.mySpinner3);
            try {
                profileConfigObj = new JSONObject(appUtils.loadJSONFromAsset(this));
                generSpinner.setItems(getArrayListFromJSON(profileConfigObj.getJSONArray("genres")));
                languageSpinner.setItems(getArrayListFromJSON(profileConfigObj.getJSONArray("languages")));
                contentSpinner.setItems(getArrayListFromJSON(profileConfigObj.getJSONArray("content_type")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //adding validation to edittexts
            awesomeValidation.addValidation(this, R.id.userMobile, "^[+]?[0-9]{10,13}$", R.string.phone_error_message);
        }

        ((EditText)findViewById(R.id.userDob)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        CloudwalkerPreferenceActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = day  + "/" + month + "/" + year;
                ((EditText)findViewById(R.id.userDob)).setText(date);
            }
        };
    }


    public void goToMain(View view) {

        if (awesomeValidation.validate()) {
            Toast.makeText(this, "Validation Successfull", Toast.LENGTH_LONG).show();
            //process the data further
            if(generSpinner.getSelectedStrings().size() > 0 && languageSpinner.getSelectedStrings().size() > 0 && contentSpinner.getSelectedStrings().size() > 0)
            {
                Log.d(TAG, "goToMain: in if");
                int selectedRadioButtonId = ((RadioGroup)findViewById(R.id.radioSex)).getCheckedRadioButtonId();
                preferenceManager.setDob(((EditText)findViewById(R.id.userDob)).getText().toString());
                preferenceManager.setLanguage(languageSpinner.getSelectedItemsAsString());
                preferenceManager.setGenre((generSpinner.getSelectedItemsAsString()));
                preferenceManager.setMobileNumber(((EditText)findViewById(R.id.userMobile)).getText().toString());
                preferenceManager.setType(contentSpinner.getSelectedItemsAsString());
                preferenceManager.setGender(((RadioButton)findViewById (selectedRadioButtonId)).getText().toString());

                NewUserProfile userProfile = new NewUserProfile();
                userProfile.setCwId(preferenceManager.getGoogleId());
                userProfile.setDob(preferenceManager.getDob());
                userProfile.setEmail(preferenceManager.getUserEmail());
                userProfile.setGenre(generSpinner.getSelectedStrings());
                userProfile.setLaunguage(languageSpinner.getSelectedStrings());
                userProfile.setContentType(contentSpinner.getSelectedStrings());
                userProfile.setGender(preferenceManager.getGender());
                userProfile.setMobileNumber(preferenceManager.getMobileNumber());
                userProfile.setUserName(preferenceManager.getUserName());
                Log.d(TAG, "goToMain: created user object "+preferenceManager.getGoogleId());


                new Retrofit.Builder()
                        .baseUrl("http://192.168.1.143:4000/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build().create(MyProfileInterface.class).postUserProfile(userProfile).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        Log.d(TAG, "onResponse: "+response.code());
                        if(response.code() == 200) {
                            preferenceManager.setIsCloudwalkerSignIn(true);
                            startActivity(new Intent(view.getContext(), PrimeActivity.class));
                            onBackPressed();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.d(TAG, "onFailure: "+t);
                    }
                });
            }
        }
    }
    private ArrayList<String> getArrayListFromJSON(JSONArray jsonArray) {
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                arrayList.add(jsonArray.getString(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return arrayList;
    }
}
