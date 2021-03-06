/*
 * this is a school project under "The Unlicence".
 */

package PCP.Min.data;

import java.util.*;
import org.junit.*;

/**
 *
 * @author gfurri20
 */
public class GroupUsersList_Test
{
    @Test
    public void testToBytesSingle()
    {
        Collection<String> listUsers = new ArrayList<>();
        listUsers.add("user1");
        listUsers.add("user2");
        listUsers.add("user3");
        
        GroupUsersList packet = new GroupUsersList
        (
            GroupUsersList.UpdateType.complete,
            listUsers
        );
        
        byte[] expectedResult = new byte[]
        { 
            51, 
            0,
            3,
            91, 34, 117, 115, 101, 114, 49, 34, 44, 34, 117, 115, 101, 114, 50, 34, 44, 34, 117, 115, 101, 114, 51, 34, 93, 
            0
        };
                
        ArrayList<byte[]> results = new ArrayList<> (packet.toBytes());
        
        if ( results.size() != 1 )
            Assert.fail();
        
        Assert.assertArrayEquals( expectedResult, results.get(0) );
    }
}
