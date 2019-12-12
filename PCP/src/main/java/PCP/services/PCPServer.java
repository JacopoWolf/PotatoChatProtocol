/*
 * this is a school project under "The Unlicence".
 */
package PCP.services;

import PCP.*;
import PCP.logic.*;
import PCP.net.*;
import java.io.*;
import java.net.*;
import java.nio.channels.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.logging.*;


/**
 * A PCP server, serving on the port 53101.
 * Uses {@link Logger#getGlobal()} to log data. 
 * <p>
 * To start the server, simply initalize it and the call {@link PCPServer#acceptAndServe()}.
 * <p>
 * This server is completely asyncronous and multithread; 
 * And normally uses 6 threads (if not specified otherwise with {@link PCPServer#PCPServer(java.net.InetAddress, int, int)} ): <p>
 * 1 for the server main thread, 3 for network I/O, 1 for sorting incoming data, 1 cache cleaning daemon,
 * and 1 main core logic thread.
 * Killing one of those threads might make unstable the server.
 * 
 * @author Jacopo_Wolf
 */
public final class PCPServer extends Thread implements IPCPServer
{
    final PCPManager middleware;
    final AsynchronousServerSocketChannel assc;
    final ExecutorService managerExecutor;
    
    
    //<editor-fold defaultstate="collapsed" desc="getters and setters">
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
//</editor-fold>
    
    
    //<editor-fold defaultstate="collapsed" desc="constructor and finilizers">
    
    /**
     * initialize a new PCP server with default values
     * @param address the address to bind the server to
     * @throws IOException
     */
    public PCPServer ( InetAddress address ) throws IOException
    {
        this(address, 3);
    }
    
    /**
     * initialize a new PCP server
     * @param address the address to bind the server to
     * @param listeningPoolSize number of threads dedicated to listen for incoming data and connections
     * @throws IOException
     */
    public PCPServer( InetAddress address, int listeningPoolSize ) throws IOException
    {
        Logger.getGlobal().info("initializing server...");
        
        // middleware initialization
        this.managerExecutor = Executors.newSingleThreadExecutor();
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
    protected void finalize() throws Throwable
    {
        try
        {
            Logger.getGlobal().warning("server's finalizer has been called");
            this.shutDown();
        }
        finally
        {
            super.finalize();
        }
        
    }
    
//</editor-fold>
    
 
    
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
                        // allows to answer new connection in loop
                        assc.accept(null, this);
                        

                        // initlizes variables
                        IPCPChannel pcpchannel = new PCPChannel(result, null);
                        PCPHandler handler = new PCPHandler(pcpchannel);

                        //calls the read async handler
                        result.read(pcpchannel.getBuffer(), null, handler);

                        // logs info
                        Logger.getGlobal().log(Level.INFO, "successfully recieved new connection");
                            
                      
                    }

                    @Override
                    public void failed( Throwable exc, Void attachment )
                    {
                        Logger.getGlobal().log(Level.WARNING,"error recieving new connection", exc );
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
            Logger.getGlobal().log(Level.INFO, "server info thread interrupted!", ex);
        }
    }
    
    @Override
    public void shutDown()
    {
        try
        {
            Logger.getGlobal().info("server shutting down...");
            // disposes of in-use resources
            this.middleware.dispose();
            // closes socket
            this.assc.close();
            // interrupts working loop.
            this.interrupt();
        }
        catch( IOException ex )
        {
            Logger.getGlobal().log(Level.SEVERE, "error during server shutdown! Resources might not have been correctly disposed", ex);
        }
    }
 
    
    
    private class PCPHandler implements CompletionHandler<Integer, Void>
    {
        final IPCPChannel channel;

        public PCPHandler( IPCPChannel channel )
        {
            this.channel = channel;
        }
        
        
        
        @Override
        public void completed( Integer result, Void attachment )
        {
            if (result < 0) // if reached end of stream
            {
                this.failed(null, attachment);
                return;
            }
            
            // reads the buffer
            byte[] b = channel.getBuffer().array();
            byte[] b1 = Arrays.copyOfRange(b, 0, result);
            
            // schedules next accepting 
            managerExecutor.submit
            (
                () -> middleware.accept( b1 , channel )
            );
            
            channel.getBuffer().clear();
            
            // allow to read next bytes
            channel.getChannel().read(channel.getBuffer(), attachment, this);
        }
        

        private boolean disposed = false;
        @Override
        public void failed( Throwable exc, Void attachment )
        {
            if (disposed)
                return;
            
            middleware.close(channel, null);
            
            disposed = true;
        }
        
    }
    
}