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
public class PCPMsgUserToGroupPacket implements IPCPpacket
{
    private byte[] senderId;
    private String message;

    public String getMessage()
    {
        return message;
    }

    public void setMessage( String message )
    {
        this.message = message;
    }

    public PCPMsgUserToGroupPacket( byte[] senderId, String msg )
    {
        this.senderId = senderId;
        this.message = msg;
    }
        
    @Override
    public OpCode getOpCode()
    {
        return OpCode.MsgUserToGroup;
    }

    @Override
    public int size()
    {
        return 4 + message.length();
    }

    @Override
    public Collection<byte[]> toBytes()
    {
        Collection<byte[]> out = new ArrayList<>();
        byte[] buffer = new byte[this.size()];
        
        int i = 0;
        //Opcode
        buffer[i++] = OpCode.MsgUserToUser.getByte();
        //SenderId
        for(byte b : senderId)
            buffer[i++] = b;
        
        //Message
        for(byte b : message.getBytes())
            buffer[i++] = b;
        //Delimitator
        buffer[i++] = 0;
        
        out.add(buffer);
        
        return out;
    }

}
