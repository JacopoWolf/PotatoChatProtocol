/*
 * this is a school project under "The Unlicence".
 */
package PCP.services;

import PCP.*;
import PCP.logic.*;
import PCP.net.*;
import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.logging.*;


/**
 *  a PCP server, serving on the port 53101.
 *  uses {@link Logger#getGlobal()} to log data. 
 *  to start the server, simply initalize it and the call {@link PCPServer#acceptAndServe()}
 * 
 * @author Jacopo_Wolf
 */
public final class PCPServer extends Thread implements IPCPServer
{
    final PCPManager middleware;
    final AsynchronousServerSocketChannel assc;
    final ExecutorService managerExecutor;
    
    @Override
    public IPCPManager getManager()
    {
        return middleware;
    }

    @Override
    public IMemoryAccess getMemoryAccess()
    {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public AsynchronousServerSocketChannel getAsyncServerSktChnl()
    {
        return this.assc;
    }

    /**
     * initialize a new PCP server with default values
     * @param address the address to bind the server to
     * @throws IOException 
     */
    public PCPServer ( InetAddress address ) throws IOException
    {
        this(address, 3, 2);
    }
    
    /**
     * initialize a new PCP server
     * @param address the address to bind the server to
     * @param listeningPoolSize number of threads dedicated to listen for incoming data and connections
     * @param middlewarePoolSize number of threads dedicated to execute the middleware
     * @throws IOException 
     */
    public PCPServer( InetAddress address, int listeningPoolSize, int middlewarePoolSize ) throws IOException
    {
        Logger.getGlobal().info("initializing server...");
        
        // middleware initialization
        this.managerExecutor = Executors.newFixedThreadPool( middlewarePoolSize );
        this.middleware = new PCPManager();
        
        // socket binding
        InetSocketAddress addr = new InetSocketAddress(address, PCP.PORT);
        
        try
        {
            this.assc = AsynchronousServerSocketChannel.open
                            (
                                AsynchronousChannelGroup.withThreadPool
                                (
                                    Executors.newFixedThreadPool( listeningPoolSize )
                                ) 
                            );
                    assc.bind(addr);
            Logger.getGlobal().log(Level.INFO, "server threadpool successfully initialized and binded on {0}", addr.toString());
        }
        catch ( IOException ioe )
        {
            Logger.getGlobal().log(Level.SEVERE, "failed to initialize the server on {0}", addr.toString());
            throw ioe;
        }
        
    }
      
 
    
    @Override
    public void acceptAndServe() throws IOException
    {
        this.start();
        Logger.getGlobal().log(Level.INFO, "server listening...");
    }
    
    @Override
    public void acceptAndServe( PCP.Versions startWith ) throws IOException
    {
        throw new UnsupportedOperationException();
    }
    
    
    
    @Override
    public void run()
    {
        try
        {
            this.assc.accept
            (
                null, 
                new CompletionHandler<AsynchronousSocketChannel, Void>()
                {
                    @Override
                    public void completed( AsynchronousSocketChannel result, Void attachment )
                    {
                        // allow to answer new connection
                        assc.accept(null, this);
                        
                        // first read
                        managerExecutor.submit
                        ( 
                            () -> 
                            {
                                final byte[] b = new byte[PCP.Versions.ALL.MAX_PACKET_LENGHT()];
                                ByteBuffer bb = ByteBuffer.wrap(b);
                                IPCPChannel ch = new PCPChannel(result, null);
                                result.read(bb, ch, channelDataRecieved);
                                
                                middleware.accept(b, ch);
                            }
                                
                        );
                        
                        Logger.getGlobal().log(Level.INFO, "successfully recieved new connection");
                    }

                    @Override
                    public void failed( Throwable exc, Void attachment )
                    {
                        Logger.getGlobal().log(Level.WARNING,"rrror recieving new connection",exc);
                    }
                }
            );
            
            while ( true )
            {
                Thread.sleep(10000);
            }
        }
        catch( InterruptedException ex )
        {
            Logger.getGlobal().log(Level.INFO, "server interrupted!", ex);
        }
    }
    
    
    
    
    @Override
    public void shutDown()
    {
        try
        {
            // disposes of in-use resources
            this.middleware.dispose();
            // closes socket
            this.assc.close();
            // interrupts working loop.
            this.interrupt();
        }
        catch( IOException ex )
        {
            Logger.getGlobal().log(Level.SEVERE, null, ex);
        }
    }
    
    
    

    private CompletionHandler<Integer, IPCPChannel> channelDataRecieved = 
        new CompletionHandler<Integer, IPCPChannel>()
        {
            @Override
            public void completed( Integer bytesRead, IPCPChannel channel )
            {
                // first reading
                managerExecutor.submit
                ( 
                    () -> 
                    {
                        ByteBuffer bb = ByteBuffer.allocate(PCP.Versions.ALL.MAX_PACKET_LENGHT());
                        
                        channel.getChannel().read(bb, channel, channelDataRecieved);

                        middleware.accept( Arrays.copyOfRange(bb.array(), 0, bytesRead), channel);
                    }
                );
            }

            @Override
            public void failed( Throwable exc, IPCPChannel attachment )
            {
                Logger.getGlobal().log(Level.WARNING,"connection interrupted!");
            }
        };   
 
}