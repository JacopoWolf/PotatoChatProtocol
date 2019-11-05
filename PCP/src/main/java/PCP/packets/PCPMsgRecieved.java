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
public class PCPMsgRecieved extends PCPMessage
{
    private String sourceAlias;

    
    //<editor-fold defaultstate="collapsed" desc="get set">
    public String getSourceAlias()
    {
        return sourceAlias;
    }
    
    public void setSourceAlias( String sourceAlias )
    {
        this.sourceAlias = sourceAlias;
    }
    //</editor-fold>


    //<editor-fold defaultstate="collapsed" desc="constructors">
        public PCPMsgRecieved( String sourceAlias, String message )
        {
            super(message);
            this.sourceAlias = sourceAlias;
        }

        public PCPMsgRecieved( String sourceAlias, OpCode code, String message )
        {
            super(code, message);
            this.sourceAlias = sourceAlias;
        }

    //</editor-fold>
    
    

    @Override
    public byte[] header()
    {
        byte[] header = new byte[ 2 + this.sourceAlias.length() ];
        
        int i = 0;
        
        header[i++] = this.getOpCode().getByte();
        
        for ( byte b : this.getMessage().getBytes(StandardCharsets.ISO_8859_1) )
            header[i++] = b;
        
        header[i++] = 0;
        
        return header;
    }

    @Override
    public int size()
    {
        return 3 + this.sourceAlias.length() + this.getMessage().length();
    }

    @Override
    public Collection<byte[]> toBytes()
    {
        Collection<byte[]> out = new ArrayList<>();
        
        byte[] header = this.header();
        byte[] messageB = getMessage().getBytes( StandardCharsets.ISO_8859_1 );
        
        int NpacketsToSent = 
                (
                    this.getMessage().length() / 
                    (PCP.Min.MAX_PACKET_LENGHT - 3 - this.sourceAlias.length())
                ) 
                + 1 ;
        
        int messageRelativeMaxLenght = PCP.Min.MAX_PACKET_LENGHT - 5 - sourceAlias.length();
        
        int messagePointer = 0;
        for ( int packetN = 0; packetN < NpacketsToSent; packetN++ )
        {
        
            byte[] buffer = new byte[this.size()];
                       
            int i = 0;
            //header
            for ( byte b : header )
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
