/*
 * this is a school project under "The Unlicence".
 */

package PCP.packets;

import PCP.OpCode;
import java.util.*;

/**
 *
 * @author gfurri20
 * @author Alessio789
 */
public class PCPAliasChangePacket implements IPCPpacket
{
    private byte[] id;
    private String oldAlias;
    private String newAlias;

    public PCPAliasChangePacket( byte[] id, String oldAlias, String newAlias )
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
    public byte[] header()
    {
        byte[] buffer = new byte[this.size()];
        
        int i = 0;
        //Opcode
        buffer[i++] = OpCode.AliasChanghe.getByte();
        //Id
        for(byte b : id)
            buffer[i++] = b;
        
        //OldAlias
        for(byte b : oldAlias.getBytes())
            buffer[i++] = b;
        //Delimitator
        buffer[i++] = 0;

        //NewAlias
        for(byte b : newAlias.getBytes())
            buffer[i++] = b;
        //Delimitator
        buffer[i++] = 0;
        
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
        
        //Add the header to the collection
        out.add(this.header());
        
        return out;
    }
    
}
