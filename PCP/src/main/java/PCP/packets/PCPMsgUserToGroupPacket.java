/*
 * this is a school project under "The Unlicence".
 */

package PCP.packets;

import PCP.OpCode;
import java.util.*;

/**
 *
 * @author gfurri20
 */
public class PCPMsgUserToGroupPacket implements IPCPpacket
{
    private byte[] senderId;
    private String msg;

    public PCPMsgUserToGroupPacket( byte[] senderId, String msg )
    {
        this.senderId = senderId;
        this.msg = msg;
    }
        
    @Override
    public OpCode getOpCode()
    {
        return OpCode.MsgUserToGroup;
    }

    @Override
    public int size()
    {
        return 4 + msg.length();
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
        for(byte b : msg.getBytes())
            buffer[i++] = b;
        //Delimitator
        buffer[i++] = 0;
        
        out.add(buffer);
        
        return out;
    }

}
