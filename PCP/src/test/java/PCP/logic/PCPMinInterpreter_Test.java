/*
 * this is a school project under "The Unlicence".
 */
package PCP.logic;

import PCP.Min.data.Registration;
import PCP.Min.logic.PCPMinInterpreter;
import PCP.PCPException;
import java.util.ArrayList;
import org.junit.Assert;
import org.junit.Test;




/**
 *
 * @author gfurri20
 */
public class PCPMinInterpreter_Test
{
    @Test
    public void interpretRegistration() throws PCPException 
    {
        //expected
        Registration reg = new Registration("gfurri20", "general");
        ArrayList<byte[]> regBytes = new ArrayList<> (reg.toBytes());
        byte[] expected = regBytes.get(0);
        
        PCPMinInterpreter interpreter = new PCPMinInterpreter();
        
        //result
        Registration interpretedReg = (Registration) interpreter.interpret(regBytes.get(0));
        ArrayList<byte[]> interpretedRegBytes = new ArrayList<> (interpretedReg.toBytes());
        byte[] result = interpretedRegBytes.get(0);
        
        if ( regBytes.size() != 1 )
            Assert.fail();
        
        Assert.assertArrayEquals(expected, result) ;
    }
}
