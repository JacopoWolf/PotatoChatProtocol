/*
 * this is a school project under "The Unlicence".
 */
package PCP.Min.logic;

import PCP.Min.data.*;
import PCP.PCPException;
import PCP.PCPException.ErrorCode;
import PCP.data.*;
import PCP.logic.*;
import PCP.net.*;
import java.util.*;
import java.util.stream.*;


/**
 *
 * @author Jacopo_Wolf
 * @author Alessio789
 * @author gfurri20
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
                ArrayList<String> userList = new ArrayList<>();
                for ( IPCPUserInfo user : userCollection ) 
                    userList.add( user.getAlias() );
                GroupUsersList gul = new GroupUsersList( GroupUsersList.UpdateType.complete, userList);
                manager.send( gul, from.getAlias() );
                break;   
            
            case Error:
                ErrorMsg error = ( ErrorMsg ) data;
                if ( ErrorCode.requiresConnectionClose( error.getErrorCode() ) )
                    manager.close( from.getAlias(), null );
                else
                    throw new PCPException( error.getErrorCode() );
                break;
                
            case Registration:
                Registration reg = ( Registration ) data;
                from.setRoom( reg.getTopic() );
                
                ArrayList<String> ul = (ArrayList<String>) this.getAliasesByRoom( from.getRoom() );
                
                RegistrationAck regAck = new RegistrationAck( from.getId(), from.getAlias() );
                GroupUsersList gulR = new GroupUsersList( GroupUsersList.UpdateType.complete, ul);
                manager.send( regAck, from.getAlias() );
                manager.send( gulR, from.getAlias() );   
                break;
            
            case MsgUserToUser:
                MsgUserToUser msgUserToUser = (MsgUserToUser) data;
                MsgRecieved msgRecievedUserToUser = new MsgRecieved
                ( 
                    from.getAlias(),
                    msgUserToUser.getOpCode(),
                    msgUserToUser.getMessage() 
                );
                
                manager.send( msgRecievedUserToUser, msgUserToUser.getDestinationAlias() );
                break;
            
            case MsgUserToGroup:
                MsgUserToGroup msgUserToGroup = (MsgUserToGroup) data;
                MsgRecieved msgRecievedUserToGroup = new MsgRecieved
                (
                    from.getAlias(),
                    msgUserToGroup.getMessage()
                );
                
                ArrayList<String> userArrayList = (ArrayList<String>) this.getAliasesByRoom( from.getRoom() );
                
                manager.sendBroadcast( msgRecievedUserToGroup, userArrayList );
                break;
                
            default:
                throw new PCPException( ErrorCode.Unspecified );
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
    
    private Collection<String> getAliasesByRoom( String roomName )
    {
        Collection<IPCPUserInfo> userInfos = this.getUsersByRoom( roomName );
        ArrayList<String> userArrayList = new ArrayList<>();
        for ( IPCPUserInfo user : userInfos ) 
            userArrayList.add( user.getAlias() );
        
        return userArrayList;
    }
    
}
