/*
 * this is a school project under "The Unlicence".
 */
package PCP.packages;

import PCP.*;
import PCP.errors.*;
import java.util.*;

/**
 *
 * @author Jacopo_Wolf
 */
public class PCPErrorPackage implements IPCPpacket
{
    private ErrorCode errorCode;

    public ErrorCode getErrorCode()
    {
        return errorCode;
    }

    public PCPErrorPackage( ErrorCode errorCode )
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
        throw new UnsupportedOperationException();
    }
    
}
