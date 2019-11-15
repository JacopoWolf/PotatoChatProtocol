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
import java.util.concurrent.*;
import java.util.logging.*;


/**
 * 
 * @author Jacopo_Wolf
 */
public class PCPServer extends Thread implements IPCPServer
{
    final PCPManager middleware;
    final AsynchronousServerSocketChannel assc;
    
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

    
    
    public PCPServer( InetAddress address ) throws IOException
    {
        this.middleware = new PCPManager();
        this.assc = AsynchronousServerSocketChannel.open
                        (
                            AsynchronousChannelGroup.withThreadPool
                            (
                                Executors.newFixedThreadPool( 6 )
                            ) 
                        );
                assc.bind(new InetSocketAddress(address, PCP.PORT));
    }
      
 
    
    @Override
    public void acceptAndServe() throws IOException
    {
        this.start();
    }
    
    @Override
    public void acceptAndServe( PCP.Versions startWith ) throws IOException
    {
        throw new UnsupportedOperationException();
    }
    
    /**
     *  
     */
    @Override
    public void run()
    {
        Logger.getGlobal().log(Level.INFO, "Initialized new PCPServer");
        try
        {
            this.assc.accept(null, newConnectionsHandler);
            
            while ( true )
            {
                Thread.sleep(1000);
            }
        }
        catch( InterruptedException ex )
        {
            Logger.getLogger(PCPServer.class.getName()).log(Level.SEVERE, "server interrupted!", ex);
        }
    }



    @Override
    public void shutDown()
    {
        try
        {
            this.assc.close();
            this.interrupt();
        }
        catch( IOException ex )
        {
            Logger.getGlobal().log(Level.SEVERE, null, ex);
        }
    }

    
    CompletionHandler<AsynchronousSocketChannel, Void> newConnectionsHandler = 
            new CompletionHandler<AsynchronousSocketChannel, Void>()
            {
                public PCPChannel channel;
                public ByteBuffer bb;
                
                @Override
                public void completed( final AsynchronousSocketChannel result, Void attachment )
                {
                    // allow to answer new connection
                    assc.accept(null, this);
                    
                    // init new PCPChannel
                    channel = new PCPChannel(result, null);
                    channel.setTimeLeftAwake( middleware.getDefaultkeepAlive() );
                        
                    // init new buffer for recieving
                    bb = ByteBuffer.allocate(PCP.Versions.Min.MAX_PACKET_LENGHT());
                        
                    result.read
                    (
                        bb, 
                        null, 
                        new CompletionHandler<Integer, Void>()
                        {
                            @Override
                            public void completed( Integer result, Void attachment )
                            {
                                // allow for new data recieving
                                channel.getChannel().read(bb, attachment, this);
                                
                                // read and accept 
                                byte[] destination = new byte[result];
                                bb.get(destination, 0, result);
                                middleware.accept( destination , channel );
                            }

                            @Override
                            public void failed( Throwable exc, Void attachment )
                            {
                                if ( exc instanceof AsynchronousCloseException)
                                    return;
                                    
                                Logger.getGlobal().log(Level.WARNING,"Error recieving packet",exc);
                                bb = null;
                                channel = null;
                            }
                        });
                    return;
                        
                }

                @Override
                public void failed( Throwable exc, Void attachment )
                {
                    Logger.getGlobal().log(Level.WARNING,"Error recieving new connection",exc);
                    bb = null;
                    channel = null;
                }
            };
    
    
}
