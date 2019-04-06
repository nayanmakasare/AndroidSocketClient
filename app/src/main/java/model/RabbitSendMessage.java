package model;

public class RabbitSendMessage {

    public RabbitSendMessage(String rabbitMessage, String destinationRoutingKey){
        this.rabbitMessage = rabbitMessage;
        this.destinationRoutingKey = destinationRoutingKey;
    }

    public String getRabbitMessage() {
        return rabbitMessage;
    }

    public void setRabbitMessage(String rabbitMessage) {
        this.rabbitMessage = rabbitMessage;
    }

    private String rabbitMessage ;

    public String getDestinationRoutingKey() {
        return destinationRoutingKey;
    }

    public void setDestinationRoutingKey(String destinationRoutingKey) {
        this.destinationRoutingKey = destinationRoutingKey;
    }

    private String destinationRoutingKey ;
}
