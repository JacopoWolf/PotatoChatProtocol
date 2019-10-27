/*
 * this is a school project under "The Unlicence".
 */
package PCP.packets;

import PCP.*;
import PCP.errors.*;
import java.util.*;

/**
 *
 * @author Jacopo_Wolf
 */
public class PCPErrorPacket implements IPCPpacket
{
    private ErrorCode errorCode;

    public ErrorCode getErrorCode()
    {
        return errorCode;
    }

    public PCPErrorPacket( ErrorCode errorCode )
    {
        this.errorCode = errorCode;
    }
    
    
    @Override
    public OpCode getOpCode()
    {
        return OpCode.Error;
    }

    @Override
    public int size()
    {
        return 2;
    }

    @Override
    public Collection<byte[]> toBytes()
    {
        Collection<byte[]> out = new ArrayList<>();
        
        byte[] buffer = new byte[2];
        buffer[0] = OpCode.Error.getByte();
        buffer[1] = this.errorCode.getByte();
        
        out.add(buffer);
        
        return out;
        
    }
    
}
