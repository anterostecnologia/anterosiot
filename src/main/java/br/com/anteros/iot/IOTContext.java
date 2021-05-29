package br.com.anteros.iot;

import javax.json.JsonObject;

public class IOTContext {
    private JsonObject receivedPayload;
    private Thing thing;
    private StatusListener listener;

    private IOTContext() {
    }

    private IOTContext(JsonObject receivedPayload, Thing thing, StatusListener listener) {
        this.receivedPayload = receivedPayload;
        this.thing = thing;
        this.listener = listener;
    }

    public JsonObject getReceivedPayload() {
        return receivedPayload;
    }

    public void setReceivedPayload(JsonObject receivedPayload) {
        this.receivedPayload = receivedPayload;
    }

    public Thing getThing() {
        return thing;
    }

    public void setThing(Thing thing) {
        this.thing = thing;
    }

    public StatusListener getListener() {
        return listener;
    }

    public void setListener(StatusListener listener) {
        this.listener = listener;
    }

    public static IOTContext of(JsonObject receivedPayload, Thing thing, StatusListener listener) {
        return new IOTContext(receivedPayload, thing, listener);
    }
}
