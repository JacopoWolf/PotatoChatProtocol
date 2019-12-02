/*
 * this is a school project under "The Unlicence".
 */
package PCP.net;

import PCP.logic.*;
import java.io.*;
import java.nio.channels.*;
import java.util.*;

/**
 * a PCP connection.
 * @author JacopoWolf
 */
public interface IPCPChannel 
{  
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
     * @throws java.io.IOException 
     */
    void send( Collection<byte[]> data ) throws IOException;
    
}
