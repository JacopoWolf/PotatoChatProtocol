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
public class Registration_Test 
{
    @Test
    public void testToBytesRegistrationWithTopic() 
    {
        //result
        Registration registration = new Registration( "Alessio789", "testing" );
        ArrayList<byte[]> result = ( ArrayList<byte[]> ) registration.toBytes();
        
        //expected
        byte[] expected = new byte[]
        {
           10,                                             //opcode
           0,                                              //version
           65, 108, 101, 115, 115, 105, 111, 55, 56, 57,   //alias
           0,                                              //delimitator
           116, 101, 115, 116, 105, 110, 103,              //topic
           0                                               //delimitator
        };
        
        if ( result.size() != 1 )
            Assert.fail();
        
        Assert.assertArrayEquals( expected, result.get( 0 ) );
    }
    
    @Test
    public void testToBytesRegistration() 
    {
        //result
        Registration registration = new Registration( "Alessio789", null );
        ArrayList<byte[]> result = ( ArrayList<byte[]> ) registration.toBytes();
        
        //expected
        byte[] expected = new byte[]
        {
           10,                                             //opcode
           0,                                              //version
           65, 108, 101, 115, 115, 105, 111, 55, 56, 57,   //alias
           0,                                              //delimitator
           0                                               //delimitator
        };
        
        if ( result.size() != 1 )
            Assert.fail();
        
        Assert.assertArrayEquals( expected, result.get( 0 ) );
    }
}
