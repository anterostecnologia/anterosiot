package br.com.anteros.iot.support;

import br.com.anteros.client.mqttv3.*;
import br.com.anteros.core.log.Logger;
import br.com.anteros.core.log.LoggerProvider;
import com.diozero.util.SleepUtil;

import java.net.InetAddress;
import java.util.concurrent.atomic.AtomicBoolean;

public class Pinger implements Runnable, MqttCallback, MqttCallbackExtended {

    private String thingId;
    private String deviceType;
    private String ipAddress;
    private int timeOut;
    private int interval;
    private String mqttServerAddress;
    private int mqttServerPort;
    private String mqttId;
    private String mqttUserName;
    private String mqttPassword;

    private AnterosMqttClient clientMqtt;

    private AtomicBoolean running = new AtomicBoolean(true);
    private boolean alreadyConnectedOnce = false;

    private String payload = "alive";

    private static final Logger LOG = LoggerProvider.getInstance().getLogger(Pinger.class.getName());

    public Pinger(String deviceType, String thingId, String ipAddress, int timeOut, int interval, String mqttServerAddress, int mqttServerPort, String mqttId, String mqttUserName, String mqttPassword) {
        this.thingId = thingId;
        this.deviceType = deviceType;
        this.ipAddress = ipAddress;
        this.timeOut = timeOut;
        this.interval = interval;
        this.mqttServerAddress = mqttServerAddress;
        this.mqttServerPort = mqttServerPort;
        this.mqttId = mqttId;
        this.mqttUserName = mqttUserName;
        this.mqttPassword = mqttPassword;
    }

    @Override
    public void run() {
        try {
            clientMqtt = MqttHelper.createMqttClient("tcp://" + mqttServerAddress + ":" + mqttServerPort, mqttId, mqttUserName, mqttPassword, true,
                    true, this);
            clientMqtt.connect();
        } catch (Exception e) {
            clientMqtt = null;
        }
        while(running.get()){
            try {
                InetAddress address = InetAddress.getByName(ipAddress);
                if (address.isReachable(timeOut)) {
                    try{
                        if(clientMqtt.isConnected()){
                            MqttMessage message = new MqttMessage(payload.getBytes());
                            MqttHelper.publishHeartBeat(thingId, deviceType, "alive", true, ipAddress, clientMqtt);
                            LOG.info("publicado heart beat da coisa " + thingId);
                        }
                    } catch (Exception e){
                    }
                } else {
                    LOG.info("PING TIMEOUT. IP: " + ipAddress + " THING: " + thingId);
                }
            } catch (Exception e){
            }
            SleepUtil.sleepMillis(interval);
        }
    }

    @Override
    public void connectComplete(boolean reconnect, String serverURI) {
        LOG.info("Pinger " + ipAddress + (reconnect ? "RECONECTOU" : "CONECTOU") + " ao broker interno");
        if (!alreadyConnectedOnce) {
            alreadyConnectedOnce = true;
        }
    }

    @Override
    public void connectionLost(Throwable cause) {
        LOG.info("Pinger " + ipAddress + " PERDEU conexão com broker interno");
        try {
            this.clientMqtt.reconnect();
        } catch (MqttException e) {
            if (e.getReasonCode() == MqttException.REASON_CODE_CONNECT_IN_PROGRESS || e.getReasonCode() == MqttException.REASON_CODE_CLIENT_DISCONNECTING) {
                SleepUtil.sleepMillis(2000);
            } else {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {

    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }
}
