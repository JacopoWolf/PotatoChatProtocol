/*
 * this is a school project under "The Unlicence".
 */
package PCP.Min.data;

import PCP.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 *
 * @author gfurri20
 */
public class MsgReceived_Test
{
    public static String MESSAGE = "";
    
    @Test
    public void testToBytesSingle()
    {
        // result
        MsgRecieved msg = new MsgRecieved( "gfurri20", OpCode.MsgUserToUser, "This is a messagge!" );
        ArrayList<byte[]> result = new ArrayList<> (msg.toBytes());
        
        // expected
        byte[] expected = new byte[]
        {
            01,
            103, 102, 117, 114, 114, 105, 50, 48,
            0,
            84, 104, 105, 115, 32, 105, 115, 32, 97, 32, 109, 101, 115, 115, 97, 103, 103, 101, 33,
            0,
        };
        
        if ( result.size() == -1 )
            Assert.fail();
        
        Assert.assertArrayEquals(expected, result.get(0));
    }
    
    @Test
    public void testToBytesMultiple()
    {
        for ( int i = 0; i < 2037; i++ )
            MESSAGE += "a";
        
        // result
        MsgRecieved msg = new MsgRecieved( "gfurri20", OpCode.MsgUserToUser, MESSAGE);
        ArrayList<byte[]> result = new ArrayList<> (msg.toBytes());
        
        if ( result.size() != 2 )
            Assert.fail("The number of packets doesn't corrispond!");
        
        Logger.getGlobal().log(Level.INFO, "{0} - {1}", new Object[]{String.valueOf(result.get(0).length), String.valueOf(result.get(1).length)});
    }
}
