/*
 * this is a school project under "The Unlicence".
 */
package PCP.net;

import PCP.services.*;
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
                Socket test  = new Socket( InetAddress.getLoopbackAddress() , PCP.PCP.PORT );
                System.out.println("open client on: " + test.getLocalSocketAddress().toString());
                    BufferedOutputStream bout = new BufferedOutputStream( test.getOutputStream() );
                    BufferedInputStream bin = new BufferedInputStream( test.getInputStream() );
                    byte[] buffer = (""+a).getBytes();
                        bout.write(buffer);
                        bout.flush();
                    buffer = new byte[2];
                        bin.read(buffer);
                    Logger.getGlobal().log(Level.INFO, "TESTSOCKET n." + a + " recieved {0}", Arrays.toString(buffer));

                    if (! Arrays.equals(  buffer ,  new byte[] {-1,-2}) )
                        Assert.fail();
                
                    Thread.sleep(500);
                test.close();
            }
            catch (IOException | InterruptedException e)
            {
                Assert.fail();
            }
                
            });
            t.start();
        }
        
        Thread.sleep(2000);
        server.interrupt();
        
    }
    
}
