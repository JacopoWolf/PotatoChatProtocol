/*
 * this is a school project under "The Unlicence".
 */
package PCP.logic;

import PCP.Min.data.*;
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
    
    @Test
    public void interpretDisconnection() throws PCPException
    {
        //expected
        Disconnection dis = new Disconnection( new byte[] {50,49} ); // id = (int) 21
        ArrayList<byte[]> disBytes = new ArrayList<> (dis.toBytes());
        byte[] expected = disBytes.get(0);
        
        PCPMinInterpreter interpreter = new PCPMinInterpreter();
        
        //result
        Disconnection interpretedDis = (Disconnection) interpreter.interpret(disBytes.get(0));
        ArrayList<byte[]> interpretedDisBytes = new ArrayList<> (interpretedDis.toBytes());
        byte[] result = interpretedDisBytes.get(0);
        
        if ( disBytes.size() != 1 )
            Assert.fail();
        
        Assert.assertArrayEquals(expected, result) ;
    }
    
    @Test
    public void interpretAliasChange() throws PCPException
    {
        //expected
        AliasChange ac = new AliasChange( new byte[] {50,49}, "gfurri" , "gfurri20" );
        ArrayList<byte[]> acBytes = new ArrayList<> (ac.toBytes());
        byte[] expected = acBytes.get(0);
        
        PCPMinInterpreter interpreter = new PCPMinInterpreter();
        
        //result
        AliasChange interpretedAc = (AliasChange) interpreter.interpret(acBytes.get(0));
        ArrayList<byte[]> interpretedAcBytes = new ArrayList<> (interpretedAc.toBytes());
        byte[] result = interpretedAcBytes.get(0);
        
        if ( acBytes.size() != 1 )
            Assert.fail();
        
        Assert.assertArrayEquals(expected, result) ;
    }
    
    @Test
    public void interpretErrorMsg() throws PCPException
    {
        //expected
        ErrorMsg err = new ErrorMsg( PCPException.ErrorCode.PackageMalformed );
        ArrayList<byte[]> errBytes = new ArrayList<> (err.toBytes());
        byte[] expected = errBytes.get(0);
        
        PCPMinInterpreter interpreter = new PCPMinInterpreter();
        
        //result
        ErrorMsg interpretedErr = (ErrorMsg) interpreter.interpret(errBytes.get(0));
        ArrayList<byte[]> interpretedErrBytes = new ArrayList<> (interpretedErr.toBytes());
        byte[] result = interpretedErrBytes.get(0);
        
        if ( errBytes.size() != 1 )
            Assert.fail();
        
        Assert.assertArrayEquals(expected, result) ;
    }
}
