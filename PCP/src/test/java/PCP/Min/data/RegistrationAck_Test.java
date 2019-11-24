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
public class RegistrationAck_Test 
{
    @Test
    public void testToBytesRegistrationAck() 
    {
        //result
        RegistrationAck registrationAck = new RegistrationAck( new byte[] {1, 2}, "Alessio789" );
        ArrayList<byte[]> result = ( ArrayList<byte[]> ) registrationAck.toBytes();
        
        //expected
        byte[] expected = new byte[]
        {
            20,                                             //opcode
            1, 2,                                           //id
            65, 108, 101, 115, 115, 105, 111, 55, 56, 57,   //alias
            0                                               //delimitator
        };
        
        if ( result.size() != 1 )
            Assert.fail();
        
        Assert.assertArrayEquals( expected, result.get( 0 ) );
    }
}
