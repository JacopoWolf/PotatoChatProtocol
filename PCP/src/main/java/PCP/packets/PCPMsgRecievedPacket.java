/*
 * this is a school project under "The Unlicence".
 */
package PCP.packets;

import PCP.*;
import java.nio.charset.*;


/**
 *
 * @author Jacopo_Wolf
 */
public class PCPMsgRecievedPacket extends PCPMessage
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
        public PCPMsgRecievedPacket( String sourceAlias, String message )
        {
            super(message);
            this.sourceAlias = sourceAlias;
        }

        public PCPMsgRecievedPacket( String sourceAlias, OpCode code, String message )
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
        
        for ( byte b : this.sourceAlias.getBytes(StandardCharsets.ISO_8859_1) )
            header[i++] = b;
        
        header[i++] = 0;
        
        return header;
    }

    @Override
    public int size()
    {
        return 3 + this.sourceAlias.length() + this.getMessage().length();
    }

    
}
