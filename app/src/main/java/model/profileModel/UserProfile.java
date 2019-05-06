package model.profileModel;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserProfile {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("userName")
    @Expose
    private String userName;
    @SerializedName("dob")
    @Expose
    private String dob;
    @SerializedName("mobileNumber")
    @Expose
    private String mobileNumber;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("genre")
    @Expose
    private List<String> genre = null;
    @SerializedName("launguage")
    @Expose
    private List<String> launguage = null;
    @SerializedName("contentType")
    @Expose
    private List<String> contentType = null;
    @SerializedName("linkedDevices")
    @Expose
    private List<LinkedDevice> linkedDevices = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getGenre() {
        return genre;
    }

    public void setGenre(List<String> genre) {
        this.genre = genre;
    }

    public List<String> getLaunguage() {
        return launguage;
    }

    public void setLaunguage(List<String> launguage) {
        this.launguage = launguage;
    }

    public List<String> getContentType() {
        return contentType;
    }

    public void setContentType(List<String> contentType) {
        this.contentType = contentType;
    }

    public List<LinkedDevice> getLinkedDevices() {
        return linkedDevices;
    }

    public void setLinkedDevices(List<LinkedDevice> linkedDevices) {
        this.linkedDevices = linkedDevices;
    }

}
