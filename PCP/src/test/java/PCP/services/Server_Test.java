/*
 * this is a school project under "The Unlicence".
 */
package PCP.services;

import PCP.Min.data.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.*;
import org.junit.*;


/**
 *
 * @author Jacopo_Wolf
 */
public class Server_Test
{
    @Test
    public void serverSingleRequest() throws IOException, InterruptedException
    {
        Logger.getLogger("").getHandlers()[0].setLevel(Level.FINEST);
        Logger.getGlobal().setLevel(Level.FINEST);
        
        PCPServer server = new PCPServer(InetAddress.getLoopbackAddress());
        server.acceptAndServe();
        
        Thread.sleep(1000);
        exec(0);
        
        server.shutDown();
        
    }
    
    @Test @Ignore
    public void serverConnections () throws IOException, InterruptedException
    {
        
        Logger.getLogger("").getHandlers()[0].setLevel(Level.FINEST);
        Logger.getGlobal().setLevel(Level.FINEST);
        
        PCPServer server = new PCPServer(InetAddress.getLoopbackAddress());
        server.acceptAndServe();    
        
        for ( int i = 0; i < 5; i++ )
        {
            final int a = i;
            Thread t = new Thread(() ->{
            try
            {
                exec(a);  
            }
            catch (IOException | InterruptedException e)
            {
                Assert.fail();
            }
                
            });
            t.start();
        }
        
        Thread.sleep(200);
        server.interrupt();
        
    }
 
    
    public void exec( int val ) throws IOException, InterruptedException
    {
         Socket test  = new Socket( InetAddress.getLoopbackAddress() , PCP.PCP.PORT );
                System.out.println("open client on: " + test.getLocalSocketAddress().toString());
                    BufferedOutputStream bout = new BufferedOutputStream( test.getOutputStream() );
                    BufferedInputStream bin = new BufferedInputStream( test.getInputStream() );
                    for ( byte[] buffer : new Registration("testAlias", "").toBytes() )
                    {
                        bout.write(buffer);
                        bout.flush();
                        Logger.getGlobal().log(Level.INFO, "TESTSOCKET n." + val + " sent {0}", Arrays.toString(buffer));
                    }
                    
                    Thread.sleep(200);
                    
                    byte[] buffer = new byte[18];
                        bin.read(buffer);
                        
                    Logger.getGlobal().log(Level.INFO, "TESTSOCKET n." + val + " recieved {0}", Arrays.toString(buffer));
                    
                    /*if (! Arrays.equals(  buffer ,  new byte[] {-1,-2}) )
                        Assert.fail();*/
                    
                test.close();
    }
    
}
