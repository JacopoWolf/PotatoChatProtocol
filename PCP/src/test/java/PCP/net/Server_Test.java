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
        PCPServer server = new PCPServer();
        server.acceptAndServe();
        
        
        
        Thread.sleep(2000);
        for ( int i = 0; i < 10; i++ )
        {
            Socket test  = new Socket( InetAddress.getLocalHost() , PCP.PCP.PORT );
            System.out.println("open client on: " + test.getLocalSocketAddress().toString());
                BufferedOutputStream bout = new BufferedOutputStream(test.getOutputStream());
                BufferedInputStream bin = new BufferedInputStream( test.getInputStream() );
                byte[] buffer = (""+i).getBytes();
                    bout.write(buffer);
                    bout.flush();
                    
                Thread.sleep(500);
                buffer = new byte[2];
                bin.read();
                Logger.getGlobal().log(Level.INFO, "recieved {0}", Arrays.toString(buffer));
        }
        
    }
    
}
