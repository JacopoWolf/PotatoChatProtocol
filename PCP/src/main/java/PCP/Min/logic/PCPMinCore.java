/*
 * this is a school project under "The Unlicence".
 */
package PCP.Min.logic;

import PCP.Min.data.*;
import PCP.PCPException;
import PCP.data.*;
import PCP.logic.*;
import PCP.net.*;
import java.util.*;
import java.util.stream.*;


/**
 *
 * @author Jacopo_Wolf
 */
class PCPMinCore implements IPCPCore, IMemoryAccess
{
    private IPCPManager manager;

    @Override
    public void accept( IPCPData data, IPCPUserInfo from ) throws PCPException
    {
        
        switch ( data.getOpCode() ) 
        {
            case AliasChange:
                AliasChange ac = (AliasChange) data;
                from.setAlias( ac.getNewAlias() );
                break;
                
            case Disconnection:
                manager.close( from.getAlias(), null );
                break;
                
            case GroupUsersListRrq:
                Collection<IPCPUserInfo> userCollection = this.getUsersByRoom( from.getRoom() );
                GroupUsersList groupUsersList = new GroupUsersList( GroupUsersList.UpdateType.complete, null);
                manager.send( groupUsersList, from.getAlias() );
                break;   
            
            case Error:
                ErrorMsg error = ( ErrorMsg ) data;
                if ( PCPException.ErrorCode.requiresConnectionClose( error.getErrorCode() ) )
                    manager.close( from.getAlias(), null );
                else
                    throw new PCP.PCPException( error.getErrorCode() );
                
            case Registration:
                RegistrationAck registrationAck = new RegistrationAck( from.getId(), from.getAlias() );
                GroupUsersList groupUsersListReg = new GroupUsersList( GroupUsersList.UpdateType.complete, null);
                
        }
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
