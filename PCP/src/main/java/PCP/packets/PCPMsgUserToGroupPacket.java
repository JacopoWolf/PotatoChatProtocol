/*
 * this is a school project under "The Unlicence".
 */

package PCP.packets;

import PCP.*;
import java.nio.charset.*;
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
    public byte[] header()
    {
        byte[] buffer = new byte[3];

        int i = 0;
        //Opcode
        buffer[i++] = OpCode.MsgUserToGroup.getByte();
        //SenderId
        for(byte b : senderId)
            buffer[i++] = b;
    
        return buffer;
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
        
        byte[] messageB = message.getBytes( StandardCharsets.ISO_8859_1 );
        
        int NpacketsToSent = 
                (
                    this.message.length() / 
                    (PCP.Min.MAX_PACKET_LENGHT - 4)
                ) 
                + 1 ;
        
        int messageRelativeMaxLenght = PCP.Min.MAX_PACKET_LENGHT - 4;
        
        
        int messagePointer = 0;
        for ( int packetN = 0; packetN < NpacketsToSent; packetN++ )
        {                       
            byte[] buffer = new byte[this.size()];
            
            int i = 0;
            
            //Adds the header
            for( byte b : this.header() )
                buffer[i++] = b;
            
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
