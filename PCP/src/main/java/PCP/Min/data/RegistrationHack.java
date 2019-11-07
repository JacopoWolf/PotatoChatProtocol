/*
 * this is a school project under "The Unlicence".
 */
package PCP.Min.data;

import PCP.*;
import java.util.*;

/**
 *
 * @author Alessio789
 */
public class RegistrationHack implements IPCPdata
{
    
    private byte[] assignedId;
    private String alias;

    public RegistrationHack( byte[] assignedId, String alias ) 
    {
        this.assignedId = assignedId;
        this.alias = alias;
    }
    
    //<editor-fold defaultstate="collapsed" desc="getter and setters">

    public byte[] getAssignedId() 
    {
        return assignedId;
    }

    public void setAssignedId( byte[] assignedId ) 
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
    public PCP.Versions getVersion()
    {
        return PCP.Versions.Min;
    }
    
    @Override
    public OpCode getOpCode() 
    {
        return OpCode.RegAck;
    }

    @Override
    public byte[] header()
    {
        byte[] buffer = new byte[1];
        
        int i = 0;
        //Opcode
        buffer[i++] = OpCode.RegAck.getByte();
                
        return buffer;
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
        //Add the header to the package
        buffer[i++] = this.header()[0];
        
        //Adds the payload to the package
        
        //Assigned id
        for( byte b : assignedId ) 
            buffer[i++] = b;
        
        //Alias chosen
        for( byte b : alias.getBytes() )
            buffer[i++] = b;
        
        //Delimitator
        buffer[i++] = 0;
        
        //Push the package into the collection
        out.add(buffer);
        
        return out;
    } 
    
}
