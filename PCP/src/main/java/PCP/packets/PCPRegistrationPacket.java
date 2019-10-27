/*
 * this is a school project under "The Unlicence".
 */

package PCP.packets;

import PCP.*;
import java.util.*;

/**
 *
 * @author gfurri20
 */
public class PCPRegistrationPacket implements IPCPpacket
{
    private String alias;
    private String topic;
    
    public PCPRegistrationPacket( String alias , String topic )
    {
        this.alias = alias;
        this.topic = topic;
    }

    @Override
    public OpCode getOpCode()
    {
        return OpCode.Registration;
    }

    @Override
    public int size()
    {
        return 4 + alias.length() + topic.length();
    }

    @Override
    public Collection<byte[]> toBytes()
    {
        Collection<byte[]> out = new ArrayList<>();
        
        byte[] buffer = new byte[4 + alias.length() + topic.length()];
        //Pointer
        int i = 0;
        buffer[i++] = OpCode.Registration.getByte();
        //Version, for now 0
        buffer[i++] = 0;
        
        //Alias
        for(byte b : alias.getBytes())
            buffer[i++] = b;
        //Delimitator
        buffer[i++] = 0;
        
        //Topic
        for(byte b : topic.getBytes())
            buffer[i++] = b;
        //Delimitator
        buffer[i++] = 0;
        
        out.add(buffer);
        
        return out;
    }
    
}
