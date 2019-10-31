/*
 * this is a school project under "The Unlicence".
 */
package PCP.packets;

import PCP.*;
import java.util.*;

/**
 *
 * @author Alessio789
 */
public class PCPRegHack implements IPCPpacket
{
    
    private String assignedId;
    private String alias;

    public PCPRegHack( String assignedId, String alias ) 
    {
        this.assignedId = assignedId;
        this.alias = alias;
    }
    
    //<editor-fold defaultstate="collapsed" desc="getter and setters">

    public String getAssignedId() 
    {
        return assignedId;
    }

    public void setAssignedId( String assignedId ) 
    {
        this.assignedId = assignedId;
    }

    public String getAlias() 
    {
        return alias;
    }

    public void setAlias( String alias ) 
    {
        this.alias = alias;
    }
    
    //</editor-fold>
    
    @Override
    public OpCode getOpCode() 
    {
        return OpCode.RegAck;
    }
    
    @Override
    public int size() 
    {
        return 4 + alias.length();
    }
    
    @Override
    public Collection<byte[]> toBytes() 
    {
        Collection<byte[]> out = new ArrayList<>();
        
        byte[] buffer = new byte[this.size()];
        
        int i = 0;
        
        buffer[i++] = OpCode.RegAck.getByte();
       
        for( byte b : assignedId.getBytes() ) 
            buffer[i++] = b;
        
        for( byte b : alias.getBytes() )
            buffer[i++] = b;
        
        //Delimitator
        buffer[i++] = 0;
        
        out.add(buffer);
        
        return out;
    } 
    
}
