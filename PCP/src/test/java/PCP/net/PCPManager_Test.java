/*
 * this is a school project under "The Unlicence".
 */
package PCP.net;

import PCP.Min.data.*;
import PCP.net.ServerChannels_Test.ServerTest;
import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.logging.*;
import org.junit.*;


/**
 *
 * @author Jacopo_Wolf
 */
@Ignore(value = "this test is obsolete")
public class PCPManager_Test
{   
    // initializes Manager
    PCPManager middleware;
    
    @Before
    public void setUp () throws IOException
    {
        Logger.getGlobal().log(Level.INFO,"Started!");
        // init middleware
        middleware = new PCPManager();

    }
    
    @Test
    public void Initialize() throws IOException, InterruptedException
    {
        // start the server
        ServerTest stest = new ServerChannels_Test().new ServerTest();
            stest.handler = new CompletionHandler<AsynchronousSocketChannel, Void>()
            {
                @Override
                public void completed( final AsynchronousSocketChannel result, Void attachment )
                {
                    final PCPChannel channel = new PCPChannel(result, null);
                        channel.setTimeLeftAwake( middleware.getDefaultkeepAlive() );
                        
                    final ByteBuffer bb = ByteBuffer.allocate(4096); // 4Kbs
                        
                    result.read
                    (
                        bb, 
                        null, 
                        new CompletionHandler<Integer, Void>()
                        {
                            @Override
                            public void completed( Integer result, Void attachment )
                            {
                                channel.getChannel().read(bb, attachment, this);
                                byte[] destination = new byte[result];
                                bb.get(destination, 0, result);
                                middleware.accept( destination , channel );
                            }

                            @Override
                            public void failed( Throwable exc, Void attachment )
                            {
                                
                            }
                        });
                    return;
                        
                }

                @Override
                public void failed( Throwable exc, Void attachment )
                {
                }
            };
        stest.start();

        
        // start sending requests
        for ( int i = 0; i < 1; i++ )
        {
             Socket socket = new Socket("localhost", PCP.PCP.PORT);
                    BufferedOutputStream bout = new BufferedOutputStream(socket.getOutputStream());
                    BufferedInputStream bin = new BufferedInputStream(socket.getInputStream());
                    
                for ( byte[] b : new Registration("Pippo", "general").toBytes() )
                    bout.write( b );
                bout.flush();
                    
                byte[] errorAnswer = new byte[2];
                
                bin.read(errorAnswer);
                
                socket.close();
                
                //todo update when middlewere will be further implemented
                Assert.assertArrayEquals
                (
                    errorAnswer,
                    new byte[] { -1 , -2 }  // { 255, 254 }
                );
        }
        
        Thread.sleep(2000);
        stest.interrupt();
        
    }
}