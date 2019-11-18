/*
 * this is a school project under "The Unlicence".
 */
package PCP.services;

import PCP.*;
import PCP.logic.*;
import PCP.net.*;
import java.io.*;
import java.nio.channels.*;


/**
 * base interface for IPCPServers.
 * @author JacopoWolf
 */
public interface IPCPServer
{
    /**
     * 
     * @return the module used to access the memory layer
     */
    IMemoryAccess getMemoryAccess();
    
    /**
     * 
     * @return middlewere used to mange workload
     */
    IPCPManager getManager();
    
    /**
     * 
     * @return the channel used to recieve new connection
     */
    AsynchronousServerSocketChannel getAsyncServerSktChnl();
    
    /**
     * initialize all of the useful components and acceptAndServe listening
     * @throws IOException usually thrown when the port is already occupied
     */
    public void acceptAndServe() throws IOException;
    /**
     * {@link IPCPServer#acceptAndServe() } but initialized and keeps alive at least a thread to manage 
     * connections incoming from the specified version of the protocol
     * @param startWith
     * @throws IOException 
     */
    public void acceptAndServe( PCP.Versions startWith ) throws IOException;
    
    /**
     * safely shut down the server, closing all connections and saving all necessary data.
     */
    public void shutDown();
    
    
}
