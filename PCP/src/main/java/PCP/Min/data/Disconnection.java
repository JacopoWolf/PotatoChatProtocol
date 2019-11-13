/*
 * this is a school project under "The Unlicence".
 */
package PCP.Min.data;

import PCP.data.IPCPData;
import PCP.*;
import java.util.*;


/**
 *
 * @author Alessio789
 * @author gfurri20
 */
public class Disconnection implements IPCPData
{
    public enum Reason
    {
        none        (0),
        timedOut    (1),
        goneOffline (2);
        
        
        
        private byte code;
        
        private Reason( int code )
        {
            this.code = (byte)code;
        }
        public byte getByte()
        {
            return this.code;
        }
    }
    
    private byte[] id;
    private Reason reason;
    private boolean byClient; //It's true if the disconnection has been required by the client

    public Disconnection( byte[] id ) 
    {
        this.id = id;
        this.byClient = true;
    }

    public Disconnection( Reason reason ) 
    {
        this.reason = reason;
        this.byClient = false;
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

    public Reason getReason() 
    {
        return reason;
    }

    public void setReason( Reason reason ) 
    {
        this.reason = reason;
    }

    public boolean isByClient() 
    {
        return byClient;
    }

    public void setByClient( boolean byClient ) 
    {
        this.byClient = byClient;
    }
    
   //</editor-fold>

    @Override
    public OpCode getOpCode() 
    {
        return OpCode.Disconnection;
    }
    
    @Override
    public PCP.Versions getVersion()
    {
        return PCP.Versions.Min;
    }
    
    @Override
    public byte[] header()
    {
        byte[] buffer = new byte[this.size()];
        
        int i = 0;
        
        buffer[i++] = OpCode.Disconnection.getByte();
        
        if( byClient )
            for(byte b : id) 
                buffer[i++] = b;
        else
            buffer[i++] = this.reason.getByte();
        
        return buffer;
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
       
        //Adds the header to the collection
        out.add(this.header());
        
        return out;
    }
    
}
