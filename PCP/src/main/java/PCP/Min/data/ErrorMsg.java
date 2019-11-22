/*
 * this is a school project under "The Unlicence".
 */
package PCP.Min.data;

import PCP.data.IPCPData;
import PCP.*;
import PCP.PCPException.ErrorCode;
import java.util.*;


    /**
 *
 * @author Jacopo_Wolf
 * @author Alessio789
 */
public class ErrorMsg implements IPCPData
{     
    private ErrorCode errorCode;
    
    //<editor-fold defaultstate="collapsed" desc="getters and setters">
    public void setErrorCode( ErrorCode errorCode ) 
    {
        this.errorCode = errorCode;
    }
    
    public ErrorCode getErrorCode()
    {
        return errorCode;
    }
    
    //</editor-fold>

    
    public ErrorMsg ( PCPException exception )
    {
        this( exception.getErrorCode() );
    }
    
    public ErrorMsg( ErrorCode errorCode )
    {
        this.errorCode = errorCode;
    }
    
    
    @Override
    public OpCode getOpCode()
    {
        return OpCode.Error;
    }
    
    @Override
    public PCP.Versions getVersion()
    {
        return PCP.Versions.Min;
    }

    @Override
    public byte[] header()
    {
        byte[] buffer = new byte[1];
        buffer[0] = OpCode.Error.getByte();
        
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
        byte[] buffer = new byte[2];
        
        //Adds the header to the package
        buffer[0] = this.header()[0];
        
        //Adds the payload (error code)
        buffer[1] = this.errorCode.getByte();
        
        //Push the package into the collection
        out.add(buffer);
        
        return out;
    }
}
