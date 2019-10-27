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
public class PCPMsgUserToUserPacket implements IPCPpacket
{
    private byte[] senderId;
    private String dstAlias;
    private String msg;

    public PCPMsgUserToUserPacket( byte[] senderId, String dstAlias, String msg )
    {
        this.senderId = senderId;
        this.dstAlias = dstAlias;
        this.msg = msg;
    }
        
    @Override
    public OpCode getOpCode()
    {
        return OpCode.MsgUserToUser;
    }

    @Override
    public int size()
    {
        return 5 + dstAlias.length() + msg.length();
    }

    @Override
    public Collection<byte[]> toBytes()
    {
        Collection<byte[]> out = new ArrayList<>();
        byte[] buffer = new byte[this.size()];
        
        int i = 0;
        //Opcode
        buffer[i++] = OpCode.MsgUserToUser.getByte();
        //SenderID
        for(byte b : senderId)
            buffer[i++] = b;
        
        //destinationAlias
        for(byte b : dstAlias.getBytes())
            buffer[i++] = b;
        //Delimitator
        buffer[i++] = 0;

        //Message
        for(byte b : msg.getBytes())
            buffer[i++] = b;
        //Delimitator
        buffer[i++] = 0;   
        
        out.add(buffer);
        
        return out;
    }
    
}
