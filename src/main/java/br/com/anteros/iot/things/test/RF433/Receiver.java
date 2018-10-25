package br.com.anteros.iot.things.test.RF433;

import java.time.Instant;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections4.queue.CircularFifoQueue;

import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinEdge;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

import br.com.anteros.iot.things.test.rf.HighLow;
import br.com.anteros.iot.things.test.rf.Protocol;



/**
 * Receiver is a simple class to receive changing waveforms and store them for interpretation
 * Created by GJWood on 17/01/2017.
 */
public class Receiver implements GpioPinListenerDigital,Runnable
{
    private int pulseWidthTolerance; //percentage variation in pulse width allowed
    @SuppressWarnings("FieldCanBeLocal")
    private final int MIN_MESSAGE_SEPARATION_TIME = 4300; //minimum gap between rawMessages in microseconds
    @SuppressWarnings("FieldCanBeLocal")
    private final int MESSAGE_STORAGE_CAPACITY = 1000;
    private static final int MIN_MESSAGE_SIZE = 6; //two bits sync + four bits message
    private static final int MAX_MESSAGE_SIZE = 258;//66; // limit on long (64bits) => 32 bit * 2 H/L changes per bit + 2 for sync

    private final GpioPinDigitalInput receivePin;
    private RawMessage rawMessage;
    private volatile boolean newMessage;
    private volatile boolean interrupted;
    private volatile boolean finished;
    @SuppressWarnings("FieldCanBeLocal")
    private final Thread decoder;
    private final CircularFifoQueue <RawMessage> rawMessages;
    private final CircularFifoQueue<DecodedMessage> decodedMessages;

    private long lastTime;

    public Receiver(GpioPinDigitalInput receivePin)
    {
        this.receivePin = receivePin;
        this.pulseWidthTolerance = 30;
        this.lastTime = 0;
        this.rawMessage = new RawMessage(MAX_MESSAGE_SIZE);
        this.rawMessages = new CircularFifoQueue<>(MESSAGE_STORAGE_CAPACITY);
        this.decodedMessages = new CircularFifoQueue<>(MESSAGE_STORAGE_CAPACITY);
        this.newMessage = false;
        this.interrupted = false;
        this.finished = false;
        this.decoder = new Thread(this);
        decoder.start();
    }

    public int getPulseWidthTolerance() {return pulseWidthTolerance;}
    public boolean isFinished(){return finished;}
    public void setPulseWidthTolerance(int pulseWidthTolerance) {this.pulseWidthTolerance = pulseWidthTolerance;}

    /**
     * EnableReceive    -   plug in the interrupt handler
     *
     */
    public void enableReceive()
    {
        this.receivePin.addListener(this);
        System.out.println("Listener added");
    }

    /**
     * DisableReceive     -   unplug in the interrupt handler
     */
    public void disableReceive()
    {
        this.receivePin.removeListener(this);
        System.out.println("Listener removed");
        interrupted = true;
    }

    /**
     * handleGpioPinDigitalStateChangeEvent -   interrupt handler
     *                                          records pin event & time since last event
     * @param pinEvent  the event that caused the interrupt
     */
    @Override
    public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent pinEvent)
    {
        final long time = System.nanoTime()/1000; //micros();
        final int duration = (int)(time - lastTime);

        if (duration > MIN_MESSAGE_SEPARATION_TIME)
        {
            // A long stretch without signal level change occurred. This could
            // be the gap between two transmissions. store events as a message
            if (rawMessage.events.size()>= MIN_MESSAGE_SIZE)
            {
                rawMessages.add(rawMessage); // save only reasonable length rawMessages
                newMessage =true;
            }
            rawMessage= new RawMessage(MAX_MESSAGE_SIZE);
        } else
        {
            if (rawMessage.events.size() >= MAX_MESSAGE_SIZE)
            {
                rawMessage.events.clear(); //throw fragment away
            }
        }
        rawMessage.events.add( new RF433Event(pinEvent,duration)); // save the current event
        lastTime = time;
    }

    /**
     * processMessage   -   Attempt to decode the message
     * @param msg       -   a raw message
     */
    private boolean processMessage(RawMessage msg)
    {
    	System.out.println("Raw message: "+msg);
        boolean matched = false;
        for(Protocol pr: Protocol.values())
        {
            matched = processMsgWithProtocol(pr, msg);
            if (matched){break;}
        }
        return matched;
    }

    /**
     * processMsgWithProtocol  -   Attempt to decode a raw messages using the specified protocol
     * @param protocol          -   A specification of sync and bit encoding timings
     * @param rawMessage               -   the raw message
     * @return                  -   true if decoded successfully, decoded message added to store
     */
    private boolean processMsgWithProtocol(Protocol protocol, RawMessage rawMessage)
    {
        if (rawMessage == null) return false;
        if (rawMessage.events.size() < MIN_MESSAGE_SIZE) return false; // ignore very short transmissions: no device sends them, so this must be noise
        //Assuming the longer pulse length is the pulse captured in timings[0]
        final int syncLengthInPulses =  ((protocol.syncFactor.low) > (protocol.syncFactor.high)) ? (protocol.syncFactor.low) : (protocol.syncFactor.high);
        final int pulseWidth = rawMessage.events.get(0).duration / syncLengthInPulses;
        final int pulseWidthTolerance = pulseWidth * this.pulseWidthTolerance / 100;

        /* For protocols that start low, the sync period looks like
         *               _________
         * _____________|         |??????????|
         *
         * |--1st dur--|-2nd dur-|-Start data-|
         *
         * The 3rd saved duration starts the data.
         *
         * For protocols that start high, the sync period looks like
         *
         *  ______________
         * |              |____________|?????????|
         *
         * |-filtered out-|--1st dur--|--Start data--|
         *
         * The 2nd saved duration starts the data
         */
        final int firstDataTiming = (protocol.invertedSignal) ? (2) : (1);
        DecodedMessage dMsg = new DecodedMessage(protocol.name(),pulseWidth,rawMessage.getReceivedTime());
        for (int i = firstDataTiming; i < rawMessage.events.size() - 1; i += 2)
        {
            int bit1Dur = rawMessage.events.get(i).duration;
            int bit2Dur = rawMessage.events.get(i+1).duration;
            //check each pair of bits matches the protocol definition for
            if (Math.abs( bit1Dur - pulseWidth * protocol.zero.high) < pulseWidthTolerance &&
                    Math.abs(bit2Dur - pulseWidth * protocol.zero.low) < pulseWidthTolerance)
            {
                // matched bit 0
                dMsg.addBit(false);
            } else if (Math.abs(bit1Dur - pulseWidth * protocol.one.high) < pulseWidthTolerance &&
                    Math.abs(bit2Dur - pulseWidth * protocol.one.low) < pulseWidthTolerance)
            {
                // matched bit 1
                dMsg.addBit(true);
            } else
            {
                return false; // Failed, out of spec bit pair cannot be translated
            }
        }
        if (dMsg.size() >= MIN_MESSAGE_SIZE) {
            decodedMessages.add(dMsg);
            return true;
        }
        return false;
    }

    private boolean analyseMsg(RawMessage msg)
    {
        if (rawMessage == null) return false;
        if (rawMessage.events.size() < MIN_MESSAGE_SIZE) return false; // ignore very short transmissions: no device sends them, so this must be noise
        //Assuming the longer pulse length is the pulse captured in timings[0]
        int s0Duration = msg.events.get(0).duration;
        int s1Duration =  msg.events.get(1).duration;
        int pulseWidth;
        int syncLengthInPulses;
        int pulseWidthTolerance;
        if (s0Duration>= s1Duration)
        {
            pulseWidth = s1Duration;
            syncLengthInPulses = s0Duration/pulseWidth;
        }else
        {
            pulseWidth = s0Duration;
            syncLengthInPulses = s1Duration/pulseWidth;
        }
        int firstDataTiming = (msg.events.get(0).gpioEvent.getState() == PinState.getState(true) ) ? (2) : (1);
        System.out.format("Pulse width: %d, Sync Length %d, First data %d, ",pulseWidth,syncLengthInPulses, firstDataTiming);
        pulseWidthTolerance = pulseWidth * this.pulseWidthTolerance / 100;
        // check if we have two plausible bits
        int bit1Dur = rawMessage.events.get(firstDataTiming).duration;
        int bit2Dur = rawMessage.events.get(firstDataTiming+1).duration;
        System.out.println("bit1Dur = " + bit1Dur+" bit2Dur = " + bit2Dur);
        byte bit1Pulses = 0;
        byte bit2Pulses = 0;
        for (byte i = 1; i<5; i++) {if (Math.abs( bit1Dur - (pulseWidth * i)) < pulseWidthTolerance) {bit1Pulses = i; break;}}
        for (byte i = 1; i<5; i++) {if (Math.abs(bit2Dur - (pulseWidth * i)) < pulseWidthTolerance) {bit2Pulses = i; break;}}
        if (bit1Pulses == 0 | bit2Pulses == 0)
        {
            System.out.println("First bits not within pulse tolerance");
            return false; // expecting bit pulse length ratio of 1,2,3,4 to pulse length for both bits
        }
        // we do have pausible bits

        HighLow zero = new HighLow(bit1Pulses,bit2Pulses);  // one and zero may be swapped
        HighLow one = new HighLow(bit2Pulses,bit1Pulses);
        System.out.print("Bit zero " + zero.toString() + " Bit one " + one.toString() );
        DecodedMessage dMsg = new DecodedMessage("***NEW***",pulseWidth,rawMessage.getReceivedTime());
        for (int i = firstDataTiming; i < rawMessage.events.size() - 1; i += 2)
        {
            bit1Dur = rawMessage.events.get(i).duration;
            bit2Dur = rawMessage.events.get(i+1).duration;
            //check each pair of bits matches the protocol definition for
            if (Math.abs( bit1Dur - pulseWidth * zero.high) < pulseWidthTolerance &&
                    Math.abs(bit2Dur - pulseWidth * zero.low) < pulseWidthTolerance)
            {
                // matched bit 0
                dMsg.addBit(false);
            } else if (Math.abs(bit1Dur - pulseWidth * one.high) < pulseWidthTolerance &&
                    Math.abs(bit2Dur - pulseWidth * one.low) < pulseWidthTolerance)
            {
                // matched bit 1
                dMsg.addBit(true);
            } else
            {
                System.out.println(" bad bits in message at "+i);
                return false; // Failed, out of spec bit pair cannot be translated
            }
        }
        decodedMessages.add(dMsg);
        return true;
    }

    /**
     * Run  -   The decoding loop
     */
    @Override
    public void run()
    {
        boolean decoded;
        while(!interrupted)
        {
            try
            {
                TimeUnit.MILLISECONDS.sleep(200);
            } catch (InterruptedException e)
            {
                interrupted = true;
                System.out.println("run interrupted");
                break;
            }
            if (newMessage)
            {
                newMessage=false;
                for(RawMessage message: rawMessages)
                {
                    decoded = processMessage(message);
                    if (decoded)
                    {
                        rawMessages.remove(message);
                        System.out.println(decodedMessages.get(decodedMessages.size()-1).toString());
                    }
                }
            }
        }
        System.out.println("Decoding stopped");
        System.out.printf("Summary - Decoded Messages: %d, Raw rawMessages: %d%n",decodedMessages.size(), rawMessages.size());
        for(DecodedMessage d:decodedMessages) System.out.println(d.toString());
        System.out.println("Messages not decoded");
        for(RawMessage r:rawMessages)
        {   analyseMsg(r);
            System.out.println(r.toString());
            System.out.println(r.waveform());
        }
        System.out.println("Timings in CSV format");
        for(RawMessage r:rawMessages)
        {
            System.out.println(r.timingsToCSV());
        }
        finished = true; // let main know
    }
}

class RF433Event
{
    final GpioPinDigitalStateChangeEvent gpioEvent;
    final int duration;

    RF433Event(GpioPinDigitalStateChangeEvent e, int d)
    {gpioEvent=e; duration=d;}

    @Override
    public String toString()
    {
        return gpioEvent.getEdge().toString()+" "+ duration;

    }
}

class RawMessage
{
    final ArrayList<RF433Event> events;
    private final Instant receivedTime;

    public Instant getReceivedTime()
    {
        return receivedTime;
    }
    RawMessage(int size)
    {
        events = new ArrayList<>(size);
        receivedTime = Instant.now();
    }
    @Override
    public String toString()
    {
        String s = "";
        for (RF433Event e: events)
        {
            s = s+e.duration+" "+e.gpioEvent.getState().toString().substring(0,2)+" "; // duration and new pin state
        }
        return s;
    }
    public String timingsToCSV()
    {
        String s = "";
        for (RF433Event e: events)
        {
            s = s+e.duration+",";
        }
        return s.substring(0,s.length()-1); // remove last ,
    }
    public String waveform()
    {
        String s = "";
        int pulses;
        int pulseWidth;
        // Characters tried for drawing the pulse train
        // Low line - "_", "\u0332" Combining Low Line, "\uFF3F"; FULLWIDTH LOW LINE
        // High line - "\u0305" COMBINING OVERLINE, "\u203E" over line
        // Vertical -  "\u20D2" COMBINING LONG VERTICAL LINE OVERLAY, "\u007C" Vertical line, "\u02E9" MODIFIER LETTER EXTRA-LOW TONE BAR
        if (events.get(0).duration > 50000) {return "Excessive duration in pluse 0 "+events.get(0).duration;}
        pulseWidth = 100; //gives a reasonable pulse train
        for (RF433Event e: events)
        {
            pulses = e.duration/pulseWidth;
            if (e.gpioEvent.getEdge() == PinEdge.RISING)
            {
                // rising edge so for the duration it was low
                for (int i = 0; i<pulses; i++) s = s+ "_";
                s = s+"\u20D2";
            } else
            {
                // falling edge so for the duration it was high
                for (int i = 0; i<pulses; i++) s = s+"\u0305";
                s = s+"\u20D2";
            }
        }
        return s;
    }
}
class DecodedMessage
{
    private final Instant receivedTime;
    private final String protocolName;
    private final int pulseWidth;
    private int numberOfBits;
    private long code;
    private String codeString;

    DecodedMessage(String protocolName,int pulseWidth, Instant t)
    {
        this.protocolName = protocolName;
        this.pulseWidth = pulseWidth;
        this.receivedTime = t;
        this.numberOfBits = 0;
        this.code = 0;
        this.codeString = "";
    }

    @Override
    public String toString()
    {
        return String.format("%s (PulseWidth %d) %d bits code %d 0x%h %s",
                protocolName,pulseWidth,numberOfBits,code,code,codeString);
    }

    public void addBit(boolean bit)
    {
        codeString += ((bit)?"1":"0");
        code = ((code<<1) | ((bit)?1:0)); //this will overflow and lose msb if moe than 64 bits
        numberOfBits++;
    }

    //getters
    public int size() {return codeString.length();}
    public String getBits() {return codeString;}
    public long getCode() {return code;}
    public Instant getReceivedTime(){return receivedTime;}
}
