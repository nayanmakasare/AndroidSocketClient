package model.profile;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TvInfo {

    @SerializedName("emac")
    @Expose
    private String emac;
    @SerializedName("panel")
    @Expose
    private String panel;

    public String getEmac() {
        return emac;
    }

    public void setEmac(String emac) {
        this.emac = emac;
    }

    public String getPanel() {
        return panel;
    }

    public void setPanel(String panel) {
        this.panel = panel;
    }

}
