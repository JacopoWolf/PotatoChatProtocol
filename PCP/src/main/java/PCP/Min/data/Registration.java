/*
 * this is a school project under "The Unlicence".
 */

package PCP.Min.data;

import PCP.*;
import PCP.data.IPCPData;
import java.util.*;

/**
 *
 * @author gfurri20
 * @author Alessio789
 */
public class Registration implements IPCPData
{
    private String alias;
    private String topic;
    
    public Registration( String alias , String topic )
    {
        this.alias = alias;
        this.topic = topic;
    }

    //<editor-fold defaultstate="collapsed" desc="getters and setters">

    public String getAlias() 
    {
        return alias;
    }

    public void setAlias( String alias ) 
    {
        this.alias = alias;
    }

    public String getTopic() 
    {
        return topic;
    }

    public void setTopic( String topic ) 
    {
        this.topic = topic;
    }
    
    //</editor-fold> 
    
    @Override
    public OpCode getOpCode()
    {
        return OpCode.Registration;
    }
    
    @Override
    public PCP.Versions getVersion()
    {
        return PCP.Versions.Min;
    }

    @Override
    public byte[] header()
    {
        byte[] buffer = new byte[2];
        
        int i = 0;
        buffer[i++] = OpCode.Registration.getByte();
        //Version, for now 0
        buffer[i++] = 0;
                       
        return buffer;
    }

    @Override
    public int size()
    {
        if ( this.topic == null )
            return 4 + alias.length();
        else
            return 4 + alias.length() + topic.length();
    }

    @Override
    public Collection<byte[]> toBytes()
    {
        Collection<byte[]> out = new ArrayList<>();
        byte[] buffer = new byte[this.size()];
        
        int i = 0;
        //Add the header to the package
        for( byte b : this.header() )
            buffer[i++] = b;
        
        //Adds the payload to the package
        
        //Alias
        for(byte b : alias.getBytes())
            buffer[i++] = b;
        //Delimitator
        buffer[i++] = 0;
        
        //Topic
        if ( this.topic != null )
        {
            for(byte b : topic.getBytes())
                buffer[i++] = b;
        }   
        //Delimitator
        buffer[i++] = 0;
        
        //Push the package into the collection
        out.add(buffer);
        
        return out;
    }
    
}
