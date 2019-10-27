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
public class PCPDisconnectionPacket implements IPCPpacket
{
    private String id;
    private int reason;
    private boolean byClient; //It's true if the disconnection has been required by the client

    public PCPDisconnectionPacket( String id ) 
    {
        this.id = id;
        this.byClient = true;
    }

    public PCPDisconnectionPacket( int reason ) 
    {
        this.reason = reason;
        this.byClient = false;
    }

    @Override
    public OpCode getOpCode() 
    {
        return OpCode.Disconnection;
    }

    @Override
    public int size() 
    {
        if( byClient ) 
            return 3;
        else 
            return 2;
    }

    @Override
    public Collection<byte[]> toBytes() 
    {
        Collection<byte[]> out = new ArrayList<>();
       
        byte[] buffer = new byte[this.size()];
        
        int i = 0;
        
        buffer[i++] = OpCode.Disconnection.getByte();
        
        if( byClient ) 
        {
            for(byte b : id.getBytes()) 
                buffer[i++] = b;
        }
        else
            buffer[i++] = (byte) this.reason;
        
        out.add(buffer);
        
        return out;
    }
    
     
}
