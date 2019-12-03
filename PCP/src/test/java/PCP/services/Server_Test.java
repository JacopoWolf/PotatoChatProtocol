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
    static PCPServer server;
    
    @Before
    public void startServer() throws IOException, InterruptedException
    {
        Logger.getLogger("").getHandlers()[0].setLevel(Level.FINEST);
        Logger.getGlobal().setLevel(Level.FINEST);
        
        server = new PCPServer(InetAddress.getLoopbackAddress());
        server.start();   
        Thread.sleep(200);
    }
    
    @Test @Ignore
    public void serverSingleRequest() throws IOException, InterruptedException
    {
        
        exec(-1);
       
    }
    
    @Test
    public void serverMultipleConcurrentRequests () throws IOException, InterruptedException
    {    
        for ( int i = 0; i <= 5; i++ )
        {
            final int a = i;
            Thread t = new Thread
            (
                () ->
                {
                    try
                    {
                        exec(a);  
                    }
                    catch (IOException | InterruptedException e)
                    {
                        Logger.getGlobal().log(Level.WARNING, "TEST: TESTSOCKET n." + a + " closed with an error",e);
                    }
                }
            );
            t.start();
        }
        
        Thread.sleep(5000);
        
    }
 
    
    public void exec( int val ) throws IOException, InterruptedException
    {
         Socket test  = new Socket( InetAddress.getLoopbackAddress() , PCP.PCP.PORT );
                Logger.getGlobal().log(Level.INFO, "TEST: open client on: {0}", test.getLocalSocketAddress().toString());
                    BufferedOutputStream bout = new BufferedOutputStream( test.getOutputStream() );
                    BufferedInputStream bin = new BufferedInputStream( test.getInputStream() );
                    for ( byte[] buf : new Registration("test"+val +"Alias", "").toBytes() )
                    {
                        bout.write(buf);
                        bout.flush();
                        Logger.getGlobal().log(Level.INFO, "TEST: TESTSOCKET n." + val + " sent {0}", Arrays.toString(buf));
                    }
                    
                    Thread.sleep(200);
                    
                    byte[] buffer = new byte[18];
                    int read = bin.read(buffer);
                    buffer = Arrays.copyOfRange(buffer, 0, read);
                        
                    Logger.getGlobal().log(Level.INFO, "TEST: TESTSOCKET n." + val + " recieved {0}", Arrays.toString(buffer));
                    
                    Thread.sleep(100);
                    
                    for ( byte[] buf : new MsgUserToUser( new byte[]{buffer[1],buffer[2]},"test" + val + "Alias","messaggio di test, forse o" ).toBytes() )
                    {
                        bout.write(buf);
                        bout.flush();
                        Logger.getGlobal().log(Level.INFO, "TEST: TESTSOCKET n." + val + " sent a message {0}", Arrays.toString(buf));
                    }
                    
                    Thread.sleep(500);
                    
                    buffer = new byte[64];
                    read = bin.read(buffer);
                    
                test.close();

    }
    
    @After
    public void stopServer() throws InterruptedException
    {
        server.shutDown();
    }
    
}
