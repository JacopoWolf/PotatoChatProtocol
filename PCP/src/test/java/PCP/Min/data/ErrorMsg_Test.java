/*
 * this is a school project under "The Unlicence".
 */
package PCP.Min.data;

import PCP.PCPException;
import java.util.ArrayList;
import org.junit.*;


/**
 *
 * @author @Alessio789
 */
public class ErrorMsg_Test 
{
    @Test
    public void testToBytesErrorMsg() 
    {
        //result
        ErrorMsg errorMsg = new ErrorMsg( PCPException.ErrorCode.ServerExploded );
        ArrayList<byte[]> result = ( ArrayList<byte[]> ) errorMsg.toBytes();
        
        //expected
        byte[] expected = new byte[]
        {
            ( byte ) 255,       //opcode
            ( byte ) 254        //errorcode
        };
        
        if ( result.size() != 1 )
            Assert.fail();
        
        Assert.assertArrayEquals( expected, result.get( 0 ) );
    }
    
}
