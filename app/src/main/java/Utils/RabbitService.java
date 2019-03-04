package Utils;

import android.app.Service;

public class RabbitService  {
//    public static final String TAG = RabbitService.class.getSimpleName();
//    private ArrayList<String> routingKeyString = new ArrayList<>();
//    private ConnectionFactory factory;
//    private Thread subscribeThread;
//    private Connection connection;
//    private Channel mChannel;
//
//    public RabbitService() {
//
//    }
//
//
//    @Nullable
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
//
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        //creating routingkey
//        OttoBus.getBus().register(this);
//        String boardName = Utils.getSystemProperty(Utils.mBordname);
//        String ethMac = getEthMacAddress();
//        if (ethMac != null) {
//            ethMac = ethMac.replaceAll(":", "");
//        }
//        String tvSize = getTvScreenSize();
//
//        //TODO need to get Tv Screen infromation from api calls
//        if (boardName != null && ethMac != null && tvSize != null && TextUtils.isDigitsOnly(tvSize) ) {
//            StringBuilder sb = new StringBuilder("cw.tv");
//            //TV
//            routingKeyString.add(sb.toString());
//            sb.append(".");
//            if (boardName.contains("5510")) {
//                sb.append("5510");
//            } else if (boardName.contains("638")) {
//                sb.append("638");
//            } else if (boardName.contains("338")) {
//                sb.append("338");
//            } else if (boardName.contains("538")) {
//                sb.append("538");
//            } else if (boardName.contains("553")) {
//                sb.append("553");
//            }
//            //TV.boardName
//            routingKeyString.add(sb.toString());
//            sb.append(".");
//            sb.append(tvSize);
//            //TV.boardName.tvSize
//            routingKeyString.add(sb.toString());
//            sb.append(".");
//            sb.append(ethMac);
//            //TV.boardName.tvSize.ethMac
//            routingKeyString.add(sb.toString());
//            //Tvsize
//            routingKeyString.add(sb.toString());
//        }
//
//    }
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//
//        if (factory == null  && routingKeyString.size() > 0) {
//            setupConnectionFactory();
//            setupSubscription();
//        }
//        return super.onStartCommand(intent, flags, startId);
//    }
//
//    private void setupConnectionFactory() {
//        factory = new ConnectionFactory();
//        factory.setHost(getResources().getString(R.string.rabbit_host));
//        factory.setPort(Integer.valueOf(getString(R.string.rabbit_port)));
//        factory.setVirtualHost(getString(R.string.rabbit_vHost));
//        factory.setUsername(getString(R.string.rabbit_userName));
//        factory.setPassword(getString(R.string.rabbit_password));
//    }
//
//    @Override
//    public void onDestroy() {
//        if(subscribeThread != null && connection != null && mChannel != null){
//            subscribeThread.interrupt();
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        if(connection.isOpen()){
//                            connection.close();
//                        }
//                        if(mChannel.isOpen()){
//                            mChannel.close();
//                        }
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    } catch (TimeoutException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }).start();
//        }
//        super.onDestroy();
//    }
//
//    private void setupSubscription() {
//        if (subscribeThread != null) {
//            subscribeThread.interrupt();
//            subscribeThread.start();
//            return;
//        }
//
//        subscribeThread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while (true) {
//                    try {
//                        connection = factory.newConnection();
//                        if(MainActivity.getInstance() != null){
//                            MainActivity.getInstance().setConnectedToRabbit(true);
//                        }
//                        mChannel = connection.createChannel();
//                        mChannel.exchangeDeclare(getString(R.string.rabbit_exchange), "topic", true);
//                        if (mChannel.isOpen()) {
//                            String ethmac = getEthMacAddress();
//                            if (ethmac == null) {
//                                return;
//                            }
//                            String mQueue = ethmac.replaceAll(":", "");
//                            Map<String, Object> queueArguments = new HashMap<>();
//                            queueArguments.put("x-message-ttl", 60000);
//                            mChannel.queueDeclare(mQueue, true, false, false, queueArguments);
//
//                            //adding routing key to queue.
//                            for (String baseRoutingKey : routingKeyString) {
//                                mChannel.queueBind(mQueue, getString(R.string.rabbit_exchange), baseRoutingKey);
//                            }
//
//                            connection.addShutdownListener(new ShutdownListener() {
//                                @Override
//                                public void shutdownCompleted(ShutdownSignalException cause) {
//                                    try {
//                                        if(connection.isOpen()){
//                                            connection.close();
//                                        }
//                                        if(mChannel.isOpen()){
//                                            mChannel.close();
//                                        }
//                                        setupSubscription();
//                                    } catch (IOException e) {
//                                        e.printStackTrace();
//                                    } catch (TimeoutException e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//                            });
//
//                            QueueingConsumer consumer = new QueueingConsumer(mChannel);
//                            mChannel.basicConsume(mQueue, true, consumer);
//
//                            while (true) {
//                                try {
//                                    rabbitMessage(consumer.nextDelivery().getBody());
//                                } catch (InterruptedException ie) {
//                                    ie.printStackTrace();
//                                    return;
//                                }
//                            }
//                        }
//                    } catch (Exception e) {
//                        if (e.getClass().equals(InterruptedException.class)) {
//                            break;
//                        }
//                        e.printStackTrace();
//
//                        try {
//                            Thread.sleep(4000);
//                        } catch (InterruptedException e1) {
//                            break;
//                        }
//                    }
//
//                }
//
//            }
//        });
//
//        subscribeThread.start();
//    }
//
//    private void rabbitMessage(byte[] message) {
//        try {
//            String letter = new String(message, "UTF-8");
//            if (letter.compareToIgnoreCase("refresh") == 0) {
//                OttoBus.getBus().post("refresh");
//            } else if (letter.compareToIgnoreCase("deviceInfo") == 0) {
//                Utils.postDeviceInfo(this);
//            } else if (letter.compareToIgnoreCase("ota") == 0) {
//                BackgroundInstallHelper backgroundInstallHelper = new BackgroundInstallHelper(this);
//                backgroundInstallHelper.checkForUpdates();
//            }
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//    }
}



















