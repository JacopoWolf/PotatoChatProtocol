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
    private byte[] id;
    private int reason;
    private boolean byClient; //It's true if the disconnection has been required by the client

    public PCPDisconnectionPacket( byte[] id ) 
    {
        this.id = id;
        this.byClient = true;
    }

    public PCPDisconnectionPacket( int reason ) 
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

    public int getReason() 
    {
        return reason;
    }

    public void setReason( int reason ) 
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
    public byte[] header()
    {
        byte[] buffer = new byte[this.size()];
        
        int i = 0;
        
        buffer[i++] = OpCode.Disconnection.getByte();
        
        if( byClient ) 
        {
            for(byte b : id) 
                buffer[i++] = b;
        }
        else
            buffer[i++] = (byte) this.reason;
        
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
       
        //Add the header to the collection
        out.add(this.header());
        
        return out;
    }
    
}
