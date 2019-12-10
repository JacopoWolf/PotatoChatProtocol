/*
 * this is a school project under "The Unlicence".
 */
package PCP.Min.logic;

import PCP.Min.data.*;
import PCP.*;
import PCP.PCPException.ErrorCode;
import PCP.data.*;
import PCP.logic.*;
import java.nio.charset.StandardCharsets;
import java.util.*;


/**
 *
 * @author gfurri20
 * @author Alessio789
 */
public class PCPMinInterpreter implements IPCPInterpreter
{
    private Set<IPCPData> incompleteDataList;
    
    public PCPMinInterpreter() {};
    
    public PCPMinInterpreter( Set incompleteDataList )
    {
        this.incompleteDataList = incompleteDataList;
    };

    @Override
    public PCP.Versions getVersion()
    {
        return PCP.Versions.Min;
    }

    @Override
    public IPCPData interpret( byte[] data ) throws PCPException
    {
        byte receivedOpcode = data[0];
        
        switch( OpCode.getOpCodeFromByte( receivedOpcode ) )
        {
            case Registration:
                return createRegistrationFromBytes( data );
                
            case Disconnection:
                return createDisconnectionFromBytes( data );
                        
            case AliasChange:
                return createAliasChangeFromBytes( data );
                
            case GroupUsersListRrq:
                return createGroupUserListRrqFromBytes( data );
                
            case MsgUserToGroup:
                return createMsgUserToGroupFromBytes( data );
                
            case MsgUserToUser:
                return createMsgUserToUserFromBytes( data );
                
            case Error:
                return createErrorMsgFromBytes( data );
                
            default:
                throw new PCPException( ErrorCode.PackageMalformed );
        }
    }

    @Override
    public Set<IPCPData> getIncompleteDataList()
    {
        return this.incompleteDataList;
    }

    @Override
    public void setIncompleteDataList( Set<IPCPData> incompleteDataList )
    {
        this.incompleteDataList = incompleteDataList;
    }
    
    private void addIncompleteData ( IPCPData incompleteData ) 
    {
        synchronized ( incompleteDataList )
        {
            this.incompleteDataList.add(incompleteData);
        }
    }
    
    private Registration createRegistrationFromBytes( byte[] data ) throws PCPException 
    {
        
        Registration registration = new Registration( null, null );
        
        int start = 2;
        
        ArrayList<byte[]> variableElements = new ArrayList<>();
        
        
        for ( int i = 2; i < data.length; i++ ) 
        {
            if ( data[i] == 0 ) 
            {
                variableElements.add( Arrays.copyOfRange( data, start, i ) );
                start = i + 1; 
            }
            
            if ( data[i] == 040 ) 
                throw new PCPException( ErrorCode.InvalidAlias );
        }
        
        
        if ( variableElements.get(0).length < 6 || variableElements.get(0).length > 32 )
            throw new PCPException( ErrorCode.InvalidAlias );
        
        if ( variableElements.get(1).length != 0 && ( variableElements.get(1).length < 6 || variableElements.get(1).length > 32 ) )
            throw new PCPException( ErrorCode.InvalidRoomName );
        
        registration.setAlias( new String( variableElements.get( 0 ), StandardCharsets.ISO_8859_1 ) );
        
        if ( variableElements.get(1).length != 0 )
            registration.setTopic( new String( variableElements.get( 1 ) , StandardCharsets.ISO_8859_1));
       
        return registration;
    }
    
    private Disconnection createDisconnectionFromBytes ( byte[] data ) throws PCPException
    {

        if ( data.length != 3 ) 
            throw new PCPException( ErrorCode.PackageMalformed );
        
        byte[] id = Arrays.copyOfRange( data, 1, data.length );
        
        Disconnection disconnection = new Disconnection( id );
        
        return disconnection;
    }
    
    private AliasChange createAliasChangeFromBytes ( byte[] data ) throws PCPException
    {
       AliasChange aliasChange = new AliasChange( null, null, null );
       ArrayList<byte[]> aliasList = new ArrayList<>();
       
       byte[] id = new byte[2];
       id[0] = data[1];
       id[1] = data[2];
       
       aliasChange.setId(id);
       
       int start = 3;
      
       for ( int i = 3; i < data.length; i++ ) 
       {
           if ( data[i] == 0 ) 
           {
               aliasList.add( Arrays.copyOfRange( data, start, i ) );
               start = i + 1;
           }
           if ( data[i] == 040 )
                throw new PCPException( ErrorCode.InvalidAlias );
       }
       
       for ( byte[] b : aliasList ) 
       {
           if ( b.length < 6 || b.length > 32 ) 
               throw new PCPException( ErrorCode.InvalidAlias );
       }
       
       aliasChange.setOldAlias( new String ( aliasList.get( 0 ), StandardCharsets.ISO_8859_1 ) );
       aliasChange.setNewAlias( new String ( aliasList.get( 1 ), StandardCharsets.ISO_8859_1 ) );
       
       return aliasChange;
    }
    
    private GroupUsersListRrq createGroupUserListRrqFromBytes( byte[] data ) throws PCPException 
    {
        if ( data.length != 3 )
            throw new PCPException( ErrorCode.PackageMalformed );
        
        GroupUsersListRrq groupUserListRrq = new GroupUsersListRrq( null );
        
        byte[] id = new byte[2];
        id[0] = data[1];
        id[1] = data[2];
        
        groupUserListRrq.setSenderId(id);
        
        return groupUserListRrq;
    }
    
    private MsgUserToGroup createMsgUserToGroupFromBytes ( byte[] data ) throws PCPException
    {
        if ( data.length > this.getVersion().MAX_PACKET_LENGHT )
            throw new PCPException( ErrorCode.PackageMalformed );
        
        MsgUserToGroup msgUserToGroup = new MsgUserToGroup( null, null ); 
        
        byte[] id = Arrays.copyOfRange( data, 1, 3 );
            
        byte[] message = Arrays.copyOfRange( data, 3, data.length - 1 );
        
        msgUserToGroup.setSenderId(id);
        msgUserToGroup.setMessage( new String( message, StandardCharsets.ISO_8859_1 ) );
       
        Optional<IPCPData> incompletePackets = this.getIncompleteDataList().stream()
            .filter( incompleteData -> 
            {
                if ( incompleteData.getClass().isInstance( msgUserToGroup ) ) 
                {
                    MsgUserToGroup incompleteMsgUserToGroup = ( MsgUserToGroup ) incompleteData;
                    if ( Arrays.equals( msgUserToGroup.getSenderId() , incompleteMsgUserToGroup.getSenderId() ) )
                        return true;
                }
                return false;
            } ).findFirst(); 
        
        if ( incompletePackets.isPresent() )  
        { 
            MsgUserToGroup incompleteMsgUserToGroup = ( MsgUserToGroup ) incompletePackets.get();
            String completeMessage = incompleteMsgUserToGroup.getMessage() + msgUserToGroup.getMessage();
            incompleteMsgUserToGroup.setMessage( completeMessage );
            if ( data.length < this.getVersion().MAX_PACKET_LENGHT )
                return incompleteMsgUserToGroup;
        } 
        else if ( data.length == this.getVersion().MAX_PACKET_LENGHT )
        {
            this.addIncompleteData( msgUserToGroup );
        }
        else
        {
            return msgUserToGroup;
        }
        
        return null;
    }
    
    private MsgUserToUser createMsgUserToUserFromBytes ( byte[] data ) throws PCPException
    {
        if ( data.length > this.getVersion().MAX_PACKET_LENGHT )
            throw new PCPException( ErrorCode.PackageMalformed );
        
        MsgUserToUser msgUserToUser = new MsgUserToUser( null, null, null ); // src id , dst alias , message
        
        byte[] id = Arrays.copyOfRange(data, 1, 3);
        
        ArrayList<byte[]> variableElements = new ArrayList<>();
        
        int start = 3;
        for ( int i = 3; i < data.length; i++ )
        {
            if ( data[i] == 0 )
            {
                variableElements.add(Arrays.copyOfRange(data, start, i));
                start = i + 1;
            }            
        }
        
        // check if destination alias is valid
        for ( byte b : variableElements.get(0) )
        {
            if ( b == 040 )
                throw new PCPException( ErrorCode.InvalidAlias );
        }
        if ( variableElements.get(0).length < 6 || variableElements.get(0).length > 32 )
            throw new PCPException( ErrorCode.InvalidAlias );
        
        msgUserToUser.setSenderId( id );
        msgUserToUser.setDestinationAlias( new String( variableElements.get( 0 ), StandardCharsets.ISO_8859_1 ) );
        msgUserToUser.setMessage( new String( variableElements.get( 1 ), StandardCharsets.ISO_8859_1 ) );
        
        Optional<IPCPData> incompletePackets = this.getIncompleteDataList().stream()
            .filter( incompleteData -> 
            {
                if ( incompleteData.getClass().isInstance( msgUserToUser ) ) 
                {
                    MsgUserToUser incompleteMsgUserToUser= ( MsgUserToUser ) incompleteData;
                    if ( Arrays.equals(msgUserToUser.getSenderId(), incompleteMsgUserToUser.getSenderId() ) )
                        return true;
                }
                return false;
            } ).findFirst();
        
        if ( incompletePackets.isPresent() )  
        { 
            MsgUserToUser incompleteMsgUserToUser = ( MsgUserToUser ) incompletePackets.get();
            String completeMessage = incompleteMsgUserToUser.getMessage() + msgUserToUser.getMessage();
            incompleteMsgUserToUser.setMessage( completeMessage );
            if ( data.length < this.getVersion().MAX_PACKET_LENGHT )
                return incompleteMsgUserToUser;
        } 
        else if ( data.length == this.getVersion().MAX_PACKET_LENGHT )
        {
            this.addIncompleteData( msgUserToUser );
        }
        else
        {
            return msgUserToUser;
        }
        
        return null;
    }
    
    private ErrorMsg createErrorMsgFromBytes( byte[] data ) throws PCPException
    {
        if ( data.length > 2 )
            throw new PCPException( ErrorCode.PackageMalformed );
        
        byte errorCode = data[1];
             
        ErrorMsg errorMsg = new ErrorMsg( ErrorCode.getErrorCodeFromByte(errorCode) );
        
        return errorMsg;
    }
}
