/*
 * this is a school project under "The Unlicence".
 */

package PCP.Min.data;

import PCP.data.IPCPData;
import PCP.*;
import java.util.*;

/**
 *
 * @author gfurri20
 * @author Alessio789
 */
public class AliasChange implements IPCPData
{
    private byte[] id;
    private String oldAlias;
    private String newAlias;

    public AliasChange( byte[] id, String oldAlias, String newAlias )
    {
        this.id = id;
        this.oldAlias = oldAlias;
        this.newAlias = newAlias;
    }     
    
    //<editor-fold defaultstate="collapsed" desc="getter and setters">

    public byte[] getId() 
    {
        return id;
    }

    public void setId( byte[] id ) 
    {
        this.id = id;
    }

    public String getOldAlias() 
    {
        return oldAlias;
    }

    public void setOldAlias( String oldAlias ) 
    {
        this.oldAlias = oldAlias;
    }

    public String getNewAlias() 
    {
        return newAlias;
    }

    public void setNewAlias( String newAlias ) 
    {
        this.newAlias = newAlias;
    }
    
    //</editor-fold>

    @Override
    public OpCode getOpCode()
    {
        return OpCode.AliasChanghe;
    }

    @Override
    public PCP.Versions getVersion()
    {
        return PCP.Versions.Min;
    }

    @Override
    public byte[] header()
    {
        byte[] buffer = new byte[3];
        
        int i = 0;
        //Opcode
        buffer[i++] = OpCode.AliasChanghe.getByte();
        //Id
        for(byte b : id)
            buffer[i++] = b;
             
        return buffer;
    }

    @Override
    public int size()
    {
        return 5 + oldAlias.length() + newAlias.length();
    }

    @Override
    public Collection<byte[]> toBytes()
    {
        Collection<byte[]> out = new ArrayList<>();
        byte[] buffer = new byte[this.size()];
        
        int i = 0;
        
        //Adds the header        
        for( byte b : this.header() )
            buffer[i++] = b;

        //Adds the payload
        
        //OldAlias
        for( byte b : oldAlias.getBytes() )
            buffer[i++] = b;
        //Delimitator
        buffer[i++] = 0;

        //NewAlias
        for( byte b : newAlias.getBytes() )
            buffer[i++] = b;
        //Delimitator
        buffer[i++] = 0;
        
        //Push the packege into Collection
        out.add(buffer);
        
        return out;
    }
    
}
