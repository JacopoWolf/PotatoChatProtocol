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
 * @author Alessio789
 */
public class PCPErrorPacket implements IPCPpacket
{
    private ErrorCode errorCode;
    
    //<editor-fold defaultstate="collapsed" desc="getter and setters">
    public void setErrorCode( ErrorCode errorCode ) 
    {
        this.errorCode = errorCode;
    }
    
    public ErrorCode getErrorCode()
    {
        return errorCode;
    }
    
    //</editor-fold>

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
    public byte[] header()
    {
        byte[] buffer = new byte[2];
        buffer[0] = OpCode.Error.getByte();
        buffer[1] = this.errorCode.getByte();
        
        return buffer;
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
        
        //Add the header to the collection
        out.add(this.header());
        
        return out;
    }
    
}
