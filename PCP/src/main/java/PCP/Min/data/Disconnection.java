/*
 * this is a school project under "The Unlicence".
 */
package PCP.Min.data;

import PCP.*;
import PCP.data.*;
import java.util.*;


/**
 * rappresents a disconnection action
 * @author Alessio789
 * @author gfurri20
 */
public class Disconnection implements IPCPData
{

    /**
     * reasons for the disconnection
     */
    public enum Reason
    {

        /**
         * no reason
         */
        none        (0),

        /**
         * you jave timed out, meaning no activity for the default time
         */
        timedOut    (1),

        /**
         * the server has been shut down. Before that it will tell you.
         */
        goneOffline (2);
        
        
        
        private byte code;
        
        private Reason( int code )
        {
            this.code = (byte)code;
        }

        /**
         *
         * @return
         */
        public byte getByte()
        {
            return this.code;
        }
    }
    
    private byte[] id;
    private Reason reason;
    private boolean byClient; //It's true if the disconnection has been required by the client

    /**
     *
     * @param id
     */
    public Disconnection( byte[] id ) 
    {
        this.id = id;
        this.byClient = true;
    }

    /**
     *
     * @param reason
     */
    public Disconnection( Reason reason ) 
    {
        this.reason = reason;
        this.byClient = false;
    }
    
    //<editor-fold defaultstate="collapsed" desc="getters and setters">

    /**
     *
     * @return
     */
    
    public byte[] getId() 
    {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId( byte[] id ) 
    {
        this.id = id;
    }

    /**
     *
     * @return
     */
    public Reason getReason() 
    {
        return reason;
    }

    /**
     *
     * @param reason
     */
    public void setReason( Reason reason ) 
    {
        this.reason = reason;
    }

    /**
     *
     * @return
     */
    public boolean isByClient() 
    {
        return byClient;
    }

    /**
     *
     * @param byClient
     */
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
