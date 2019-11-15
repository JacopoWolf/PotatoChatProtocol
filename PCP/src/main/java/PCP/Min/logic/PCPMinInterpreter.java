/*
 * this is a school project under "The Unlicence".
 */
package PCP.Min.logic;

import PCP.*;
import PCP.Min.data.Registration;
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
        
        int i = 0;
        int start = 2;
        
        ArrayList<byte[]> list = new ArrayList<>();
        
        for ( byte b : data ) 
        {
            if ( b == 0 ) 
            {
                list.add(Arrays.copyOfRange(data, start, i));
                start = ++i;
            }
            if ( b == 040 )
                throw new PCPException(ErrorCode.InvalidAlias);
            i++;
        }
        
        if ( list.get(0).length < 6 || list.get(0).length > 32 )
            throw new PCPException(ErrorCode.InvalidAlias);
        
        if ( list.get(1)[0] != 0 && (list.get(1).length < 6 || list.get(1).length > 32))
            throw new PCPException(ErrorCode.InvalidRoomName);
        
        registration.setAlias(new String(list.get(0)));
        registration.setTopic(new String(list.get(1)));
       
        return registration;
    }
}
