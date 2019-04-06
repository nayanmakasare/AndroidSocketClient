package model.profile;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Profile {

    @SerializedName("users")
    @Expose
    private Users users;
    @SerializedName("tvinfo")
    @Expose
    private List<TvInfo> tvinfo = null;

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public List<TvInfo> getTvinfo() {
        return tvinfo;
    }

    public void setTvinfo(List<TvInfo> tvinfo) {
        this.tvinfo = tvinfo;
    }

}
