/*
 * this is a school project under "The Unlicence".
 */
package PCP.Min.data;

import PCP.Min.data.MsgUserToUser;
import java.util.ArrayList;
import org.junit.*;

/**
 *
 * @author JacopoWolf
 */
public class MsgUserToUserPacket_Test
{
    @Test
    public void testToBytesSingle()
    {
        MsgUserToUser packet = new MsgUserToUser
        (   
            new byte[]{0,0},
            "testme",
            "this is simple text"
        );
        
        byte[] expectedResult = new byte[]
        { 
            01, 
            0,0,
            116,101,115,116,109,101,
            0,
            116,104,105,115,32,105,115,32,115,105,109,112,108,101,32,116,101,120,116,
            0
        };
        
        ArrayList<byte[]> results = new ArrayList<> (packet.toBytes());
        
        if ( results.size() != 1 )
            Assert.fail();
        
        Assert.assertArrayEquals( expectedResult, results.get(0) );
        
    }
    
}
