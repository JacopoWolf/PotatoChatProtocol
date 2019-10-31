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
public class PCPGroupUserList_TEST
{
    @Test
    public void testToBytesSingle()
    {
        PCPGroupUsersList packet = new PCPGroupUsersList
        (
            0,
            3,
            "[\"ALIAS1\",\"ALIAS2\",\"ALIAS3\"]"
        );
        
        byte[] expectedResult = new byte[]
        { 
            51,
            0, 
            3,
            91, 34, 65, 76, 73, 65, 83, 49, 34, 44, 34, 65, 76, 73, 65, 83, 50, 34, 44, 34, 65, 76, 73, 65, 83, 51, 34, 93, 
            0
        };
        
        ArrayList<byte[]> results = new ArrayList<> (packet.toBytes());
        
        if ( results.size() != 1 )
            Assert.fail();
        
        Assert.assertArrayEquals( expectedResult, results.get(0) );
    }
}
