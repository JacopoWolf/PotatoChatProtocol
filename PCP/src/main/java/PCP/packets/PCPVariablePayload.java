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
 */
public abstract class PCPVariablePayload implements IPCPpacket
{
    /**
     * 
     * @return the variable lenght message to send
     */
    abstract String getMessage();
    
    @Override
    public final Collection<byte[]> toBytes()
    {
        // puts message here
        Collection<byte[]> out = new ArrayList<>();
        
        // allocates byte arrays
        byte[] messageBytes = this.getMessage().getBytes( StandardCharsets.ISO_8859_1 );
        byte[] header = this.header();
        
        // necessary to calculation
        int msgRelativeMaxLenght = 
                // MAX - header - payloadDelimitator
                PCP.Min.MAX_PACKET_LENGHT - header.length - 1;
        int nPacketsToSent = 
                // ( msgTOT / msgREL ) + 1
                ( this.getMessage().length() / msgRelativeMaxLenght ) + 1 ;
        

        int absMsgPointer = 0;  // absolute message pointer
        int packetN = 0;        // current packet number
        for ( ; packetN < nPacketsToSent; packetN++ )
        {
            int bufferLenght = 
                    ( messageBytes.length - absMsgPointer ) > msgRelativeMaxLenght ? 
                        PCP.Min.MAX_PACKET_LENGHT : 
                        (header.length + messageBytes.length + 1);
            byte[] buffer = new byte[ bufferLenght ];
                       
            int i = 0;
            // appends header
            for( byte b : this.header() )
                buffer[i++] = b;            

            // appends message payload
            for 
            ( 
                int relMsgPointer = 0; 
                relMsgPointer < msgRelativeMaxLenght && absMsgPointer < messageBytes.length;
                relMsgPointer++, absMsgPointer++
            )
            {
                buffer[i++] = messageBytes[ absMsgPointer ];
            }
            
            // delimitator
            buffer[i++] = 0;
            
            // add current packet to the list
            out.add(buffer);
            
        }

        return out;
    }
    
}
