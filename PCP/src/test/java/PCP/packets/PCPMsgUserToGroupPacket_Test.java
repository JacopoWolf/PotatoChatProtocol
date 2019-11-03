/*
 * this is a school project under "The Unlicence".
 */

package PCP.packets;

import java.util.*;
import org.junit.*;

/**
 *
 * @author gfurri20
 */
public class PCPMsgUserToGroupPacket_Test
{
    @Test
    public void testToBytesSingle()
    {
        PCPMsgUserToGroupPacket packet = new PCPMsgUserToGroupPacket
        (
            new byte[] {0,0},
            "test"
        );
        
        byte[] expectedResult = new byte[]
        { 
            05, 
            0,0,
            116, 101, 115, 116,
            0
        };
        
        ArrayList<byte[]> results = new ArrayList<> (packet.toBytes());
        
        if ( results.size() != 1 )
            Assert.fail();
        
        Assert.assertArrayEquals( expectedResult, results.get(0) );
    }
}
