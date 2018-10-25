package br.com.anteros.iot.things.test.rf;

/**
 * Created by GJWood on 24/01/2017.
 */
public class HighLow
{
    public final byte high;
    public final byte low;

    public HighLow(byte h, byte l)
    {
        high = h;
        low = l;
    }
    public String toString()
    {
        return String.format("[%d,%d]",high,low);
    }
}
