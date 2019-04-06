package services;

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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeoutException;

import Utils.OttoBus;
import Utils.PreferenceManager;
import model.RabbitSendMessage;
import tv.cloudwalker.androidsocketclient.R;

public class CloudwalkerRabbitService extends Service {

    private static final String TAG = "CloudwalkerRabbit";

    private Thread subscribeThread;
    private ConnectionFactory factory;

    public CloudwalkerRabbitService() {
    }


    public class RabbitBinder extends Binder
    {
        public CloudwalkerRabbitService getService(){
            return CloudwalkerRabbitService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind: ");
        return new RabbitBinder();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand called");
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
        subscribeThread.interrupt();
        OttoBus.getBus().unregister(this);
        super.onDestroy();
    }

    private void setupConnectionFactory() {
        Log.d(TAG, "setupConnectionFactory: ");
        factory = new ConnectionFactory();
        factory.setHost(getResources().getString(R.string.rabbit_host));
        factory.setPort(Integer.valueOf(getString(R.string.rabbit_port)));
        factory.setVirtualHost(getString(R.string.rabbit_vHost));
        factory.setUsername(getString(R.string.rabbit_userName));
        factory.setPassword(getString(R.string.rabbit_password));
    }

    private void setupSubscription() {
        Log.d(TAG, "setupSubscription: ");
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
                        Log.d(TAG, "run: new ConnectionCreated");
                        new PreferenceManager(CloudwalkerRabbitService.this).setRabbitServiceOn(true);
                        Channel channel = connection.createChannel();
                        channel.exchangeDeclare(getString(R.string.rabbit_exchange), "topic", true);
                        Map<String, Object> queueArguments = new HashMap<>();
                        queueArguments.put("x-message-ttl", 60000);
                        PreferenceManager preferenceManager = new PreferenceManager(CloudwalkerRabbitService.this);
                        String queueName = preferenceManager.getGoogleId();
                        channel.queueDeclare(queueName, true, false, false, queueArguments);
                        Set<String> routingKeySet ;
                        //adding routing key to queue.
                        if(preferenceManager.getRoutingKey() == null){
                            Log.d(TAG, "run: preparing routingkey ");
                            routingKeySet = new HashSet<>();
                            StringBuilder sb = new StringBuilder("cw.app");
                            routingKeySet.add(sb.toString());
                            sb.append(".");
                            sb.append(preferenceManager.getGoogleId());
                            routingKeySet.add(sb.toString());
                            preferenceManager.setRoutingKey(routingKeySet);
                        }else {
                            routingKeySet = preferenceManager.getRoutingKey();
                            Log.d(TAG, "run: routingKey "+routingKeySet.toString());
                        }
                        for(String routingkey : routingKeySet){
                            channel.queueBind(queueName, getString(R.string.rabbit_exchange), routingkey);
                        }
//                        // bind queue to channels
//                        QueueingConsumer consumer = new QueueingConsumer(channel);
//                        channel.basicConsume(queueName, true, consumer);
//
//                        while(true) {
//                            try {
//                                QueueingConsumer.Delivery delivery = consumer.nextDelivery();
//                                String message = new String(delivery.getBody());
//                                OttoBus.getBus().post(new RabbitReceiveMessage(message));
//                                Log.d(TAG, "rabbitMessage: "+message);
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
                        channel.basicPublish("kidsTopic", rabbitSendMessage.getDestinationRoutingKey(), null, rabbitSendMessage.getRabbitMessage().getBytes("UTF-8"));
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
