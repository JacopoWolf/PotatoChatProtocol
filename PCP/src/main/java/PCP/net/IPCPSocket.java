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
    
    String getAlias();
    
    byte[] getId();
    
    PCP.Versions getVersion();
    
}
