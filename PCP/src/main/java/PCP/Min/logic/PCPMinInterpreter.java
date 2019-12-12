/*
 * this is a school project under "The Unlicence".
 */
package PCP.Min.logic;

import PCP.*;
import PCP.Min.data.*;
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
        
        //position of the first byte of alias
        int start = 2;
        
        //ArrayList which will contain alias and topic
        ArrayList<byte[]> variableElements = new ArrayList<>();
        
        
        for ( int i = 2; i < data.length; i++ ) 
        {
            if ( data[i] == 0 ) //if the byte is the delimitator (0) of a variable element
            {
                //it copies the part of the packet from the starting position to the current position (excluded)
                variableElements.add( 
                        Arrays.copyOfRange( data, start, i ) ); 
                
                start = i + 1; //the next position will be the starting position of the next variable element
            }
            
            //if the byte is a space
            if ( data[i] == 040 ) 
                throw new PCPException( ErrorCode.InvalidAlias );
        }
        
        //if the alias is invalid
        if ( variableElements.get(0).length < 6 || variableElements.get(0).length > 32 )
            throw new PCPException( ErrorCode.InvalidAlias );
        
        
        //if the topic is present and invalid
        if ( variableElements.get(1).length != 0 && ( variableElements.get(1).length < 3 || variableElements.get(1).length > 64 ) )
            throw new PCPException( ErrorCode.InvalidRoomName );
        
        registration.setAlias( new String( variableElements.get( 0 ), StandardCharsets.ISO_8859_1 ) );
        
        //if the topic isn't null
        if ( variableElements.get(1).length != 0 )
            registration.setTopic( new String( variableElements.get( 1 ) , StandardCharsets.ISO_8859_1));
       
        return registration;
    }
    
    private Disconnection createDisconnectionFromBytes ( byte[] data ) throws PCPException
    {

        if ( data.length != 3 ) 
            throw new PCPException( ErrorCode.PackageMalformed );
        
        //Copy of data from position 1 to the end (the id)
        byte[] id = Arrays.copyOfRange( data, 1, data.length );
        
        Disconnection disconnection = new Disconnection( id );
        
        return disconnection;
    }
    
    private AliasChange createAliasChangeFromBytes ( byte[] data ) throws PCPException
    {
       AliasChange aliasChange = new AliasChange( null, null, null );
       
       //ArrayList which will contain the two alias 
       ArrayList<byte[]> aliasList = new ArrayList<>();
       
       byte[] id = new byte[2];
       id[0] = data[1];
       id[1] = data[2];
       
       aliasChange.setId(id);
       
       //the starting position of the old alias
       int start = 3;
      
       for ( int i = 3; i < data.length; i++ ) 
       {
           if ( data[i] == 0 ) //if the byte is the delimitator (0) of a variable element
           {
               //it copies the part of the packet from the starting position to the current position (excluded)
               aliasList.add( Arrays.copyOfRange( data, start, i ) );
               
               //the next position will be the starting position of the next variable element
               start = i + 1;
           }
           
           //if the byte is a space
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
        
        //Copy of data from position 1 to 3 (excluded)
        byte[] id = Arrays.copyOfRange( data, 1, 3 );
        
        //Copy of data from position 3 to penultimate (byte at 0, excluded)
        byte[] message = Arrays.copyOfRange( data, 3, data.length - 1 );
        
        msgUserToGroup.setSenderId(id);
        msgUserToGroup.setMessage( new String( message, StandardCharsets.ISO_8859_1 ) );
       
        Optional<IPCPData> incompletePackets = this.getIncompleteDataList().stream()
            .filter( incompleteData -> 
            {
                //if it is a MsgUserToGroup
                if ( incompleteData.getClass().isInstance( msgUserToGroup ) ) 
                {
                    MsgUserToGroup incompleteMsgUserToGroup = ( MsgUserToGroup ) incompleteData;
                    
                    //if they are sended from the same user
                    if ( Arrays.equals( msgUserToGroup.getSenderId(), incompleteMsgUserToGroup.getSenderId() ) )
                        return true;
                }
                return false;
            } ).findFirst(); 
        
        //if there is a precedent packet of 2048 byte
        if ( incompletePackets.isPresent() )  
        { 
            MsgUserToGroup incompleteMsgUserToGroup = ( MsgUserToGroup ) incompletePackets.get();
            
            //merges the messages
            String completeMessage = incompleteMsgUserToGroup.getMessage() + msgUserToGroup.getMessage();
            incompleteMsgUserToGroup.setMessage( completeMessage );
            
            //if data is the last packet, return the complete message
            if ( data.length < this.getVersion().MAX_PACKET_LENGHT )
                return incompleteMsgUserToGroup;
        } 
        
        //if it's the first packet 2048 byte length
        else if ( data.length == this.getVersion().MAX_PACKET_LENGHT )
        {
            this.addIncompleteData( msgUserToGroup );
        }
        
        //it's a simple packet <2048 byte length
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
        
        //Copy of data from position 1 to 3 (excluded)
        byte[] id = Arrays.copyOfRange(data, 1, 3);
        
        //ArrayList which will contain the two alias 
        ArrayList<byte[]> variableElements = new ArrayList<>();
        
        //the starting position of the first alias
        int start = 3;
        
        for ( int i = 3; i < data.length; i++ )
        {
            if ( data[i] == 0 ) //if the byte is the delimitator (0) of a variable element
            {
                //it copies the part of the packet from the starting position to the current position (excluded)
                variableElements.add(Arrays.copyOfRange(data, start, i));
                
                //the next position will be the starting position of the next variable element
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
                //if it is a MsgUserToUser
                if ( incompleteData.getClass().isInstance( msgUserToUser ) ) 
                {
                    MsgUserToUser incompleteMsgUserToUser= ( MsgUserToUser ) incompleteData;
                    
                    //if they are sended from the same user
                    if ( Arrays.equals(msgUserToUser.getSenderId(), incompleteMsgUserToUser.getSenderId() ) )
                        return true;
                }
                return false;
            } ).findFirst();
        
        //if there is a precedent packet of 2048 byte
        if ( incompletePackets.isPresent() )  
        { 
            MsgUserToUser incompleteMsgUserToUser = ( MsgUserToUser ) incompletePackets.get();
            
            //merges the messages
            String completeMessage = incompleteMsgUserToUser.getMessage() + msgUserToUser.getMessage();
            incompleteMsgUserToUser.setMessage( completeMessage );
            
            //if data is the last packet, return the complete message
            if ( data.length < this.getVersion().MAX_PACKET_LENGHT )
                return incompleteMsgUserToUser;
        } 
        
        //if it's the first packet 2048 byte length
        else if ( data.length == this.getVersion().MAX_PACKET_LENGHT )
        {
            this.addIncompleteData( msgUserToUser );
        }
        
        //it's a simple packet < 2048 byte length
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
