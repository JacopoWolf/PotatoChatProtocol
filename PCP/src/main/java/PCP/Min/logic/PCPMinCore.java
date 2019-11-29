/*
 * this is a school project under "The Unlicence".
 */
package PCP.Min.logic;

import PCP.data.*;
import PCP.logic.*;
import PCP.net.*;
import java.util.*;
import java.util.logging.*;
import java.util.stream.*;


/**
 *
 * @author Jacopo_Wolf
 */
class PCPMinCore implements IPCPCore, IMemoryAccess
{
    private IPCPManager manager;

    @Override
    public void accept( IPCPData data, IPCPUserInfo from )
    {
        Logger.getGlobal().info("accepted something. But unfortunately I'm not yet implemented");
        throw new UnsupportedOperationException();
    }

    @Override
    public IPCPManager getManager()
    {
        return manager;
    }

    @Override
    public void setManager( IPCPManager manager )
    {
        this.manager = manager;
    }

    
    @Override
    public Set<String> getRoomNames()
    {
        Set<String> rooms = new HashSet<>();
            rooms.add("general");
            rooms.add( null );
            
        return rooms;
    }

    @Override
    public Collection<IPCPUserInfo> getUsers()
    {
        return this.getManager().allConnectedUsers();
    }

    @Override
    public Collection<IPCPUserInfo> getUsersByRoom( String roomName )
    {
        return getUsers()
                .stream()
                .filter( (inf) -> inf.getRoom().endsWith(roomName) )
                .collect(Collectors.toList());
    }
    
}
