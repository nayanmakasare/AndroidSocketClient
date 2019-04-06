package model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by cognoscis on 14/4/18.
 */

public class UserProfileBody {
    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("dob")
    @Expose
    private String dob;

    @SerializedName("gender")
    @Expose
    private String gender;

    @SerializedName("mobilenumber")
    @Expose
    private String mobilenumber;

    @SerializedName("genres")
    @Expose
    private ArrayList<String> genres;

    @SerializedName("languages")
    @Expose
    private ArrayList<String> languages;

    @SerializedName("content_type")
    @Expose
    private ArrayList<String> content_type;

    public UserProfileBody() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMobilenumber() {
        return mobilenumber;
    }

    public void setMobilenumber(String mobilenumber) {
        this.mobilenumber = mobilenumber;
    }

    public ArrayList<String> getGenres() {
        return genres;
    }

    public void setGenres(ArrayList<String> genres) {
        this.genres = genres;
    }

    public ArrayList<String> getLanguages() {
        return languages;
    }

    public void setLanguages(ArrayList<String> languages) {
        this.languages = languages;
    }

    public ArrayList<String> getContent_type() {
        return content_type;
    }

    public void setContent_type(ArrayList<String> content_type) {
        this.content_type = content_type;
    }
}
