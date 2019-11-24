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
public class AliasChange_Test
{
    @Test
    public void testToBytesSingle()
    {
        // result
        AliasChange aliaschange = new AliasChange( new byte[] {0,0} , "gfurri", "gfurri20");
        ArrayList<byte[]> result = (ArrayList<byte[]>) aliaschange.toBytes();
        
        // expected
        byte[] expected = new byte[] 
        {
            18,     // opcode
            0,0,    // sender id
            103, 102, 117, 114, 114, 105,               // old alias
            0,
            103, 102, 117, 114, 114, 105, 50, 48,       // new alias
            0
        };
        
        if ( result.size() != 1 )
            Assert.fail();
        
        Assert.assertArrayEquals(expected, result.get(0));
    }
}
