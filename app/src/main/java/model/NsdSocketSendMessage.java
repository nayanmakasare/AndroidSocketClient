package model;

public class NsdSocketSendMessage
{
    public NsdSocketSendMessage(String sendSocketMessage)
    {
        this.sendSocketMessage = sendSocketMessage;
    }

    public String getSendSocketMessage() {
        return sendSocketMessage;
    }

    public void setSendSocketMessage(String sendSocketMessage) {
        this.sendSocketMessage = sendSocketMessage;
    }

    String sendSocketMessage;
}
