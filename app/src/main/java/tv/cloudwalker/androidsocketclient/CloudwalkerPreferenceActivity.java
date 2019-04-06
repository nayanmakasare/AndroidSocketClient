package tv.cloudwalker.androidsocketclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Utils.MultiSelectSpinner;
import Utils.PreferenceManager;
import Utils.appUtils;

public class CloudwalkerPreferenceActivity extends Activity {

    MultiSelectSpinner spinner1, spinner2, spinner3;
    JSONObject profileConfigObj;
    public static final String TAG = CloudwalkerPreferenceActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloudwalker_preference);
        spinner1 = (MultiSelectSpinner) findViewById(R.id.mySpinner1);
        spinner2 = (MultiSelectSpinner) findViewById(R.id.mySpinner2);
        spinner3 = (MultiSelectSpinner) findViewById(R.id.mySpinner3);
        try {
            profileConfigObj = new JSONObject(appUtils.loadJSONFromAsset(this));
            spinner1.setItems(getArrayListFromJSON(profileConfigObj.getJSONArray("genres")));
            spinner2.setItems(getArrayListFromJSON(profileConfigObj.getJSONArray("languages")));
            spinner3.setItems(getArrayListFromJSON(profileConfigObj.getJSONArray("content_type")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void goToMain(View view) {
        PreferenceManager preferenceManager = new PreferenceManager(view.getContext());
        preferenceManager.setDob(((EditText)findViewById(R.id.userDob)).getText().toString());
        preferenceManager.setLanguage(spinner2.getSelectedItemsAsString());
        preferenceManager.setGenre((spinner1.getSelectedItemsAsString()));
        preferenceManager.setMobileNumber(((EditText)findViewById(R.id.userMobile)).getText().toString());
        preferenceManager.setType(spinner3.getSelectedItemsAsString());
        preferenceManager.setGender(((EditText)findViewById(R.id.userGender)).getText().toString());
        preferenceManager.setIsCloudwalkerSignIn(true);
        startActivity(new Intent(view.getContext(), NoNetworkActivity.class));
        onBackPressed();

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
