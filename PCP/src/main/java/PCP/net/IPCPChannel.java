/*
 * this is a school project under "The Unlicence".
 */
package PCP.net;

import PCP.logic.*;
import java.nio.channels.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * a PCP connection.
 * @author JacopoWolf
 */
public interface IPCPChannel 
{
    
    /**
     * 
     * @return the referenced executor sevice for this channel.
     */
    ExecutorService getExecutorService();
    
    /**
     * 
     * @return the wrapped socket, rappresenting a PCP connection 
     */
    AsynchronousSocketChannel getChannel();
    
    /**
     * 
     * @return reference to the user infos relative to this connection
     */
    IPCPUserInfo getUserInfo();
    
    /**
     * 
     * @return an integer in milliseconds rappresenting how much time this channel has to be kept alive.
     */
    int getTimeLeftAwake();
    void setTimeLeftAwake(int timeleft);
    
    /**
     * {@link IPCPSocket#getUserInfo() }
     * @param info the new reference to userinfos
     */
    void setUserInfo( IPCPUserInfo info );
    
    
    
    /**
     * send the respective data to this socket
     * @param data 
     */
    void send( Collection<byte[]> data );
    
}
