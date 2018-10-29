package br.com.anteros.iot.protocol.modbus;

public class ModbusCommEvent {

	private int status;
    private int eventCount;
    private int messageCount;
    private int[] events;

    public ModbusCommEvent() {
        this.status = 0;
        this.eventCount = 0;
        this.messageCount = 0;
        this.events = null;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getEventCount() {
        return this.eventCount;
    }

    public void setEventCount(int eventCount) {
        this.eventCount = eventCount;
    }

    public int getMessageCount() {
        return this.messageCount;
    }

    public void setMessageCount(int messageCount) {
        this.messageCount = messageCount;
    }

    public int[] getEvents() {
        return this.events;
    }

    public void setEvents(int[] events) {
        this.events = events;
    }
}
