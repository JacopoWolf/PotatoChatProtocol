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
        buffer[0] = OpCode.Registration.getByte();
        //Version, for now 0
        buffer[1] = 0;
        
        //Alias
        System.arraycopy(buffer, 2, alias.getBytes(), 0, alias.getBytes().length);
        //Delimitator
        buffer[2 + alias.length()] = 0;
        
        //Topic
        System.arraycopy(buffer, 3 + alias.length(), topic.getBytes(), 0, topic.getBytes().length);
        //Delimitator
        buffer[3 + alias.length() + topic.length()] = 0;
        
        out.add(buffer);
        
        return out;
    }
    
}
