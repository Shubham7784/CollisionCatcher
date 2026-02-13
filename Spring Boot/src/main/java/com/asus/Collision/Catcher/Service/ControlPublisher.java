package com.asus.Collision.Catcher.Service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class ControlPublisher {

    @Autowired
    private MessageChannel mqttOutboundChannel;

    public void sendControl(String hardwareId, boolean stream, int interval){
        String topic = "vehicle/"+hardwareId+"/control";

        String payload = String.format(
                "{\"stream\":%s,\"interval\":%d}",
                stream,interval
        );

        mqttOutboundChannel.send(
                MessageBuilder.withPayload(payload)
                        .setHeader("mqtt_topic",topic)
                        .build()
        );
    }

}
