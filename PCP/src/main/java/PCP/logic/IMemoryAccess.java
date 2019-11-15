/*
 * this is a school project under "The Unlicence".
 */
package PCP.logic;

import java.util.*;


/**
 * interface to allow memory access
 * @author Jacopo_Wolf
 */
public interface IMemoryAccess
{
    /**
     * 
     * @return a set of all the current rooms
     */
    public Set<String> getRoomNames();
    
    /**
     * 
     * @return a set of every active users
     */
    public Set<IPCPUserInfo> getUsers();
    
    /**
     * @param roomName name of the room to filter. 
     * A null value means all users not connected to a room.
     * @return all the users in the specified room.
     */
    public Collection<IPCPUserInfo> getUsersByRoom(String roomName);
    
    
    
}
