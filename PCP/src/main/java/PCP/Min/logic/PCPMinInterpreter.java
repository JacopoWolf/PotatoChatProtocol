/*
 * this is a school project under "The Unlicence".
 */
package PCP.Min.logic;

import PCP.*;
import PCP.Min.data.*;
import PCP.PCPException.ErrorCode;
import PCP.data.*;
import PCP.logic.*;
import java.util.*;


/**
 *
 * @author gfurri20
 * @author Alessio789
 */
public class PCPMinInterpreter implements IPCPInterpreter
{
    private Set<IPCPData> incompleteDataList;
    
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
        
        switch( OpCode.getOpCodeFromByte(receivedOpcode) )
        {
            case Registration:
                return createRegistrationFromBytes(data);
                
            case Disconnection:
                return createDisconnectionFromBytes(data);
                        
            case AliasChange:
                return createAliasChangeFomBytes(data);
                
            case GroupUsersListRrq:
                return createGroupUserListRrqFromBytes(data);
                
            default:
                throw new PCPException(ErrorCode.PackageMalformed);
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
        
        Registration registration = new Registration(null, null);
        int start = 2;
        
        ArrayList<byte[]> list = new ArrayList<>();
        for ( int i = 2; i < data.length; i++) 
        {
            if ( data[i] == 0 ) 
            {
                list.add(Arrays.copyOfRange(data, start, i));
                start = i + 1;
            }
            if ( data[i] == 040 )
                throw new PCPException(ErrorCode.InvalidAlias);
        }
        
        if ( list.get(0).length < 6 || list.get(0).length > 32 )
            throw new PCPException(ErrorCode.InvalidAlias);
        
        if ( list.get(1)[0] != 0 && (list.get(1).length < 6 || list.get(1).length > 32))
            throw new PCPException(ErrorCode.InvalidRoomName);
        
        registration.setAlias(new String(list.get(0)));
        registration.setTopic(new String(list.get(1)));
       
        return registration;
    }
    
    private Disconnection createDisconnectionFromBytes ( byte[] data ) throws PCPException
    {

        if ( data.length > 3 ) 
            throw new PCPException(ErrorCode.PackageMalformed);
        
        byte[] id = Arrays.copyOfRange(data, 1, data.length);
        
        Disconnection disconnection = new Disconnection(id);
        
        return disconnection;
    }
    
    private AliasChange createAliasChangeFomBytes ( byte[] data) throws PCPException
    {
       AliasChange aliasChange = new AliasChange(null, null, null);
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
               aliasList.add(Arrays.copyOfRange(data, start, i));
               start = i + 1;
           }
           if ( data[i] == 040 )
                throw new PCPException(ErrorCode.InvalidAlias);
       }
       
       for ( byte[] b : aliasList ) 
       {
           if (b.length < 6 || b.length > 32) 
               throw new PCPException(ErrorCode.InvalidAlias);
       }
       
       return aliasChange;
    }
    
    private GroupUserListRrq createGroupUserListRrqFromBytes( byte[] data ) 
    {
        GroupUserListRrq groupUserListRrq = new GroupUserListRrq(null);
        
        byte[] id = new byte[2];
        id[0] = data[1];
        id[1] = data[2];
        
        groupUserListRrq.setSenderId(id);
        
        return groupUserListRrq;
    }
}
