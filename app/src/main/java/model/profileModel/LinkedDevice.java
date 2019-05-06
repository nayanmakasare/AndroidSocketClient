package model.profileModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LinkedDevice {

    @SerializedName("emac")
    @Expose
    private String emac;
    @SerializedName("boardName")
    @Expose
    private String boardName;
    @SerializedName("panelName")
    @Expose
    private String panelName;

    public String getEmac() {
        return emac;
    }

    public void setEmac(String emac) {
        this.emac = emac;
    }

    public String getBoardName() {
        return boardName;
    }

    public void setBoardName(String boardName) {
        this.boardName = boardName;
    }

    public String getPanelName() {
        return panelName;
    }

    public void setPanelName(String panelName) {
        this.panelName = panelName;
    }

}
