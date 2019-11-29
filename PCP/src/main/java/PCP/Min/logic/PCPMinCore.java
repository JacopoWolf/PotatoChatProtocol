/*
 * this is a school project under "The Unlicence".
 */
package PCP.Min.logic;

import PCP.Min.data.*;
import PCP.*;
import PCP.PCPException.ErrorCode;
import PCP.data.*;
import PCP.logic.*;
import PCP.net.*;
import java.util.*;
import java.util.stream.*;


/**
 * @author Jacopo_Wolf
 * @author Alessio789
 * @author gfurri20
 */
public class PCPMinCore implements IPCPCore, IMemoryAccess
{
    private IPCPManager manager;
    
    //<editor-fold defaultstate="collapsed" desc="getters and setters">
    
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
//</editor-fold>
    
    
    @Override
    public void accept( IPCPData data, IPCPUserInfo from ) throws PCPException
    {
        switch ( data.getOpCode() ) 
        {
            
        // messages
            case MsgUserToUser:
            {
                MsgUserToUser msg = (MsgUserToUser) data;
                MsgRecieved msgRecievedUserToUser = new MsgRecieved
                    ( 
                        from.getAlias(),
                        msg.getOpCode(),
                        msg.getMessage() 
                    );
                
                manager.send( msgRecievedUserToUser, msg.getDestinationAlias() );
                break;
            }
            
            case MsgUserToGroup:
            {
                MsgUserToGroup msg = (MsgUserToGroup) data;
                MsgRecieved msgRecievedUserToGroup = new MsgRecieved
                    (
                        from.getAlias(),
                        msg.getMessage()
                    );
                
                manager.sendBroadcast
                    ( 
                        msgRecievedUserToGroup, 
                        this.getAliasesByRoom( from.getRoom()) 
                    );
                break;
            }
            
                
        // user status
            case Registration:
            {
                Registration reg = ( Registration ) data;
                from.setRoom( reg.getTopic() );
                
                manager.send
                ( 
                    new RegistrationAck( from.getId(), from.getAlias() ), 
                    from.getAlias() 
                );
                
                ArrayList<String> ul = new ArrayList<>(this.getAliasesByRoom( from.getRoom() ));
                manager.send
                ( 
                    new GroupUsersList( GroupUsersList.UpdateType.complete, ul), 
                    from.getAlias() 
                );
                break;
            }   
            
            case Disconnection:
            {
                manager.close( from.getAlias(), null );
                break;
            }   
            
            case AliasChange:
            {
                AliasChange ac = (AliasChange) data;
                from.setAlias( ac.getNewAlias() );
                break;
            }
                
            
        // control messages
            case GroupUsersListRrq:     
            {
                GroupUserListRrq gulr = (GroupUserListRrq)data;
                
                
                GroupUsersList gul = new GroupUsersList
                ( 
                    GroupUsersList.UpdateType.complete,
                    new ArrayList<> ( this.getAliasesByRoom("general") )
                );
                
                manager.send( gul, from.getAlias() );
                break;   
            }
            
        // error
            case Error:
            {
                ErrorMsg error = ( ErrorMsg ) data;
                if ( ErrorCode.requiresConnectionClose( error.getErrorCode() ) )
                    manager.close( from.getAlias(), null );
                else
                    throw new PCPException( error.getErrorCode() );
                break;
            }
                
            
                
            default:
                throw new PCPException( ErrorCode.Unspecified );
        }
    }

    

    //<editor-fold defaultstate="collapsed" desc="IMemoryManager">
    
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
    
    /**
     * 
     * @param roomName
     * @return a collection of the aliases in the specified room
     */
    public Collection<String> getAliasesByRoom(String roomName)
    {
        return getUsers()
                .stream()
                .filter( (inf) -> inf.getRoom().endsWith(roomName) )
                .map( IPCPUserInfo::getAlias )
                .collect( Collectors.toList() );
    }
    
//</editor-fold>
    
}
