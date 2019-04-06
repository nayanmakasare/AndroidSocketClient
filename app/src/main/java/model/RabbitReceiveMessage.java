package model;

public class RabbitReceiveMessage {

    public RabbitReceiveMessage(String rabbitMessage){
        this.rabbitMessage = rabbitMessage;
    }

    public String getRabbitMessage() {
        return rabbitMessage;
    }

    public void setRabbitMessage(String rabbitMessage) {
        this.rabbitMessage = rabbitMessage;
    }

    private String rabbitMessage ;
}
