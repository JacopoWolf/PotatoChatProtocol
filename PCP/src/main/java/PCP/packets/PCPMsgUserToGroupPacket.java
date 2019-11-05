/*
 * this is a school project under "The Unlicence".
 */

package PCP.packets;

import PCP.*;
import java.nio.charset.*;
import java.util.*;

/**
 *
 * @author Jacopo_Wolf
 * @author gfurri20
 * @author Alessio789
 */
public class PCPMsgUserToGroupPacket extends PCPMessage
{
    private byte[] senderId;
    
    

    //<editor-fold defaultstate="collapsed" desc="getter and setters">
    
    public byte[] getSenderId()
    {
        return senderId;
    }
    
    public void setSenderId( byte[] senderId ) 
    {
        this.senderId = senderId;
    }

    //</editor-fold>
    
    
    
    //<editor-fold defaultstate="collapsed" desc="constructors">

    
    public PCPMsgUserToGroupPacket( byte[] senderId, String message )
    {
        super(OpCode.MsgUserToGroup, message);
        this.senderId = senderId;
    }

    //</editor-fold>
    

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
        return 4 + getMessage().length();
    }

    @Override
    public Collection<byte[]> toBytes()
    {      
        Collection<byte[]> out = new ArrayList<>();
        
        byte[] messageB = getMessage().getBytes( StandardCharsets.ISO_8859_1 );
        
        int NpacketsToSent = 
                (
                    this.getMessage().length() / 
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
