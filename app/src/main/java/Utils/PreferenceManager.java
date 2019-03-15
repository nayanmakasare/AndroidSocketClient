package Utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

/**
 * Created by cognoscis on 9/3/18.
 */

public class PreferenceManager {

    private static final String PREF_NAME = "cloudwalker_launcher";



    private static final String USER_NAME = "userName";
    private static final String USER_EMAIL = "userEmail";
    private static final String GOOGLE_ID = "googleId";



    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;
    /**
     * Private mode of shared preferences.
     */
    private int PRIVATE_MODE = 0;

    /**
     * Instantiates a new Preference manager.
     *
     * @param ctx the context of the application
     */
    public PreferenceManager(Context ctx) {
        mPreferences = ctx.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        mEditor = mPreferences.edit();
    }

    public String getUserName() {
        return mPreferences.getString(USER_NAME, null);
    }

    public void setUserName(String userName) {
        mEditor.putString(USER_NAME, userName);
        mEditor.commit();
    }


    public String getUserEmail() {
        return mPreferences.getString(USER_EMAIL, null);
    }

    public void setUserEmail(String userEmail) {
        mEditor.putString(USER_EMAIL, userEmail);
        mEditor.commit();
    }

    public String getGoogleId() {
        return mPreferences.getString(GOOGLE_ID, null);
    }

    public void setGoogleId(String googleId) {
        mEditor.putString(GOOGLE_ID, googleId);
        mEditor.commit();
    }
}
