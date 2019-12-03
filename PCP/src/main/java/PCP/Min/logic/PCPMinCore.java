/*
 * this is a school project under "The Unlicence".
 */
package PCP.Min.logic;

import PCP.Min.data.*;
import PCP.*;
import PCP.Min.data.*;
import PCP.PCPException.ErrorCode;
import PCP.data.*;
import PCP.logic.*;
import PCP.net.*;
import java.util.*;
import java.util.logging.*;
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
                MsgRecieved msg_tosend = new MsgRecieved
                    ( 
                        from.getAlias(),
                        msg.getOpCode(),
                        msg.getMessage() 
                    );
                
                
                // send the message to a single user
                manager.send( msg_tosend, msg.getDestinationAlias() );
                
                // logs information about the message
                Logger.getGlobal().log(Level.INFO, "MsgUserToUser received from {0} -- send to {1}", new Object[]{from.toString(), msg.getDestinationAlias()});
                Logger.getGlobal().log(Level.FINE, "Content of the message:\n{0}", msg_tosend.getMessage());
                
                break;
            }
            
            case MsgUserToGroup:
            {
                MsgUserToGroup msg = (MsgUserToGroup) data;
                MsgRecieved msg_tosend = new MsgRecieved
                    (
                        from.getAlias(),
                        msg.getMessage()
                    );
                
                // send the message to every user in a specific room
                manager.sendBroadcast( msg_tosend, this.getAliasesByRoom( from.getRoom() ) );
                
                // logs informations about the message
                Logger.getGlobal().log(Level.INFO, "MsgUserToGroup received from {0} -- send broadcast", from.toString());
                Logger.getGlobal().log(Level.FINE, "Content of the message:\n{0}", msg_tosend.getMessage());
                
                break;
            }
            
                
        // user status
            // regard change rooms
            case Registration:
            {
                Registration reg = ( Registration ) data;
                from.setRoom( reg.getTopic() );
                
                // send the registration ack to the user
                manager.send
                ( 
                    new RegistrationAck( from.getId(), from.getAlias() ), 
                    from.getAlias() 
                );
                
                // all user in the room
                ArrayList<String> ul = new ArrayList<>(this.getAliasesByRoom( from.getRoom() ));
                // send the complete user list
                manager.send
                ( 
                    new GroupUsersList( GroupUsersList.UpdateType.complete, ul), 
                    from.getAlias() 
                );

                // send to everyone the new user
                manager.sendBroadcast
                (
                    new GroupUsersList( GroupUsersList.UpdateType.joined, from.getAlias() ),
                    ul
                );
                
                // logs informations about the message
                Logger.getGlobal().log(Level.INFO, "Registration received from {0}", from.toString());
                Logger.getGlobal().log(Level.INFO, "Registration Ack (id assigned: {0} - new room: {1}) sended to {2}", new Object[]{from.getId(), from.toString(), from.getRoom()});
                Logger.getGlobal().log(Level.INFO, "GroupUserList of all user sended to {0}", from.getAlias());
                Logger.getGlobal().log(Level.INFO, "GroupUserList update sended in broadcast");

                
                break;
            }   
            
            case Disconnection:
            {
                // close the connection
                manager.close( from.getAlias(), null );
                
                // all user in the room
                ArrayList<String> ul = new ArrayList<>(this.getAliasesByRoom( from.getRoom() ));
                
                // send to everyone the disconnected user
                manager.sendBroadcast
                (
                    new GroupUsersList( GroupUsersList.UpdateType.disconnected, from.getAlias() ),
                    ul
                );
                
                // logs informations about the disconnection
                Logger.getGlobal().log(Level.INFO, "Connection with {0} closed", from.toString());
                Logger.getGlobal().log(Level.INFO, "GroupUserList update sended in broadcast");
                
                break;
            }   
            
            case AliasChange:
            {
                String oldAlias = from.getAlias();
                manager.sendBroadcast(
                        new GroupUsersList( GroupUsersList.UpdateType.disconnected, oldAlias ), 
                        this.getAliasesByRoom( from.getRoom()) );
                AliasChange ac = (AliasChange) data;
                from.setAlias( ac.getNewAlias() );
                manager.sendBroadcast(
                        new GroupUsersList( GroupUsersList.UpdateType.joined, from.getAlias() ),
                        this.getAliasesByRoom( from.getRoom() ) );
                Logger.getGlobal().log( Level.INFO, "AliasChange packet received from {0}", from.toString() );
                Logger.getGlobal().log( Level.INFO, "User {0} changed alias from {1} to {2}", new Object[]{from.toString(), oldAlias, from.getAlias()} );
                Logger.getGlobal().log( Level.INFO, "GroupUsersList disconnected type sended with alias {0}", oldAlias);
                Logger.getGlobal().log( Level.INFO, "GroupUsersList joined type sended with alias {0}", from.getAlias() );
                break;
            }
                
            
        // control messages
            case GroupUsersListRrq:     
            {
                GroupUserListRrq gulr = (GroupUserListRrq) data;
                
                GroupUsersList gul = new GroupUsersList
                ( 
                    GroupUsersList.UpdateType.complete,
                    new ArrayList<> ( this.getAliasesByRoom( "general" ) ) // for now "general"
                );
                
                // send the complete user list
                manager.send( gul, from.getAlias() );
                
                // log information about the complete user list
                Logger.getGlobal().log(Level.INFO, "GroupUserListRrq received from {0}", from.toString());
                Logger.getGlobal().log(Level.INFO, "Complete GroupUserList sended to {0}", from.toString());
                
                break;   
            }
            
        // error
            case Error:
            {
                ErrorMsg error = ( ErrorMsg ) data;
                Logger.getGlobal().log( Level.WARNING, "Error packet received from {0}", from.toString() );
                if ( ErrorCode.requiresConnectionClose( error.getErrorCode() ) ) 
                {
                    manager.close( from.getAlias(), null );
                }
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
