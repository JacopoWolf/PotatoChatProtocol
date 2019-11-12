/*
 * this is a school project under "The Unlicence".
 */
package PCP.net;

import PCP.*;
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
    
    
    /**
     * {@link IPCPSocket#getAlias() }
     * @param alias the new 
     */
    void setAlias(String alias);
    
    /**
     * 
     * @return the alias associated whith this connection
     */
    String getAlias();
    
    
    /**
     * 
     * @param id the new id
     */
    void setId (byte[] id);
    
    /**
     * 
     * @return the id associated with this connection
     */
    byte[] getId();
    
    
    /**
     * sets the version of this connection
     * @param version the new version
     */
    void setVersion( PCP.Versions version );
    
    /**
     * 
     * @return the version of the PCP protocol associated with this connection
     */
    PCP.Versions getVersion();
    
}
