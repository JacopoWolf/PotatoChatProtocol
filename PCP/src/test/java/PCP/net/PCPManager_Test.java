/*
 * this is a school project under "The Unlicence".
 */
package PCP.net;

import PCP.*;
import java.io.*;
import java.net.*;
import org.junit.*;


/**
 *
 * @author Jacopo_Wolf
 */
public class PCPManager_Test
{   
    // initializes Manager
    PCPManager middlewere;
    ServerSocket sskt;
    
    @Before
    public void setUp () throws IOException
    {
        // init middlewere
        middlewere = new PCPManager();
        
        // init mock server
        sskt = new ServerSocket(PCP.PORT,0,InetAddress.getByName("localhost"));
        
        
        
    }
    
    @Test
    public void Initialize() throws IOException
    {
        // ! rewrite the whole test using channels
        /**
        // test incoming connection
        Socket testSender = new Socket(InetAddress.getByName("localhost"), PCP.PORT , true);
            BufferedOutputStream bos = new BufferedOutputStream(testSender.getOutputStream());
            BufferedInputStream bin = new BufferedInputStream(testSender.getInputStream());
            for ( byte[] b : new Registration("Pippo", "general").toBytes() )
                bos.write( b );
            bos.flush();
        
        // accepts connection
        Socket mockSocket = sskt.accept();
        
        // initialized new PCPChannel
        PCPChannel recievedTest = new PCPChannel(mockSocket,null);
        
        byte[] b = new byte[recievedTest.getBuffInStream().available()];
        if ( recievedTest.getBuffInStream().read(b) = 16 )
            Assert.fail();
        
        
        // MAIN TEST
        middlewere.accept( b , recievedTest );
 
        b = new byte[2];
        bin.read(b);
        Assert.assertArrayEquals
        (
            b, 
            new byte[] { -1 , -2 }  // { 255, 254 }
        ); // java bytes must go from +128 to -128 cuz they're signed. That's bullshit imo but ok
        */
    }    
}
