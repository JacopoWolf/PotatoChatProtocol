/*
 * this is a school project under "The Unlicence".
 */

package PCP.packets;

import PCP.*;
import java.util.*;

/**
 *
 * @author gfurri20
 * @author Alessio789
 */
public class PCPMsgUserToGroupPacket implements IPCPpacket
{
    private byte[] senderId;
    private String message;
    
    public byte[] getSenderId()
    {
        return senderId;
    }

    //<editor-fold defaultstate="collapsed" desc="getter and setters">
    
    public void setSenderId( byte[] senderId ) 
    {
        this.senderId = senderId;
    }

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
        
    //</editor-fold>
    
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
