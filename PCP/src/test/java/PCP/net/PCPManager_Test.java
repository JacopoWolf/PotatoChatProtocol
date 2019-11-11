/*
 * this is a school project under "The Unlicence".
 */
package PCP.net;

import PCP.Min.data.*;
import java.net.*;
import org.junit.*;


/**
 *
 * @author Jacopo_Wolf
 */
public class PCPManager_Test
{   
    
    @Test
    public void Initialize()
    {
        // initializes Manager
        PCPManager middlewere = new PCPManager();
        
        // creates a mock connection
        Registration reg = new Registration("Pippo", "general");
        Socket mockSocket = new Socket();
        PCPSocket testSender = new PCPSocket(mockSocket, null, null, null);
        
        // done only once
        for ( byte[] b : reg.toBytes() )
            middlewere.recieve(b, null); // ! fails because send() is not implemented
        
        
        
    }
    
    
}
