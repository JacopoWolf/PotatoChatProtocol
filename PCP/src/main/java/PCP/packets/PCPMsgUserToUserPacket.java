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
 * @author Jacopo_Wolf
 * @author Alessio789
 */
public class PCPMsgUserToUserPacket extends PCPMessage
{
    private byte[] senderId;
    private String destinationAlias;


//<editor-fold defaultstate="collapsed" desc="getter and setters">
    
    public String getDestinationAlias()
    {
        return destinationAlias;
    }
    
    public void setDestinationAlias( String destinationAlias )
    {
        this.destinationAlias = destinationAlias;
    }
    
    
//</editor-fold>

    public PCPMsgUserToUserPacket( byte[] senderId, String destinationAlias, String message )
    {
        super(OpCode.MsgUserToUser, message);
        this.senderId = senderId;
        this.destinationAlias = destinationAlias;
    }
    

    @Override
    public byte[] header()
    {
        byte[] buffer = new byte[this.size()];
        int i = 0;
        //Opcode
        buffer[i++] = OpCode.MsgUserToUser.getByte();

        //SenderID
        for(byte b : senderId)
            buffer[i++] = b;

        return buffer;
    }

    @Override
    public int size()
    {
        return 5 + destinationAlias.length() + getMessage().length();
    }

    @Override
    public Collection<byte[]> toBytes()
    {
        Collection<byte[]> out = new ArrayList<>();
        
        
        byte[] messageB = getMessage().getBytes( StandardCharsets.ISO_8859_1 );
        
        int NpacketsToSent = 
                (
                    this.getMessage().length() / 
                    (PCP.Min.MAX_PACKET_LENGHT - 5 - this.destinationAlias.length())
                ) 
                + 1 ;
        
        int messageRelativeMaxLenght = PCP.Min.MAX_PACKET_LENGHT - 5 - destinationAlias.length();
        
        
        int messagePointer = 0;
        for ( int packetN = 0; packetN < NpacketsToSent; packetN++ )
        {
        
            byte[] buffer = new byte[this.size()];
                       
            int i = 0;
            //Adds the header
            for( byte b : this.header() )
                buffer[i++] = b;            
            
            //Adds the payload
            
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
