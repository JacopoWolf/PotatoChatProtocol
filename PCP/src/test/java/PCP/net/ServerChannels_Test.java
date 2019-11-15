package PCP.net;

import java.io.*;
import static java.lang.Thread.currentThread;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.concurrent.*;
import org.junit.*;


/**
 *
 * @author Jacopo_Wolf
 */
//@Ignore
public class ServerChannels_Test
{
    @Test
    public void asyncserver() throws IOException, InterruptedException
    {
        System.out.println("hello from thread " + currentThread().toString());
        ServerTest serverTest = new ServerChannels_Test().new ServerTest();
            serverTest.start();
        
        Thread.sleep(2000);
        for ( int i = 0; i < 10; i++ )
        {
            Socket test  = new Socket("localhost", PCP.PCP.PORT );
            System.out.println("open client on: " + test.getLocalSocketAddress().toString());
                BufferedOutputStream bout = new BufferedOutputStream(test.getOutputStream());
                byte[] buffer = (""+i).getBytes();
                    bout.write(buffer);
                    bout.flush();
                    test.close();
                
            
        }
        
        Thread.sleep(2000);
        serverTest.interrupt();
    }
    
    
    class ServerTest extends Thread
    {
        AsynchronousServerSocketChannel SERVER;
        public CompletionHandler<AsynchronousSocketChannel, Void> handler =
                new CompletionHandler<AsynchronousSocketChannel, Void>()
                    {

                        @Override
                        public void completed( AsynchronousSocketChannel result, Void attachment )
                        {
                            SERVER.accept(null, this); // that's here because java nio sucks

                            try
                            {
                                System.out.println
                                (
                                    "recieved a connection from " + result.getRemoteAddress().toString()
                                );
                                System.out.println("hello from thread " + currentThread().toString());

                                ByteBuffer bb = ByteBuffer.allocate(1024);
                                result.read(bb).get();
                                
                                System.out.println( new String( bb.array() ) );
                                result.close();
                            }
                            catch( InterruptedException | ExecutionException | IOException ex )
                            {
                                System.out.println( ex.getMessage() );
                            }

                            
                            
                        }

                        @Override
                        public void failed( Throwable exc, Void attachment )
                        {
                            System.out.println( exc.getMessage() );
                        }
                    } ;
        
        @Override
        public void run()
        {
            try
            {
                System.out.println("hello from thread " + currentThread().toString());
                 
                SERVER = 
                        AsynchronousServerSocketChannel.open
                        (
                            AsynchronousChannelGroup.withThreadPool
                            (
                                Executors.newFixedThreadPool(3)
                            )
                        );
                    SERVER.bind( new InetSocketAddress("localhost", PCP.PCP.PORT) );
                    System.out.println("open server on: " + SERVER.getLocalAddress().toString());
               
                
                SERVER.accept
                (
                    null, 
                    handler
                );

                
            }
            catch (IOException e)
            {
                System.out.println( e.getMessage() );
                return;
            }
        }
        
    }
    
    
}
