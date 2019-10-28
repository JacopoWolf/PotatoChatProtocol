/*
 * this is a school project under "The Unlicence".
 */

package PCP.packets;
import PCP.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 *
 * @author gfurri20
 * @author Jacopo_Wolf
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
        
        
        byte[] messageB = message.getBytes( StandardCharsets.ISO_8859_1 );
        
        int NpacketsToSent = 
                (
                    this.message.length() / 
                    (IPCPpacket.MAX_PACKET_LENGHT - 5 - this.destinationAlias.length())
                ) 
                + 1 ;
        
        int messageRelativeMaxLenght = IPCPpacket.MAX_PACKET_LENGHT - 5 - destinationAlias.length();
        
        
        int messagePointer = 0;
        for ( int packetN = 0; packetN < NpacketsToSent; packetN++ )
        {
        
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
            for 
            ( 
                int relPointer = 0; 
                relPointer < messageRelativeMaxLenght 
                    && 
                messagePointer < messageB.length;
                relPointer++, messagePointer++
            )
            {
                buffer[i++] = messageB[ messagePointer ];
            }
            
            //Delimitator
            buffer[i++] = 0;
            

            out.add(buffer);
        }
        
        
        
        return out;
    }
    
}
