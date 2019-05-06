package newDeviceModel;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NewUserProfile {

    @SerializedName("cwId")
    @Expose
    private String cwId;
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
    private List<TvInfo> linkedDevices = null;

    public String getCwId() {
        return cwId;
    }

    public void setCwId(String cwId) {
        this.cwId = cwId;
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

    public List<TvInfo> getLinkedDevices() {
        return linkedDevices;
    }

    public void setLinkedDevices(List<TvInfo> linkedDevices) {
        this.linkedDevices = linkedDevices;
    }

}