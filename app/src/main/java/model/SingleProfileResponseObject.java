package model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by cognoscis on 19/4/18.
 */

public class SingleProfileResponseObject {
    @SerializedName("name")
    private String name;
    @SerializedName("dob")
    private String dob;
    @SerializedName("gender")
    private String gender;
    @SerializedName("mobilenumber")
    private String mobileNumber;
    @SerializedName("genres")
    private ArrayList<String> genres;
    @SerializedName("languages")
    private ArrayList<String> languages;
    @SerializedName("content_type")
    private ArrayList<String> contentType;
    @SerializedName("favourites")
    private ArrayList<String> favourites;

    public SingleProfileResponseObject(String name, String dob, String gender, String mobileNumber,
                                       ArrayList<String> genres, ArrayList<String> languages,
                                       ArrayList<String> contentType, ArrayList<String> favourites) {
        this.name = name;
        this.dob = dob;
        this.gender = gender;
        this.mobileNumber = mobileNumber;
        this.genres = genres;
        this.languages = languages;
        this.contentType = contentType;
        this.favourites = favourites;
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

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
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

    public ArrayList<String> getContentType() {
        return contentType;
    }

    public void setContentType(ArrayList<String> contentType) {
        this.contentType = contentType;
    }

    public ArrayList<String> getFavourites() {
        return favourites;
    }

    public void setFavourites(ArrayList<String> favourites) {
        this.favourites = favourites;
    }
}
