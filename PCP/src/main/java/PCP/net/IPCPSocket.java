/*
 * this is a school project under "The Unlicence".
 */
package PCP.net;

import PCP.logic.*;
import java.io.*;
import java.net.*;

/**
 * a PCP connection.
 * @author JacopoWolf
 */
public interface IPCPSocket
{
    /**
     * 
     * @return the wrapped socket, rappresenting a PCP connection 
     */
    Socket getSocket();
    
    /**
     * input connection 
     * @return the {@link BufferedInputStream} of this object.
     */
    BufferedInputStream getBuffInStream();
    
    /**
     * output connection
     * @return the {@link BufferedOutputStream} of this object.
     */
    BufferedOutputStream getBuffOutStream();
    
    
    IPCPUserInfo getUserInfo();
    
    void setUserInfo( IPCPUserInfo info );
    
    
}
