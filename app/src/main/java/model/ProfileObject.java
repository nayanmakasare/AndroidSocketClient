package model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by cognoscis on 6/4/18.
 */

public class ProfileObject implements Parcelable {

    public static final Creator<ProfileObject> CREATOR = new Creator<ProfileObject>() {
        @Override
        public ProfileObject createFromParcel(Parcel source) {
            return new ProfileObject(source);
        }

        @Override
        public ProfileObject[] newArray(int size) {
            return new ProfileObject[size];
        }
    };

    private int id;
    private String name;
    private long dob;
    private String mobileNumber;
    private String gender;
    private String genres;
    private String languages;
    private String contentTypes;
    private String favourites;
    private boolean isDefault = false;
    private boolean isSelected = false;

    public ProfileObject() {
    }

    public ProfileObject(int id, String name, long dob, String mobileNumber, String gender, String genres,
                         String languages, String contentTypes, String favourites, boolean isDefault, boolean isSelected) {
        this.id = id;
        this.name = name;
        this.dob = dob;
        this.mobileNumber = mobileNumber;
        this.gender = gender;
        this.genres = genres;
        this.languages = languages;
        this.contentTypes = contentTypes;
        this.favourites = favourites;
        this.isDefault = isDefault;
        this.isSelected = isSelected;
    }

    protected ProfileObject(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.dob = in.readLong();
        this.mobileNumber = in.readString();
        this.gender = in.readString();
        this.genres = in.readString();
        this.languages = in.readString();
        this.contentTypes = in.readString();
        this.favourites = in.readString();
        this.isDefault = in.readByte() != 0;
        this.isSelected = in.readByte() != 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDob() {
        return dob;
    }

    public void setDob(long dob) {
        this.dob = dob;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public ArrayList<String> getGenres() {
        return getStringListFromCommaSeparatedString(genres);
    }

    public void setGenres(ArrayList<String> genres) {
        this.genres = getCommaSeparatedString(genres);
    }

    public String getGenresString() {
        return this.genres;
    }

    public void setGenresString(String genres) {
        this.genres = genres;
    }

    public ArrayList<String> getLanguages() {
        return getStringListFromCommaSeparatedString(languages);
    }

    public void setLanguages(ArrayList<String> languages) {
        this.languages = getCommaSeparatedString(languages);
    }

    public String getLanguagesString() {
        return languages;
    }

    public void setLanguagesString(String languages) {
        this.languages = languages;
    }

    public ArrayList<String> getContentTypes() {
        return getStringListFromCommaSeparatedString(contentTypes);
    }

    public void setContentTypes(ArrayList<String> contentTypes) {
        this.contentTypes = getCommaSeparatedString(contentTypes);
    }

    public String getContentTypesString() {
        return contentTypes;
    }

    public void setContentTypesString(String contentTypes) {
        this.contentTypes = contentTypes;
    }

    public ArrayList<String> getFavourites() {
        return getStringListFromCommaSeparatedString(favourites);
    }

    public void setFavourites(ArrayList<String> favourites) {
        this.favourites = getCommaSeparatedString(favourites);
    }

    public String getFavouritesString() {
        return favourites;
    }

    public void setFavouritesString(String favourites) {
        this.favourites = favourites;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    private String getCommaSeparatedString(ArrayList<String> itemList) {
        if (itemList == null) {
            return null;
        } else {
            String commaSeparatedString = "";
            for (String item : itemList) {
                commaSeparatedString += item + ",";
            }
            return commaSeparatedString.substring(0, commaSeparatedString.length() - 1);
        }
    }

    private ArrayList<String> getStringListFromCommaSeparatedString(String commaSeparatedString) {
        if (commaSeparatedString == null) {
            return null;
        } else {
            String[] itemArray = commaSeparatedString.split(",");
            return new ArrayList<>(Arrays.asList(itemArray));
        }
    }

    private JSONArray getJSONArrayFromCommaSeparatedString(String commaSeparatedString) {
        if (commaSeparatedString == null) {
            return null;
        } else {
            JSONArray jsonArray = new JSONArray();
            String[] itemArray = commaSeparatedString.split(",");
            for (int i = 0; i < itemArray.length; i++) {
                jsonArray.put(itemArray[i]);
            }
            return jsonArray;
        }
    }

    public UserProfileBody getPostObject() {
        UserProfileBody userProfileBody = new UserProfileBody();
        userProfileBody.setName(this.name);
        Date date = new Date(this.dob);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        userProfileBody.setDob(dateFormat.format(date));
        userProfileBody.setGender(this.gender);
        userProfileBody.setMobilenumber(this.mobileNumber);
        userProfileBody.setGenres(getStringListFromCommaSeparatedString(this.genres));
        userProfileBody.setLanguages(getStringListFromCommaSeparatedString(this.languages));
        userProfileBody.setContent_type(getStringListFromCommaSeparatedString(this.contentTypes));
        return userProfileBody;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeLong(dob);
        dest.writeString(mobileNumber);
        dest.writeString(gender);
        dest.writeString(genres);
        dest.writeString(languages);
        dest.writeString(contentTypes);
        dest.writeString(favourites);
        dest.writeByte((byte) (isDefault ? 1 : 0));
        dest.writeByte((byte) (isSelected ? 1 : 0));
    }
}
