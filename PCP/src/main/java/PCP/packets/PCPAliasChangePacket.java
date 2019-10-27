/*
 * this is a school project under "The Unlicence".
 */

package PCP.packets;

import PCP.OpCode;
import java.util.*;

/**
 *
 * @author gfurri20
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

    @Override
    public OpCode getOpCode()
    {
        return OpCode.AliasChanghe;
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
        
        out.add(buffer);
        
        return out;
    }
    
}
