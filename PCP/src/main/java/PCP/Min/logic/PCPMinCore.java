/*
 * this is a school project under "The Unlicence".
 */
package PCP.Min.logic;

import PCP.data.*;
import PCP.logic.*;
import PCP.net.*;
import java.util.*;
import java.util.logging.*;


/**
 *
 * @author Jacopo_Wolf
 */
class PCPMinCore implements IPCPCore, IMemoryAccess
{

    @Override
    public void accept( IPCPData data, IPCPUserInfo from )
    {
        Logger.getGlobal().info("accepted something. But unfortunately I'm not yet implemented");
        throw new UnsupportedOperationException();
    }

    @Override
    public IPCPManager getManager()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setManager( IPCPManager manager )
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<String> getRoomNames()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<IPCPUserInfo> getUsers()
    {
        return this.getManager().allConnectedUsers();
    }

    @Override
    public Collection<IPCPUserInfo> getUsersByRoom( String roomName )
    {
        throw new UnsupportedOperationException();
    }
    
}
