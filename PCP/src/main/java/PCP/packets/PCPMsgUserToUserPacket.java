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
public class PCPMsgUserToUserPacket implements IPCPpacket
{
    private byte[] senderId;
    private String destinationAlias;
    private String message;


//<editor-fold defaultstate="collapsed" desc="getter and setters">
    
    public String getDestinationAlias()
    {
        return destinationAlias;
    }
    
    public void setDestinationAlias( String destinationAlias )
    {
        this.destinationAlias = destinationAlias;
    }
    
    public String getMessage()
    {
        return message;
    }
    
    public void setMessage( String message )
    {
        this.message = message;
    }
    
//</editor-fold>
    
    
    public PCPMsgUserToUserPacket( byte[] senderId, String dstAlias, String msg )
    {
        this.senderId = senderId;
        this.destinationAlias = dstAlias;
        this.message = msg;
    }
        
    @Override
    public OpCode getOpCode()
    {
        return OpCode.MsgUserToUser;
    }

    @Override
    public int size()
    {
        return 5 + destinationAlias.length() + message.length();
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
        for(byte b : destinationAlias.getBytes())
            buffer[i++] = b;
        //Delimitator
        buffer[i++] = 0;

        //Message
        for(byte b : message.getBytes())
            buffer[i++] = b;
        //Delimitator
        buffer[i++] = 0;
        
        //Control variable
        int size = buffer.length;
        int x = 2048;
        int y = 0;
        
        //If necessary split the packet
        while(size > 2048)
        {           
            byte[] splittedBuffer = Arrays.copyOfRange(buffer, y, x);
            y = x;
            x += 2048;
            size -= 2048;
            
            out.add(splittedBuffer);
        }
        //Create the last packet or the unique packet (if <= 2048)
        byte[] splittedBuffer = Arrays.copyOfRange(buffer, y, y + size);
        
        out.add(splittedBuffer);
        
        return out;
    }
    
}
