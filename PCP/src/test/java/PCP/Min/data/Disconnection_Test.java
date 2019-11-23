/*
 * this is a school project under "The Unlicence".
 */
package PCP.Min.data;

import java.util.ArrayList;
import org.junit.*;


/**
 *
 * @author @Alessio789
 */
public class Disconnection_Test 
{
    @Test 
    public void testToBytesDisconnectionByClient()
    {
        //result
        Disconnection disconnection = new Disconnection( new byte[] {1, 2} );
        ArrayList<byte[]> result = ( ArrayList<byte[]> ) disconnection.toBytes();
        
        //expected
        byte[] expected = new byte[]
        {
            11,      //opcode
            1, 2     //id
        };
          
        if ( result.size() != 1 )
            Assert.fail();
        
        Assert.assertArrayEquals( expected, result.get( 0 ) );
    }
    
    @Test
    public void testToBytesDisconnectionByServer()
    {
        //result
        Disconnection disconnection = new Disconnection( Disconnection.Reason.none );
        ArrayList<byte[]> result = ( ArrayList<byte[]> ) disconnection.toBytes();
        
        //expected
        byte[] expected = new byte[]
        {
            11,      //opcode
            0,       //id
        };
        
         if ( result.size() != 1 )
            Assert.fail();
        
        Assert.assertArrayEquals( expected, result.get( 0 ) );
    }
}
