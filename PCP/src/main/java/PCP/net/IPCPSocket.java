/*
 * this is a school project under "The Unlicence".
 */
package PCP.net;

import PCP.*;
import java.net.*;

/**
 *
 * @author JacopoWolf
 */
public interface IPCPSocket
{
    
    Socket getSocket();
    
    
    void setAlias(String alias);
    
    String getAlias();
    
    
    void setId (byte[] id);
    
    byte[] getId();
    
    
    void setVersion( PCP.Versions version );
    
    PCP.Versions getVersion();
    
}
