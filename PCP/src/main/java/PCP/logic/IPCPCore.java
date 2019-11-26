/*
 * this is a school project under "The Unlicence".
 */
package PCP.logic;

import PCP.*;
import PCP.data.*;
import PCP.net.*;


/**
 * version specific plug-and-play component for a {@link IPCPLogicCore}
 * @author JacopoWolf
 */
public interface IPCPCore
{
    
    IPCPManager getManager();
    void setManager( IPCPManager manager );
    
    
    /**
     * processes the specified data
     * @param data data to process
     * @param from information of the sending user
     * @throws PCP.PCPException
     */
    void accept ( IPCPData data, IPCPUserInfo from ) throws PCPException;
    
}