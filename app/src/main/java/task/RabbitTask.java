package task;

import android.util.Log;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ExceptionHandler;
import com.rabbitmq.client.ShutdownSignalException;
import com.rabbitmq.client.TopologyRecoveryException;
import com.rabbitmq.client.impl.nio.NioParams;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class RabbitTask implements Consumer, ExceptionHandler
{
    private ConnectionFactory factory = new ConnectionFactory();
    public static final String TAG = RabbitTask.class.getSimpleName();

    public void connectingMq() {

//        Map<String , String> properties = new HashMap<>();
//        properties.put(ConnectionFactoryConfigurator.USERNAME,"blwmcjci");
//        properties.put(ConnectionFactoryConfigurator.PASSWORD, "vUyxgSpStc5IgHuSi4WFdVPVnxQ8qR57");
//        properties.put(ConnectionFactoryConfigurator.VIRTUAL_HOST, "blwmcjci");
//        properties.put(ConnectionFactoryConfigurator.HOST, "barnacle.rmq.cloudamqp.com");
//        properties.put(ConnectionFactoryConfigurator.PORT, "5672");
//        properties.put(ConnectionFactoryConfigurator.CONNECTION_RECOVERY_ENABLED , "true");
//        properties.put(ConnectionFactoryConfigurator.CONNECTION_RECOVERY_INTERVAL, "10000");
//        properties.put(ConnectionFactoryConfigurator.CONNECTION_TIMEOUT, "30000");
//        properties.put(ConnectionFactoryConfigurator.USE_NIO, "false");
//        ConnectionFactoryConfigurator.load(factory, properties);

        factory.setHost("barnacle.rmq.cloudamqp.com");
        factory.setPort(Integer.valueOf("5672"));
        factory.setVirtualHost("blwmcjci");
        factory.setUsername("blwmcjci");
        factory.setPassword("vUyxgSpStc5IgHuSi4WFdVPVnxQ8qR57");
        factory.setExceptionHandler(this);

        Connection connection = null ;
        try
        {
            connection = factory.newConnection();
            Channel channel = connection.createChannel();
            channel.exchangeDeclare("cwTopic", "topic", true);
            Map<String, Object> queueArguments = new HashMap<>();
            queueArguments.put("x-message-ttl", 60000);
            channel.queueDeclare("nayan", true, false, false, queueArguments);
            channel.queueBind("nayan", "cwTopic", "cloudwalker");
            channel.basicConsume("nayan", true, "cloudwalker", this);

        }
        catch (IOException e) {
            e.printStackTrace();
            if(connection != null){
                try {
                    connection.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleConsumeOk(String consumerTag) {
        Log.d(TAG, "handleConsumeOk: "+consumerTag);
    }

    @Override
    public void handleCancelOk(String consumerTag) {
        Log.d(TAG, "handleCancelOk: "+consumerTag);
    }

    @Override
    public void handleCancel(String consumerTag) throws IOException {
        Log.d(TAG, "handleCancel: "+consumerTag);
    }

    @Override
    public void handleShutdownSignal(String consumerTag, ShutdownSignalException sig) {
        Log.d(TAG, "handleShutdownSignal: "+consumerTag +" "+sig.getMessage());
    }

    @Override
    public void handleRecoverOk(String consumerTag) {
        Log.d(TAG, "handleRecoverOk: "+consumerTag);
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
        Log.d(TAG, "handleDelivery: "+consumerTag +" "+ new String(body));
    }

    @Override
    public void handleUnexpectedConnectionDriverException(Connection conn, Throwable exception) {
        Log.d(TAG, "handleUnexpectedConnectionDriverException: ");
    }

    @Override
    public void handleReturnListenerException(Channel channel, Throwable exception) {
        Log.d(TAG, "handleReturnListenerException: ");
    }

    @Override
    public void handleConfirmListenerException(Channel channel, Throwable exception) {
        Log.d(TAG, "handleConfirmListenerException: ");
    }

    @Override
    public void handleBlockedListenerException(Connection connection, Throwable exception) {
        Log.d(TAG, "handleBlockedListenerException: ");
    }

    @Override
    public void handleConsumerException(Channel channel, Throwable exception, Consumer consumer, String consumerTag, String methodName) {
        Log.d(TAG, "handleConsumerException: ");
    }

    @Override
    public void handleConnectionRecoveryException(Connection conn, Throwable exception) {
        Log.d(TAG, "handleConnectionRecoveryException: ");
    }

    @Override
    public void handleChannelRecoveryException(Channel ch, Throwable exception) {
        Log.d(TAG, "handleChannelRecoveryException: ");
    }

    @Override
    public void handleTopologyRecoveryException(Connection conn, Channel ch, TopologyRecoveryException exception) {
        Log.d(TAG, "handleTopologyRecoveryException: ");
    }
}
