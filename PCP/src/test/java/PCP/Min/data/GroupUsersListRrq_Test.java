/*
 * this is a school project under "The Unlicence".
 */
package PCP.Min.data;

import java.util.ArrayList;
import org.junit.Assert;
import org.junit.Test;


/**
 *
 * @author gfurri20
 */
public class GroupUsersListRrq_Test
{
    @Test
    public void testToBytes()
    {
        // result
        GroupUsersListRrq rrq = new GroupUsersListRrq( new byte[] {0,0} );
        ArrayList<byte[]> result = new ArrayList<> (rrq.toBytes());
        
        // expected
        byte[] expected = new byte[]
        {
            50,
            0,0,
        };
        
        if ( result.size() != 1 )
            Assert.fail();
        
        Assert.assertArrayEquals(expected, result.get(0));
    }
}
