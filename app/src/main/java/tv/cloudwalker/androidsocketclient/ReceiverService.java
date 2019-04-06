package tv.cloudwalker.androidsocketclient;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.squareup.otto.Subscribe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import Utils.OttoBus;
import model.RabbitSendMessage;

public class ReceiverService extends Service {

    private static final String TAG = "ReceiverService";

    private ArrayList<String> routingKeyString;
    private Thread subscribeThread;
    private ConnectionFactory factory;
    private String imei, tvRoutingKey;

    public ReceiverService() {
    }


    public class RabbitBinder extends Binder
    {
        public ReceiverService getService(){
            return ReceiverService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new RabbitBinder();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand called");
        routingKeyString = new ArrayList<>();
        //creating routingkey
        imei = intent.getStringExtra("imei");
        tvRoutingKey = intent.getStringExtra("rabbit");
        imei = imei.replaceAll(":", "");
        StringBuilder sb = new StringBuilder("cw.kids.app");
        routingKeyString.add(sb.toString());
        sb.append(".");
        sb.append(imei);
        routingKeyString.add(sb.toString());
        OttoBus.getBus().register(this);
        if (factory == null) {
            setupConnectionFactory();
            setupSubscription();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy called");
        OttoBus.getBus().unregister(this);
        subscribeThread.interrupt();
        super.onDestroy();
    }

    private void setupConnectionFactory() {
        factory = new ConnectionFactory();
        factory.setHost(getResources().getString(R.string.rabbit_host));
        factory.setPort(Integer.valueOf(getString(R.string.rabbit_port)));
        factory.setVirtualHost(getString(R.string.rabbit_vHost));
        factory.setUsername(getString(R.string.rabbit_userName));
        factory.setPassword(getString(R.string.rabbit_password));
    }

    private void setupSubscription() {
        if (subscribeThread != null) {
            subscribeThread.interrupt();
            subscribeThread.start();
            return;
        }

        subscribeThread = new Thread(new Runnable(){
            @Override
            public void run() {
                while(true) {
                    try {
                        Connection connection = factory.newConnection();
                        Channel channel = connection.createChannel();
                        channel.exchangeDeclare(getString(R.string.rabbit_exchange), "topic", true);
                        Map<String, Object> queueArguments = new HashMap<>();
                        queueArguments.put("x-message-ttl", 60000);
                        channel.queueDeclare(imei, true, false, false, queueArguments);
                        // bind queue to channels
                        for(String routingKey : routingKeyString) {
                            channel.queueBind(imei, getString(R.string.rabbit_exchange), routingKey);
                        }
//                        QueueingConsumer consumer = new QueueingConsumer(channel);
//                        channel.basicConsume(imei, true, consumer);
//
//                        while(true) {
//                            try {
//                                QueueingConsumer.Delivery delivery = consumer.nextDelivery();
//                                String message = new String(delivery.getBody());
//                                Log.d(TAG, "rabbitMessage: "+ message);
//                                OttoBus.getBus().post(new RabbitReceiveMessage(message));
//                            } catch(InterruptedException ie) {
//                                ie.printStackTrace();
//                                return;
//                            }
//                        }
                    } catch(Exception e) {
                        if (e.getClass().equals(InterruptedException.class)) {
                            Log.e(TAG, "thread interrupted");
                            break;
                        }
                        Log.e(TAG, "connection broke");
                        e.printStackTrace();

                        try {
                            Thread.sleep(4000);
                        } catch (InterruptedException e1) {
                            break;
                        }
                    }
                }
            }
        });

        subscribeThread.start();
    }


    @Subscribe
    public void getMessageSentToRabbit(final RabbitSendMessage rabbitSendMessage){
        if(factory != null){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try
                    {
                        Log.d(TAG, "run: pusblishRabbitMEssage in try ");
                        Connection connection = factory.newConnection();
                        Log.d(TAG, "run: pusblishRabbitMEssage in connectiion created ");
                        Channel channel = connection.createChannel();
                        channel.basicPublish("kidsTopic", tvRoutingKey, null, rabbitSendMessage.getRabbitMessage().getBytes("UTF-8"));
                        Log.d(TAG, "run: pusblishRabbitMEssage  published ");
                        channel.close();
                        connection.close();
                        Log.d(TAG, "run: pusblishRabbitMEssage closed ");
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (TimeoutException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}
