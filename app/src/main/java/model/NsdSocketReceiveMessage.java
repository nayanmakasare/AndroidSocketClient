package model;

public class NsdSocketReceiveMessage
{

    public NsdSocketReceiveMessage(String socketMessage){
        this.socketMessage = socketMessage;
    }

    public String getSocketMessage() {
        return socketMessage;
    }

    public void setSocketMessage(String socketMessage) {
        this.socketMessage = socketMessage;
    }

    private String socketMessage ;

}
