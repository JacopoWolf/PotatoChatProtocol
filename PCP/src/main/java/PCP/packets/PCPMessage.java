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
public abstract class PCPMessage implements IPCPpacket
{
    private OpCode opCode;
    private String message;

    //<editor-fold defaultstate="collapsed" desc="get set">
    public String getMessage()
    {
        return message;
    }
    
    public void setMessage( String message )
    {
        this.message = message;
        
    }
    
    @Override
    public OpCode getOpCode()
    {
        return opCode;
    }

    void setCode( OpCode code )
    {
        if ( code != OpCode.MsgUserToGroup && code != OpCode.MsgUserToUser )
            throw new IllegalArgumentException("message can't have not message opcode!");
        
        this.opCode = code;
    }
    
    
    
    //</editor-fold>

    public PCPMessage( String message )
    {
        this.message = message;
    }

    public PCPMessage( OpCode code, String message )
    {
        this.setCode(code);
        this.message = message;
    }

    
    @Override
    public final Collection<byte[]> toBytes()
    {
        // puts message here
        Collection<byte[]> out = new ArrayList<>();
        
        // allocates byte arrays
        byte[] messageBytes = this.message.getBytes( StandardCharsets.ISO_8859_1 );
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