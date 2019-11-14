/*
 * this is a school project under "The Unlicence".
 */
package PCP.Min.logic;

import PCP.*;
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
    
    public PCPMinInterpreter()
    {
        this.incompleteDataList = new HashSet<>();
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
            default:
                return null;
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

    
    
}
